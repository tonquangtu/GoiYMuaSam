package com.mycompany.mainui.Fragment;

import android.app.Activity;
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
import com.mycompany.mainui.RecyclerAdapter.RecyclerAdapterHotProduct;
import com.mycompany.mainui.SpaceItemRecyclerView;
import com.mycompany.mainui.actiivity.MainActivity;
import com.mycompany.mainui.controller3.HotListController;
import com.mycompany.mainui.network.LoadHotList;

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
    private int mPage;

    Handler handler;
    ArrayList listData;

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
        mPage = getArguments().getInt(ARG_PAGE);
        loadHotList = (LoadHotList)getArguments().getSerializable(ARG_LOAD_HOT);
        hotListController = new HotListController();
        listData = loadHotList.getHotObject();
        context = getActivity();
        handler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.from(context).inflate(R.layout.fragment_hot_product,container, false);
        //--------------------------------------------------------------------------------------------------

        // find list
        final RecyclerView list_hot_product =
                (RecyclerView) view.findViewById(R.id.recycler_hot_product);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,1);
        list_hot_product.setLayoutManager(layoutManager);

        // set adapter
        final RecyclerAdapterHotProduct adapterHotProduct =
                new RecyclerAdapterHotProduct(context, listData, list_hot_product);
        list_hot_product.setAdapter(adapterHotProduct);
        // add space
        SpaceItemRecyclerView space = new SpaceItemRecyclerView(10, SpaceItemRecyclerView.GRID);
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

            }
        });
        // don't have load more
//       // Load data when endless recycler
//        adapterHotProduct.setOnLoadMoreListener(new RecyclerAdapterHotProduct.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//
//              chua chinh lai load more
//                // add progressBar
//                listData.add(null);
//                adapterHotProduct.notifyItemInserted(listData.size() - 1);
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //remove progress item
//                        listData.remove(listData.size() - 1);
//                        adapterHotProduct.notifyItemRemoved(listData.size());
//
//                        //add items one by one
//                        for (int i = 0; i < 5; i++) {
//                            listData.add(DataProductView.createDataProduct().get(i));
//                            adapterHotProduct.notifyItemInserted(listData.size());
//                        }
//                        adapterHotProduct.setLoaded();
//                        //you can add all at once but
//                        // do not forget to call mAdapter.notifyDataSetChanged();
//                    }
//                }, 2000);
//            }
//        });

        // capture event when user click a item in list
        adapterHotProduct.setOnItemClickListener(new RecyclerAdapterHotProduct.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                hotListController.handle(listData, position);
            }
        });



        // Lookup the swipe container icon_view
        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout)
                view.findViewById(R.id.swipe_refresh_hot_product);

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