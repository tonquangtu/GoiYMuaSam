package com.mycompany.mainui.Fragment;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycompany.mainui.R;
import com.mycompany.mainui.RecyclerAdapter.RecyclerAdapterRecommend;
import com.mycompany.mainui.SlidingImageAdapter;
import com.mycompany.mainui.SpaceItemRecyclerView;
import com.mycompany.mainui.actiivity.MainActivity;
import com.mycompany.mainui.controller3.ProductListSuggestionController;
import com.mycompany.mainui.model.AdProductCatalog;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.RequestId;
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.network.LoadProductListSuggestion;
import com.mycompany.mainui.networkutil.StatusInternet;
import com.mycompany.mainui.view.DisplayNotification;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by Mr.T on 4/14/2016.
 */
public class RecommendFragment extends Fragment {


    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_LOAD_SUGGESTION = "LOAD_SUGGESTION";

    private LoadProductListSuggestion loadSuggestion;
    private final int TIME_SWIPE = 6000;
//    private View.OnClickListener adListener;
    private ProductListSuggestionController suggestionController;
    boolean isRefresh = false;
    boolean isLoading = false;

    public Activity context;
    //attr of Slide Image
    private boolean runTimer;
    private static ViewPager mPager;
    RecyclerAdapterRecommend adapterRecommend;
    ArrayList<ShortProduct> listData;
    ArrayList<AdProductCatalog> adProductCatalogs;
    SwipeRefreshLayout swipeContainer;
    SlidingImageAdapter slidingImageAdapter;

    // image for slide image of header main UI

    private static final int NUM_PAGES = 3;
    private Handler handler;
    Timer swipeTimer;
    int last_dy = 0;
    RecyclerAdapterRecommend.OnItemClickAdListener adListener;

    public static RecommendFragment newInstance(int page, LoadProductListSuggestion loadSuggestion ) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(ARG_LOAD_SUGGESTION, loadSuggestion);

        RecommendFragment fragment = new RecommendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadSuggestion = (LoadProductListSuggestion)getArguments().getSerializable(ARG_LOAD_SUGGESTION);
        adProductCatalogs = loadSuggestion.getAdProductCatalogs();
        listData = loadSuggestion.getShortProducts();

        suggestionController = new ProductListSuggestionController();
        context = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.from(context).inflate(R.layout.fragment_recommend,container, false);
        //--------------------------init slide image---------------------------------
        // init ViewPager for slide image

            //--------------------------------------------------------------------------------------------------
        final RecyclerView list_recommend = (RecyclerView) view.findViewById(R.id.recycler_recommend);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
//        recyclerView.setLayoutManager(layoutManager);
        // co the custom de cho recycler co the cuon hoac khong cuon, coi trong phien ban truoc
        list_recommend.setLayoutManager(layoutManager);

            //---------------------------------------------------------------------------------------------------------

        list_recommend.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        //--------------------------------------------------------------------------------------------------------------

        adListener = new RecyclerAdapterRecommend.OnItemClickAdListener() {
            @Override
            public void onItemClick(int position) {
                suggestionController.handleClickAdProductCatalog(context, adProductCatalogs.get(position));
            }
        };

        adapterRecommend =
                new RecyclerAdapterRecommend(context, list_recommend,  listData, adProductCatalogs, adListener);
        list_recommend.setAdapter(adapterRecommend);
        slidingImageAdapter = RecyclerAdapterRecommend.slidingImageAdapter;

        SpaceItemRecyclerView space = new SpaceItemRecyclerView(15, SpaceItemRecyclerView.GRID);
//        SpaceItemRecyclerView space = new SpaceItemRecyclerView(30,40);
        list_recommend.addItemDecoration(space);

        //---------------------------------------------------------------------------------------------------------------

        // Load data when endless recycler
        adapterRecommend.setOnLoadMoreListener(new RecyclerAdapterRecommend.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                if(!isLoading) {
                    handleOnLoadMore();
                }
            }
        });

        //--------------------------------------------------------------------------------------------------------------

        // bat su kien khi chon 1 item suggestion
        adapterRecommend.setOnItemClickListener(new RecyclerAdapterRecommend.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                ShortProduct shortProduct = listData.get(position);
                if(shortProduct != null) {
                    suggestionController.handleClickProductSuggestion(context, shortProduct);
                }
            }
        });

        //---------------------------------------------------------------------------------------------------------------
        // Lookup the swipe container icon_view
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

            return view;
        }
        //------------------------------------------------------------------------------------------------------------------
//    @Override
//    public void onStop() {
//        super.onStop();
//        if(swipeTimer != null){
//            swipeTimer.cancel();
//            swipeTimer = null;
//        }
//    }

    public void handleOnLoadMore() {

        if (!loadSuggestion.getMessage().equals(RequestId.MES_NOTHING)) {
            if(StatusInternet.isInternet(context)) {
                isLoading = true;
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        listData.add(null);
                        adapterRecommend.notifyItemInserted(listData.size());
                    }

                    @Override
                    protected Void doInBackground(Void... params) {

                        loadSuggestion.loadProductListSuggestion(context);
                        while (!loadSuggestion.isLoadProductComplete()) {
                            SystemClock.sleep(ConfigData.TIME_SLEEP);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
//                        Toast.makeText(context, "Load more", Toast.LENGTH_SHORT).show();
                        listData.remove(listData.size() - 1);
                        adapterRecommend.notifyItemRemoved(listData.size() + 1);

                        int oldSize = listData.size();
                        if (loadSuggestion.addLoadMoreList()) {
                            int newSize = listData.size();
                            for(int i = oldSize; i < newSize ; i++) {
                                adapterRecommend.notifyItemInserted(i + 1);
                            }
                        }
                        isLoading = false;
                    }
                }.execute();
            }else {
                DisplayNotification.displayNotifiNoNetwork(context);
            }
        }else {
//            Toast.makeText(context, "Nothing product", Toast.LENGTH_SHORT).show();
        }
    }

    public void handleRefresh() {

        if (adProductCatalogs.size() == 0 || listData.size() == 0) {
            if (StatusInternet.isInternet(context)) {

                isRefresh = true;
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {

                        if(adProductCatalogs.size() == 0 && listData.size() == 0) {
                            loadSuggestion.loadAdProduct(context);
                            loadSuggestion.loadProductListSuggestion(context);
                            while(!loadSuggestion.isLoadProductComplete() ||
                                    !loadSuggestion.isLoadAdComplete()) {
                                SystemClock.sleep(ConfigData.TIME_SLEEP);
                            }
                            loadSuggestion.addLoadMoreList();

                        }else if(adProductCatalogs.size() == 0) {
                            loadSuggestion.loadAdProduct(context);
                            while(!loadSuggestion.isLoadAdComplete()) {
                                SystemClock.sleep(ConfigData.TIME_SLEEP);
                            }
                        }else if(listData.size() == 0) {
                            while(!loadSuggestion.isLoadProductComplete()) {
                                SystemClock.sleep(ConfigData.TIME_SLEEP);
                            }
                            loadSuggestion.addLoadMoreList();
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        slidingImageAdapter.notifyDataSetChanged();
                        adapterRecommend.notifyDataSetChanged();
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
