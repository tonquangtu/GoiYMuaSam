package com.mycompany.mainui.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mycompany.mainui.R;
import com.mycompany.mainui.RecyclerAdapter.RecyclerAdapterProductListInAd;
import com.mycompany.mainui.SpaceItemRecyclerView;
import com.mycompany.mainui.actiivity.MainActivity;
import com.mycompany.mainui.actiivity.ProductDetailActivity;
import com.mycompany.mainui.model.AdProductCatalog;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.RequestId;
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.network.LoadProductListInAd;
import com.mycompany.mainui.networkutil.StatusInternet;
import com.mycompany.mainui.view.DisplayNotification;

import java.util.ArrayList;

/**
 * Created by Dell on 4/23/2016.
 */
public class ProductListInAdFragment extends Fragment {

    LoadProductListInAd loadProductListInAd;
    private static String ARG_LOAD_PRODUCT = "ARG_LOAD_PRODUCT";
    private static String ARG_AD_PRODUCT_CATALOG = "ARG_AD_PRODUCT_CATALOG";
    Activity activity;
    ArrayList<ShortProduct> listData;
    AdProductCatalog adProductCatalog;
    RecyclerAdapterProductListInAd adapter;
    SwipeRefreshLayout refreshLayout;
    boolean isLoading = false;
    boolean isRefresh = false;

    public static ProductListInAdFragment newInstance(
            LoadProductListInAd loadProductListInAd,
            AdProductCatalog adProductCatalog ) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_LOAD_PRODUCT, loadProductListInAd);
        args.putSerializable(ARG_AD_PRODUCT_CATALOG, adProductCatalog);
        ProductListInAdFragment fragment = new ProductListInAdFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadProductListInAd = (LoadProductListInAd) getArguments().getSerializable(ARG_LOAD_PRODUCT);
        adProductCatalog = (AdProductCatalog)getArguments().getSerializable(ARG_AD_PRODUCT_CATALOG);
        listData = loadProductListInAd.getShortProducts();
        activity = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //-------------------------------------------------------------------------------------------------------------
        View view = inflater.inflate(R.layout.fragment_product_list_in_ad, container, false);

        RecyclerView recyclerViewProducts = (RecyclerView) view.findViewById(R.id.product_list_in_ad_recycler);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.product_list_in_ad_swipe_refresh);

        adapter = new RecyclerAdapterProductListInAd(activity, recyclerViewProducts, listData, adProductCatalog);
        recyclerViewProducts.setAdapter(adapter);


        //----------------------------------------------------------------------------------------------------------
        // hide app bar
        recyclerViewProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 20) {
                    if (MainActivity.tabs.getVisibility() == View.VISIBLE) {
                        MainActivity.tabs.setVisibility(View.INVISIBLE);
                    }

                } else if (dy < -20) {
                    if (MainActivity.tabs.getVisibility() == View.INVISIBLE)
                        MainActivity.tabs.setVisibility(View.VISIBLE);
                }
            }
        });

        //-------------------------------------------------------------------------------------------------------------
        // layout contain 2 column
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerViewProducts.setLayoutManager(layoutManager);


        //--------------------------------------------------------------------------------------------------------------
        //space between other item in recycler view
        SpaceItemRecyclerView space = new SpaceItemRecyclerView(10, SpaceItemRecyclerView.GRID);
        recyclerViewProducts.addItemDecoration(space);

        //--------------------------------------------------------------------------------------------------------------

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isRefresh) {
                    handleOnRefresh();
                }
            }
        });

        //--------------------------------------------------------------------------------------------------------------------
        adapter.setOnItemClickListener(new RecyclerAdapterProductListInAd.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                handleOnClickItem(position);
            }
        });

        //---------------------------------------------------------------------------------------------------------------------
        adapter.setOnLoadMoreListener(new RecyclerAdapterProductListInAd.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(!isLoading) {
                    handleOnLoadMore();
                }
            }
        });

        return view;
    }

    public void handleOnClickItem(int position) {
        Toast.makeText(activity, "Ad list : " + position, Toast.LENGTH_LONG).show();
        ShortProduct shortProduct = listData.get(position);
        if(shortProduct != null) {

            Intent intent = new Intent(activity, ProductDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConfigData.ARG_SHORT_PRODUCT, shortProduct);
            intent.putExtra(ConfigData.ARG_PACKAGE, bundle);
            activity.startActivity(intent);
        }
    }

    public void handleOnLoadMore() {

        if(StatusInternet.isInternet(activity)) {
            if(!loadProductListInAd.getMessage().equals(RequestId.MES_NOTHING)) {

                isLoading = true;
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        listData.add(null);
                        adapter.notifyItemInserted(listData.size());
                    }

                    @Override
                    protected Void doInBackground(Void... params) {

                        loadProductListInAd.loadProductListInAd(activity,adProductCatalog.getAdProductCatalogName());
                        while(!loadProductListInAd.isLoadComplete()) {
                            SystemClock.sleep(ConfigData.TIME_SLEEP);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        listData.remove(listData.size() -1);
                        adapter.notifyItemRemoved(listData.size() + 1);
                        int oldSize = listData.size();

                        if(loadProductListInAd.addLoadMore()) {
                            int newSize = listData.size();
                            for(int i = oldSize ; i < newSize; i++) {
                                adapter.notifyItemInserted(i + 1);
                            }
                        }
                        isLoading = false;
                    }
                }.execute();
            }else {
                DisplayNotification.toast(activity, "Nothing ad product");
            }
        }else {
            DisplayNotification.displayNotifiNoNetwork(activity);
        }
    }

    public void handleOnRefresh(){

        if(StatusInternet.isInternet(activity)) {
            if(listData == null || listData.size() == 0) {
                isRefresh = true;
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {

                        loadProductListInAd.loadProductListInAd(activity, adProductCatalog.getAdProductCatalogName());
                        while(!loadProductListInAd.isLoadComplete()) {
                            SystemClock.sleep(10);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        refreshLayout.setRefreshing(false);
                        isRefresh = false;
                    }
                }.execute();
            }else {
                refreshLayout.setRefreshing(false);
            }
        }else {
            refreshLayout.setRefreshing(false);
        }
    }



}
