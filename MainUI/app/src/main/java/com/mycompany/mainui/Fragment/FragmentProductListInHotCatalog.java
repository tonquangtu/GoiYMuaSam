package com.mycompany.mainui.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycompany.mainui.R;
import com.mycompany.mainui.RecyclerAdapter.RecyclerAdapterProductListInCatalog;
import com.mycompany.mainui.SpaceItemRecyclerView;
import com.mycompany.mainui.actiivity.MainActivity;
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.network.LoadProductListInHotCatalog;

import java.util.ArrayList;

/**
 * Created by Dell on 4/28/2016.
 */
public class FragmentProductListInHotCatalog extends Fragment {

    Activity activity;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    LoadProductListInHotCatalog loadProductHot;
    public static final String ARG_LOAD_PRODUCT_HOT = "LOAD_PRODUCT_HOT";
    ArrayList<ShortProduct> listData;
    RecyclerAdapterProductListInCatalog adapter;


    public static FragmentProductListInHotCatalog newInstance(LoadProductListInHotCatalog loadProductHot) {

        FragmentProductListInHotCatalog fragment = new FragmentProductListInHotCatalog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOAD_PRODUCT_HOT, loadProductHot);
        fragment.setArguments(args);
        return  fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        loadProductHot = (LoadProductListInHotCatalog)getArguments().getSerializable(ARG_LOAD_PRODUCT_HOT);
        listData = loadProductHot.getProducts();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_recommend);
        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_shop_nearest);

        //-------------------------------------------------------------------------------------------------------------
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        SpaceItemRecyclerView space = new SpaceItemRecyclerView(15, SpaceItemRecyclerView.GRID);
        recyclerView.addItemDecoration(space);

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
        // init adapter
        View.OnClickListener filterPriceListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnClickFilterPrice();
            }
        };

        View.OnClickListener filterSalesListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handleOnClickFilterSales();
            }
        };

        adapter = new RecyclerAdapterProductListInCatalog(activity, recyclerView,
                filterPriceListener, filterSalesListener, listData);
        adapter.setOnLoadMoreListener(new RecyclerAdapterProductListInCatalog.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                handleOnLoadMore();
            }
        });

        recyclerView.setAdapter(adapter);

        //------------------------------------------------------------------------------------------------------------
        // refresh
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handleOnRefresh();
                refreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    public void handleOnClickFilterPrice() {

    }

    public void handleOnClickFilterSales() {

    }

    public void handleOnLoadMore() {

    }

    public void handleOnRefresh() {

    }


}
