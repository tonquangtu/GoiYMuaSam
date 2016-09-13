package com.mycompany.mainui.fragment;

import android.content.Context;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.mycompany.mainui.R;
import com.mycompany.mainui.SpaceItem;
import com.mycompany.mainui.actiivity.MainActivity;
import com.mycompany.mainui.actiivity.ProductDetailActivity;
import com.mycompany.mainui.actiivity.ProductListInHotCatalogActivity;
import com.mycompany.mainui.adapter.RecyclerAdapterProductListInCatalog;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.RequestId;
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.network.LoadProductListInHotCatalog;
import com.mycompany.mainui.networkutil.StatusInternet;
import com.mycompany.mainui.view.DisplayNotification;

import java.util.ArrayList;

/**
 * Created by Dell on 4/28/2016.
 */
public class FragmentProductListInHotCatalog extends Fragment {

    ProductListInHotCatalogActivity activity;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    LoadProductListInHotCatalog loadProductHot;
    public static final String ARG_LOAD_PRODUCT_HOT = "ARG_LOAD_PRODUCT_HOT";
    public static final String ARG_TOOL_HEIGHT = "ARG_TOOL_HEIGHT";
    public static final String ARG_CATALOG = "ARG_CATALOG";
    ArrayList<ShortProduct> listData;
    RecyclerAdapterProductListInCatalog adapter;
    int mToolContainerHeight;
    boolean isLoading;
    boolean isRefresh;
    String catalog;


    public static FragmentProductListInHotCatalog newInstance(LoadProductListInHotCatalog loadProductHot,
                                                              int mToolContainerHeight,
                                                              String catalog) {

        FragmentProductListInHotCatalog fragment = new FragmentProductListInHotCatalog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOAD_PRODUCT_HOT, loadProductHot);
        args.putInt(ARG_TOOL_HEIGHT, mToolContainerHeight);
        args.putString(ARG_CATALOG, catalog);
        fragment.setArguments(args);
        return  fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (ProductListInHotCatalogActivity)getActivity();
        Bundle args = getArguments();
        loadProductHot = (LoadProductListInHotCatalog)args.getSerializable(ARG_LOAD_PRODUCT_HOT);
        mToolContainerHeight = args.getInt(ARG_TOOL_HEIGHT);
        catalog = args.getString(ARG_CATALOG);

        listData = loadProductHot.getProducts();
        isLoading = false;
        isRefresh = false;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_in_catalog, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_product_in_catalog);
        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_product_in_catalog);

        //-------------------------------------------------------------------------------------------------------------
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        SpaceItem space = new SpaceItem(15, SpaceItem.GRID);
        recyclerView.addItemDecoration(space);
        recyclerView.setPadding(recyclerView.getPaddingLeft(), mToolContainerHeight,
                recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());

        //---------------------------------------------------------------------------------------------------------------
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        //----------------------------------------------------------------------------------------------------------------
        recyclerView.addOnScrollListener(new HidingScrollListener(activity, mToolContainerHeight) {
            @Override
            public void onMoved(int distance) {
                activity.viewToolContainer.setTranslationY(-distance);
            }

            @Override
            public void onShow() {
                activity.viewToolContainer.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void onHide() {
                activity.viewToolContainer.animate().translationY(-mToolContainerHeight).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });

        //---------------------------------------------------------------------------------------------------------------
        adapter = new RecyclerAdapterProductListInCatalog(activity, recyclerView, listData);
        adapter.setOnLoadMoreListener(new RecyclerAdapterProductListInCatalog.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(!isLoading) {
                    handleOnLoadMore();
                }
            }
        });

        adapter.setOnItemClickListener(new RecyclerAdapterProductListInCatalog.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                ShortProduct shortProduct = listData.get(position);
                if(shortProduct != null && StatusInternet.isInternet(activity)) {
                    Intent intent = new Intent(activity, ProductDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(ConfigData.ARG_ID_PRODUCT, shortProduct.getIdProduct());
                    bundle.putString(ConfigData.ARG_PRODUCT_NAME, shortProduct.getProductName());
                    intent.putExtra(ConfigData.ARG_PACKAGE, bundle);
                    activity.startActivity(intent);
                }
            }
        });

        recyclerView.setAdapter(adapter);

        //------------------------------------------------------------------------------------------------------------
        // refresh
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isRefresh) {
                    handleOnRefresh();
                }

                refreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    public synchronized void handleOnLoadMore() {

        if(!loadProductHot.getMessage().equals(RequestId.MES_NOTHING)) {
            if(StatusInternet.isInternet(activity)) {
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

                        loadProductHot.loadProductListInHotCatalog(activity,catalog);
                        while(!loadProductHot.isLoadProductListComplete()) {
                            SystemClock.sleep(ConfigData.TIME_SLEEP);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        //remove progressbar
                        listData.remove(listData.size() -1);
                        adapter.notifyItemRemoved(listData.size());

                        int oldSize = listData.size();
                        if(loadProductHot.addLoadMore()) {
                            int newSize = listData.size();

                            for(int i = oldSize; i< newSize; i++) {
                                adapter.notifyItemInserted(i);
                            }
                        }
                        isLoading = false;
                    }
                }.execute();
            }
        }else {
            DisplayNotification.toast(activity, "Nothing");
        }


    }

    public synchronized void handleOnRefresh() {

    }

    public abstract class HidingScrollListener extends  RecyclerView.OnScrollListener {

        public static final int HIDE_THRESHOLD = 10;
        public static final int SHOW_THRESHOLD = 70;

        private int mToolbarOffset = 0;
        private int mToolContainerHeight ;
        private  boolean mControlsVisible = true;
        private int mTotalScrolledDistance;

        Context context;

        public HidingScrollListener(Context context, int mToolContainerHeight) {

            this.context = context;
            this.mToolContainerHeight = mToolContainerHeight;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            clipToolbarOffset();
            onMoved(mToolbarOffset);

            if((mToolbarOffset < mToolContainerHeight && dy > 0) || (mToolbarOffset > 0 && dy < 0)) {
                mToolbarOffset += dy;
            }
            mTotalScrolledDistance += dy;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                if(mTotalScrolledDistance < mToolContainerHeight) {
                    setVisible();
                } else {
                    if (mControlsVisible) {
                        if (mToolbarOffset > HIDE_THRESHOLD) {
                            setInvisible();
                        } else {
                            setVisible();
                        }
                    } else {
                        if ((mToolContainerHeight - mToolbarOffset) > SHOW_THRESHOLD) {
                            setVisible();
                        } else {
                            setInvisible();
                        }
                    }
                }
            }
        }

        private void clipToolbarOffset() {
            if(mToolbarOffset > mToolContainerHeight) {
                mToolbarOffset = mToolContainerHeight;
            } else if(mToolbarOffset < 0) {
                mToolbarOffset = 0;
            }
        }


        public void setVisible() {

            if(mToolbarOffset > 0) {
                onShow();
                mToolbarOffset = 0;
            }

            mControlsVisible = true;
        }

        private void setInvisible() {
            if(mToolbarOffset < mToolContainerHeight) {
                onHide();
                mToolbarOffset = mToolContainerHeight;
            }
            mControlsVisible = false;
        }


        public abstract void onMoved(int distance);
        public abstract void onShow();
        public abstract void onHide();
    }



}
