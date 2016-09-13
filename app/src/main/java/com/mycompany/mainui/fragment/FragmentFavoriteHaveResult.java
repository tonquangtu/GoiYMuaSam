package com.mycompany.mainui.fragment;

import android.app.Activity;
import android.os.AsyncTask;
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
import com.mycompany.mainui.SpaceItem;
import com.mycompany.mainui.actiivity.MainActivity;
import com.mycompany.mainui.adapter.RecyclerAdapterSale;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.InfoAccount;
import com.mycompany.mainui.model.LoadFavorite;
import com.mycompany.mainui.networkutil.StatusInternet;
import com.mycompany.mainui.view.DisplayNotification;

import java.util.ArrayList;

/**
 * Created by Dell on 19-May-16.
 */
public class FragmentFavoriteHaveResult extends Fragment{


    public static final String ARG_LOAD_SALES = "ARG_LOAD_FAVORITE";
    public Activity context;
    RecyclerAdapterSale adapterSale;
    SwipeRefreshLayout swipeContainer;
    ArrayList listData;
    boolean isRefresh;
    private LoadFavorite loadFavorite;


    public static FragmentFavoriteHaveResult newInstance( LoadFavorite loadFavorite) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_LOAD_SALES, loadFavorite);
        FragmentFavoriteHaveResult fragment = new FragmentFavoriteHaveResult();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        loadFavorite = (LoadFavorite)getArguments().getSerializable(ARG_LOAD_SALES);
        listData = loadFavorite.getListData();
        context = getActivity();
        isRefresh = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.from(context).inflate(R.layout.fragment_sale,container, false);
        //--------------------------------------------------------------------------------------------------

        // find list
        final RecyclerView list_sale =
                (RecyclerView) view.findViewById(R.id.recycler_sale);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        list_sale.setLayoutManager(layoutManager);

        // set adapter
        adapterSale =
                new RecyclerAdapterSale(context, listData, list_sale);
        list_sale.setAdapter(adapterSale);
        // add space
        SpaceItem space = new SpaceItem(15, SpaceItem.GRID);
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

        adapterSale.setOnItemClickListener(new RecyclerAdapterSale.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {


            }
        });

        // Lookup the swipe container icon_view1
        swipeContainer = (SwipeRefreshLayout)
                view.findViewById(R.id.swipe_refresh_sale);

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

    public void handleRefresh() {

       if(InfoAccount.isLogin(context)) {
           if(StatusInternet.isInternet(context)) {
               if(listData == null || listData.size() == 0) {
                   isRefresh = true;
                   new AsyncTask<Void, Void, Void>() {
                       @Override
                       protected Void doInBackground(Void... params) {

                           loadFavorite.loadFavorite(context);
                           while(!loadFavorite.isLoadComplete()) {
                               try {
                                   Thread.sleep(ConfigData.TIME_SLEEP);
                               } catch (InterruptedException e) {
                                   e.printStackTrace();
                               }
                           }
                           return null;
                       }

                       @Override
                       protected void onPostExecute(Void aVoid) {
                           super.onPostExecute(aVoid);

                           if(loadFavorite.addFavorite()) {
                               adapterSale.notifyDataSetChanged();
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
       }else {
           DisplayNotification.toast(context, "Bạn cần đăng nhập để thực hiện chức năng này");
       }
    }

}
