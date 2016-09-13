package com.mycompany.mainui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycompany.mainui.R;
import com.mycompany.mainui.actiivity.ShopDetailActivity;
import com.mycompany.mainui.adapter.RecyclerAdapterShopNearest;
import com.mycompany.mainui.SpaceItem;
import com.mycompany.mainui.actiivity.MainActivity;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.RequestId;
import com.mycompany.mainui.model.ShortShop;
import com.mycompany.mainui.network.LoadSearchShop;
import com.mycompany.mainui.networkutil.StatusInternet;
import com.mycompany.mainui.view.DisplayNotification;

import java.util.ArrayList;

/**
 * Created by Dell on 07-May-16.
 */
public class FragmentResultSearchShop extends Fragment {

    public static final String ARG_LOAD_SEARCH_SHOP = "LOAD_SEARCH_SHOP";
    public static final String ARG_QUERY = "ARG_QUERY";
    public Activity context;

    ArrayList<ShortShop> listData;
    RecyclerAdapterShopNearest adapter;
    private LoadSearchShop loadSearchShop;
    SwipeRefreshLayout swipeContainer;
    boolean isRefresh;
    boolean isLoading;
    String query;

    public static FragmentResultSearchShop newInstance(String query, LoadSearchShop loadSearchShop) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_LOAD_SEARCH_SHOP, loadSearchShop);
        args.putString(ARG_QUERY, query);

        FragmentResultSearchShop fragment = new FragmentResultSearchShop();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        loadSearchShop = (LoadSearchShop)bundle.getSerializable(ARG_LOAD_SEARCH_SHOP);
        query = bundle.getString(ARG_QUERY);

        listData = loadSearchShop.getListData();
        context = getActivity();
        isLoading = false;
        isRefresh = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        // Inflate fragment_shop_nearest.xml

        if(listData == null || listData.size() == 0) {
            return inflater.inflate(R.layout.no_result_search_layout, container, false);
        }
        View view = inflater.inflate(R.layout.fragment_shop_nearest, container, false);

        // Find RecyclerView
        final RecyclerView list_shop_nearest = (RecyclerView)view.findViewById(R.id.recycler_shop_nearest);

        // Set layoutManager always before adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list_shop_nearest.setLayoutManager(layoutManager);

        // Set Adapter
        adapter = new RecyclerAdapterShopNearest(context,listData, list_shop_nearest);
        list_shop_nearest.setAdapter(adapter);

        // Smooth
        list_shop_nearest.setHasFixedSize(true);
        layoutManager.isSmoothScrolling();

        // Add divider
        SpaceItem decoration =
                new SpaceItem(15, SpaceItem.VERTICAL);
        list_shop_nearest.addItemDecoration(decoration);

        // Hide TabLayout
        list_shop_nearest.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        // ----------------------------------------------------------------------------------------------------
        // Load data when endless recycler
        adapter.setOnLoadMoreListener(new RecyclerAdapterShopNearest.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(!isLoading) {
                    handleLoadMore();
                }
                // add progressBar
            }
        });

        //-----------------------------------------------------------------------------------------------------
        adapter.setOnItemClickListener(new RecyclerAdapterShopNearest.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                ShortShop itemShop = listData.get(position);
                if(itemShop != null) {
                    if(StatusInternet.isInternet(context)) {
                        Intent intent = new Intent(context, ShopDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(ConfigData.ARG_ID_SHOP, itemShop.getIdShop());
                        bundle.putString(ConfigData.ARG_SHOP_NAME, itemShop.getShopName());
                        intent.putExtra(ConfigData.ARG_PACKAGE, bundle);
                        context.startActivity(intent);
                    }
//                    shopController.handleOnCLickItem(context, itemShop);
                }
            }
        });

        //----------------------------------------------------------------------------------------------------
        // Lookup the swipe container icon_view1
        swipeContainer = (SwipeRefreshLayout)
                view.findViewById(R.id.swipe_refresh_shop_nearest);

        // Setup refresh listener which triggers new data loading

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {
                if(!isRefresh) {
                    handleRefresh();
                }
            }

        });

        // Configure the refreshing colors

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);

        return view;
    }

    public void handleLoadMore() {
        if (!loadSearchShop.getMessage().equals(RequestId.MES_NOTHING)) {
            if(StatusInternet.isInternet(context)) {

                isLoading = true;
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        listData.add(null);
                        adapter.notifyItemInserted(listData.size() - 1);

                    }

                    @Override
                    protected Void doInBackground(Void... params) {

                        loadSearchShop.loadShops(context, query);
                        while (!loadSearchShop.isLoadComplete()) {
                            SystemClock.sleep(ConfigData.TIME_SLEEP);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        listData.remove(listData.size() - 1);
                        adapter.notifyItemRemoved(listData.size());

                        int oldSize = listData.size();
                        if (loadSearchShop.addLoadMore()) {
                            int newSize = listData.size();
                            for(int i = oldSize; i < newSize; i++) {
                                adapter.notifyItemInserted(i);
                            }
                        }
                        isLoading = false;
                    }
                }.execute();
            }else {
                DisplayNotification.displayNotifiNoNetwork(context);
            }
        }else {
//            Toast.makeText(context, "Nothing shop", Toast.LENGTH_SHORT).show();
        }
    }

    public void handleRefresh() {

        if(listData == null || listData.size() == 0) {
            if(StatusInternet.isInternet(context)) {
                isRefresh = true;
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {

                        loadSearchShop.loadShops(context, query);
                        while(!loadSearchShop.isLoadComplete()) {
                            SystemClock.sleep(ConfigData.TIME_SLEEP);
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        if(loadSearchShop.addLoadMore()) {
                            adapter.notifyDataSetChanged();
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
