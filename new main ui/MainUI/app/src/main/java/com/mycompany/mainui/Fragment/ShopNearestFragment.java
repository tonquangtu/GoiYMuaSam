package com.mycompany.mainui.Fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycompany.mainui.R;
import com.mycompany.mainui.RecyclerAdapter.RecyclerAdapterShopNearest;
import com.mycompany.mainui.SpaceItemRecyclerView;
import com.mycompany.mainui.actiivity.MainActivity;
import com.mycompany.mainui.controller3.ShopListSuggestionController;
import com.mycompany.mainui.model.ShortShop;
import com.mycompany.mainui.network.LoadShopListSuggestion;
import com.mycompany.mainui.networkutil.StatusInternet;
import com.mycompany.mainui.view.DisplayNotification;

import java.util.ArrayList;

/**
 * Created by Mr.T on 4/13/2016.
 */
public class ShopNearestFragment extends Fragment{

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_LOAD_SHOP_SUGGESTION = "LOAD_SHOP_SUGGESTION";

    private int mPage;
    public Activity context;

    ArrayList<ShortShop> listData;
    RecyclerAdapterShopNearest adapter;
    Handler handler;
    private LoadShopListSuggestion loadSuggestion;
    private ShopListSuggestionController shopController;




    public static ShopNearestFragment newInstance(int page,LoadShopListSuggestion loadSuggestion) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(ARG_LOAD_SHOP_SUGGESTION, loadSuggestion);

        ShopNearestFragment fragment = new ShopNearestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);

        loadSuggestion = (LoadShopListSuggestion)getArguments().getSerializable(ARG_LOAD_SHOP_SUGGESTION);
        shopController = new ShopListSuggestionController(loadSuggestion);

        listData = loadSuggestion.getShopList();
        handler = new Handler();
        context = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        // Inflate fragment_shop_nearest.xml
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
        SpaceItemRecyclerView decoration =
                new SpaceItemRecyclerView(10, SpaceItemRecyclerView.VERTICAL);
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
                // add progressBar

            if (StatusInternet.isInternet(context)) {
                AsyncTask<Void, Void, Void> taskLoadMore = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        listData.add(null);
                        adapter.notifyItemInserted(listData.size() - 1);

                    }

                    @Override
                    protected Void doInBackground(Void... params) {

                        loadSuggestion.loadShopListSuggestion(context);
                        while (!loadSuggestion.isLoadShopComplete()) ;

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        listData.remove(listData.size() - 1);
                        adapter.notifyItemRemoved(listData.size());

                        if (loadSuggestion.addLoadMoreList()) {
                            adapter.notifyDataSetChanged();
                        }
                        adapter.setLoaded();
                    }
                }.execute();
            }else {
                DisplayNotification.displayNotifiNoNetwork(context);
            }

            }
        });

      //-----------------------------------------------------------------------------------------------------
        adapter.setOnItemClickListener(new RecyclerAdapterShopNearest.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                ShortShop itemShop = listData.get(position);
                if(itemShop != null) {
                    shopController.handleOnCLickItem(context, itemShop);
                }
            }
        });

        //----------------------------------------------------------------------------------------------------
        // Lookup the swipe container icon_view
        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout)
                view.findViewById(R.id.swipe_refresh_shop_nearest);

        // Setup refresh listener which triggers new data loading

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {
                swipeContainer.setRefreshing(false);
                // Your code to refresh the list here.

                // Make sure you call swipeContainer.setRefreshing(false)

                // once the network request has completed successfully.

                //fetchTimelineAsync(0);

            }

        });

        // Configure the refreshing colors

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);

        return view;
    }
    /*public void fetchTimelineAsync(int page) {

        // Send the network request to fetch the updated data

        // `client` here is an instance of Android Async HTTP

       client.getHomeTimeline(0, new JsonHttpResponseHandler() {

            public void onSuccess(JSONArray json) {

                // Remember to CLEAR OUT old items before appending in the new ones

                adapter.clear();

                // ...the data has come back, add new items to your adapter...

                adapter.addAll(...);

                // Now we call setRefreshing(false) to signal refresh has finished

                swipeContainer.setRefreshing(false);

            }



            public void onFailure(Throwable e) {

                Log.d("DEBUG", "Fetch timeline error: " + e.toString());

            }

        });

    }*/



}
