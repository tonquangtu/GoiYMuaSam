package com.mycompany.mainui.Fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycompany.mainui.R;
import com.mycompany.mainui.RecyclerAdapter.RecyclerAdapterHotProduct;
import com.mycompany.mainui.SpaceItemRecyclerView;
import com.mycompany.mainui.actiivity.MainActivity;
import com.mycompany.mainui.controller3.HotListController;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.RequestId;
import com.mycompany.mainui.network.LoadHotList;
import com.mycompany.mainui.networkutil.StatusInternet;
import com.mycompany.mainui.view.DisplayNotification;

import java.util.ArrayList;

/**
 * Created by Mr.T on 4/15/2016.
 */
public class HotProductFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_LOAD_HOT = "ARG_LOAD_HOT";

    private LoadHotList loadHotList;
    private HotListController hotListController;

    public Activity context;
    boolean isLoading;
    private RecyclerAdapterHotProduct adapterHotProduct;
    Handler handler;
    ArrayList listData;
    SwipeRefreshLayout swipeContainer;
    boolean isRefresh;


    public static HotProductFragment newInstance(int page, LoadHotList loadHotList) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(ARG_LOAD_HOT, loadHotList);

        HotProductFragment fragment = new HotProductFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadHotList = (LoadHotList)getArguments().getSerializable(ARG_LOAD_HOT);
        hotListController = new HotListController();
        listData = loadHotList.getListData();
        context = getActivity();
        handler = new Handler();
        isLoading = false;
        isRefresh = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.from(context).inflate(R.layout.fragment_hot_product,container, false);
        //--------------------------------------------------------------------------------------------------

        // find list
        final RecyclerView list_hot_product =
                (RecyclerView) view.findViewById(R.id.recycler_hot_product);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        list_hot_product.setLayoutManager(layoutManager);

        // set adapter
        adapterHotProduct =
                new RecyclerAdapterHotProduct(context, listData, list_hot_product);
        list_hot_product.setAdapter(adapterHotProduct);
        // add space
        SpaceItemRecyclerView space = new SpaceItemRecyclerView(15, SpaceItemRecyclerView.GRID);
        list_hot_product.addItemDecoration(space);


        // Hide TabLayout
        list_hot_product.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        adapterHotProduct.setOnLoadMoreListener(new RecyclerAdapterHotProduct.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(!isLoading) {
                    handleLoadMore();
                }
            }
        });

        // capture event when user click a item in list
        adapterHotProduct.setOnItemClickListener(new RecyclerAdapterHotProduct.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                hotListController.handle(context, listData, position);
            }
        });


        // Lookup the swipe container icon_view
         swipeContainer = (SwipeRefreshLayout)
                view.findViewById(R.id.swipe_refresh_hot_product);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isRefresh) {
                    handleRefresh();
                }
            }
        });

        return view;
    }

    public synchronized void handleLoadMore() {
        if (!loadHotList.getMessage().equals(RequestId.MES_NOTHING)) {
            if(StatusInternet.isInternet(context)) {
                isLoading = true;
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        listData.add(null);
                        adapterHotProduct.notifyItemInserted(listData.size() - 1);
                    }

                    @Override
                    protected Void doInBackground(Void... params) {

                        loadHotList.loadHotProduct(context);
                        while (!loadHotList.isLoadHotCatalogComplete()) {
                            SystemClock.sleep(ConfigData.TIME_SLEEP);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        listData.remove(listData.size() - 1);
                        adapterHotProduct.notifyItemRemoved(listData.size());

                        int oldSize = listData.size();
                        if (loadHotList.addLoadMoreList()) {
                            int newSize = listData.size();

                            for(int i = oldSize; i < newSize ; i++) {
                                adapterHotProduct.notifyItemInserted(i);
                            }
                        }
                       isLoading = false;
                    }
                }.execute();
            }else {
                DisplayNotification.displayNotifiNoNetwork(context);
            }
        }else {
//            Toast.makeText(context, "Nothing hot product", Toast.LENGTH_SHORT).show();
        }
    }

    public synchronized void handleRefresh() {

        if(StatusInternet.isInternet(context)) {
            if(listData == null || listData.size() == 0) {

                isRefresh = true;
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {

                        loadHotList.loadHotProduct(context);
                        while(!loadHotList.isLoadHotCatalogComplete()) {
                            SystemClock.sleep(ConfigData.TIME_SLEEP);
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        if(loadHotList.addLoadMoreList()) {
                            adapterHotProduct.notifyDataSetChanged();
                        }
                        swipeContainer.setRefreshing(false);
                        isRefresh = false;
                    }
                }.execute();
            }else {
                swipeContainer.setRefreshing(false);
            }
        }else {
            swipeContainer.setRefreshing(false);
        }

    }


}