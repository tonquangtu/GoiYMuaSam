package com.mycompany.mainui.Fragment;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.mycompany.mainui.R;
import com.mycompany.mainui.RecyclerAdapter.RecyclerAdapterRecommend;
import com.mycompany.mainui.SlidingImageAdapter;
import com.mycompany.mainui.SpaceItemRecyclerView;
import com.mycompany.mainui.actiivity.MainActivity;
import com.mycompany.mainui.controller3.ProductListSuggestionController;
import com.mycompany.mainui.model.AdProductCatalog;
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.network.LoadProductListSuggestion;
import com.mycompany.mainui.networkutil.StatusInternet;
import com.mycompany.mainui.view.DisplayNotification;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mr.T on 4/14/2016.
 */
public class RecommendFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_LOAD_SUGGESTION = "LOAD_SUGGESTION";

    private LoadProductListSuggestion loadSuggestion;
    private final int TIME_SWIPE = 4000;
    private View.OnClickListener adListener;
    private ProductListSuggestionController suggestionController;


    public Activity context;
    private int mPage;
    //attr of Slide Image
    private boolean runTimer;
    private static ViewPager mPager;
    private static int currentPage = 0;

    ArrayList<ShortProduct> listData;
    ArrayList<AdProductCatalog> adProductCatalogs;

    // image for slide image of header main UI

    private static final int NUM_PAGES = 3;
    private Handler handler;
    Timer swipeTimer;
    int last_dy = 0;

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
        mPage = getArguments().getInt(ARG_PAGE);
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

        adListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggestionController.handleClickAdProductCatalog(context, adProductCatalogs.get(currentPage));
            }
        };

        mPager = (ViewPager) view.findViewById(R.id.image_pager);
        mPager.setAdapter(new SlidingImageAdapter(context,adProductCatalogs, adListener));
        // init indicator
        CirclePageIndicator indicator = (CirclePageIndicator)
                view.findViewById(R.id.indicator);
        // set indicator for ViewPager
        indicator.setViewPager(mPager);
        mPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPager.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


            // Auto start of viewpager
            handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };
            swipeTimer = new Timer();
            runTimer=true;
            swipeTimer.schedule(new

            TimerTask() {
                @Override
                public void run () {
                    if (runTimer)
                        handler.post(Update);
                }
            }

            ,TIME_SWIPE,TIME_SWIPE);
            //--------------------------------------------------------------------------------------------------
            final RecyclerView list_recommend = (RecyclerView) view.findViewById(R.id.recycler_recommend);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,1){
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            list_recommend.setLayoutManager(layoutManager);
            // ScrollView Hide TabLayout
            final ScrollView scrollView = (ScrollView)view.findViewById(R.id.scrollView_recommend);
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                int last = 0;
                @Override
                public void onScrollChanged() {
                    int dy = scrollView.getScrollY();
                    if(dy != last){

                        if((dy - last) > 50){
                            if (MainActivity.tabs.getVisibility() == View.VISIBLE) {
                                MainActivity.tabs.setVisibility(View.INVISIBLE);
                            }
                        }
                        else if((dy - last) < -50){
                            if (MainActivity.tabs.getVisibility() == View.INVISIBLE)
                                MainActivity.tabs.setVisibility(View.VISIBLE);
                        }
                        last = dy;
                    }
                }
            });


        final RecyclerAdapterRecommend adapterRecommend =
                new RecyclerAdapterRecommend(context, listData, list_recommend, scrollView);
        list_recommend.setAdapter(adapterRecommend);

        SpaceItemRecyclerView space = new SpaceItemRecyclerView(10, SpaceItemRecyclerView.GRID);
        list_recommend.addItemDecoration(space);


        // Load data when endless recycler
        adapterRecommend.setOnLoadMoreListener(new RecyclerAdapterRecommend.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                if (StatusInternet.isInternet(context)) {
                    AsyncTask<Void, Void, Void> taskLoadMore = new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            listData.add(null);
                            adapterRecommend.notifyItemInserted(listData.size() - 1);

                        }

                        @Override
                        protected Void doInBackground(Void... params) {

                            loadSuggestion.loadProductListSuggestion(context);
                            while (!loadSuggestion.isLoadProductComplete()) ;

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);

                            listData.remove(listData.size() - 1);
                            adapterRecommend.notifyItemRemoved(listData.size());

                            if (loadSuggestion.addLoadMoreList()) {
                                adapterRecommend.notifyDataSetChanged();
                            }
                            adapterRecommend.setLoaded();
                        }
                    }.execute();
                }else {
                    DisplayNotification.displayNotifiNoNetwork(context);
                }
            }
        });



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

            return view;
        }

    @Override
    public void onStop() {
        super.onStop();
        if(swipeTimer != null){
            swipeTimer.cancel();
            swipeTimer = null;
        }
    }
}
