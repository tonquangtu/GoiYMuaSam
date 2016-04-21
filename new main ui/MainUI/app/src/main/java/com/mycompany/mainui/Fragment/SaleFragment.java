package com.mycompany.mainui.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycompany.mainui.R;
import com.mycompany.mainui.RecyclerAdapter.RecyclerAdapterSale;
import com.mycompany.mainui.SpaceItemRecyclerView;
import com.mycompany.mainui.actiivity.MainActivity;
import com.mycompany.mainui.controller3.SalesController;
import com.mycompany.mainui.network.LoadSales;

import java.util.ArrayList;

/**
 * Created by Mr.T on 4/16/2016.
 */
public class SaleFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_LOAD_SALES = "ARG_LOAD_SALES";
    public Context context;
    private int mPage;

    Handler handler;
    ArrayList<Object> listData;

    private LoadSales loadSales;
    private SalesController salesController;


    public static SaleFragment newInstance(int page, LoadSales loadSales) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(ARG_LOAD_SALES, loadSales);

        SaleFragment fragment = new SaleFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);

        loadSales = (LoadSales)getArguments().getSerializable(ARG_LOAD_SALES);
        salesController = new SalesController();

        if(loadSales.getSalesList() != null) {
            listData = loadSales.getSalesList();
        }else {
            listData = new ArrayList<>();
        }

        context = getActivity();
        handler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.from(context).inflate(R.layout.fragment_sale,container, false);
        //--------------------------------------------------------------------------------------------------

        // find list
        final RecyclerView list_sale =
                (RecyclerView) view.findViewById(R.id.recycler_sale);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,1);
        list_sale.setLayoutManager(layoutManager);

        // set adapter
        final RecyclerAdapterSale adapterSale =
                new RecyclerAdapterSale(context, listData, list_sale);
        list_sale.setAdapter(adapterSale);
        // add space
        SpaceItemRecyclerView space = new SpaceItemRecyclerView(10, SpaceItemRecyclerView.GRID);
        list_sale.addItemDecoration(space);


        // Hide TabLayout
        list_sale.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        adapterSale.setOnLoadMoreListener(new RecyclerAdapterSale.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });

        // Load data when endless recycler
//        adapterSale.setOnLoadMoreListener(new RecyclerAdapterSale.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                // add progressBar
//                listData.add(null);
//                adapterSale.notifyItemInserted(listData.size() - 1);
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //remove progress item
//                        listData.remove(listData.size() - 1);
//                        adapterSale.notifyItemRemoved(listData.size());
//
//                        //add items one by one
//                        for (int i = 0; i < 5; i++) {
//                            listData.add(dataProduct.get(i));
//                            adapterSale.notifyItemInserted(listData.size());
//                        }
//                        adapterSale.setLoaded();
//                        //you can add all at once but
//                        // do not forget to call mAdapter.notifyDataSetChanged();
//                    }
//                }, 2000);
//            }
//        });

        adapterSale.setOnItemClickListener(new RecyclerAdapterSale.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                salesController.handleItemOnClick(listData, position);

            }
        });

        // Lookup the swipe container icon_view
        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout)
                view.findViewById(R.id.swipe_refresh_sale);

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
}