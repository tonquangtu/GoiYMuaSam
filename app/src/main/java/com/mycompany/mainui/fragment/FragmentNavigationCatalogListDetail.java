package com.mycompany.mainui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mycompany.mainui.R;
import com.mycompany.mainui.adapter.NavigationCatalogListDetailAdapter;
import com.mycompany.mainui.actiivity.NavigationCatalogActivity;
import com.mycompany.mainui.actiivity.ProductListInCatalogActivity;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.NavigationCatalogData;
import com.mycompany.mainui.networkutil.StatusInternet;

import java.util.ArrayList;

/**
 * Created by Dell on 4/28/2016.
 */
public class FragmentNavigationCatalogListDetail extends Fragment {

    public static final String ARG_DATA = "ARG_DATA";
    public static final String ARG_POS_CATALOG = "ARG_POS_CATALOG";
    NavigationCatalogActivity mActivity;
    ArrayList<String> listData;
    ListView listView;
    NavigationCatalogListDetailAdapter adapter;
    int posCatalog;

    public static FragmentNavigationCatalogListDetail newInstance(String [] arrData, int initPosCatalog) {
        FragmentNavigationCatalogListDetail fragment = new FragmentNavigationCatalogListDetail();
        Bundle args = new Bundle();
        ArrayList<String> lisData = new ArrayList<>();
        for(int i = 0; i < arrData.length; i++) {
            lisData.add(arrData[i]);
        }
        args.putSerializable(ARG_DATA, lisData);
        args.putInt(ARG_POS_CATALOG, initPosCatalog);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listData = (ArrayList<String>) getArguments().getSerializable(ARG_DATA);
        this.posCatalog = getArguments().getInt(ARG_POS_CATALOG);
        mActivity = (NavigationCatalogActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navigation_catalog_list_detail,
                container, false);
        listView = (ListView)view.findViewById(R.id.navigation_catalog_list_detail_list_view);
        adapter = new NavigationCatalogListDetailAdapter(mActivity, R.layout.navigation_catalog_list_detail_row, listData);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleOnItemClick(position);
            }
        });

        return view;
    }

    public void handleOnItemClick(int position) {

        if(StatusInternet.isInternet(mActivity)) {
            Intent intent;
            Bundle bundle;
            if(posCatalog == NavigationCatalogData.SEARCH_POS) {

                // do search in here
                Toast.makeText(mActivity, "posCatalog : " +  posCatalog + "catalog : " +
                        listData.get(position), Toast.LENGTH_SHORT).show();
            }else if(posCatalog == NavigationCatalogData.CITY_SET_POS) {

                // do choose city
                Toast.makeText(mActivity, "posCatalog : " +  posCatalog + "catalog : " +
                        listData.get(position), Toast.LENGTH_SHORT).show();
            }else if(posCatalog == NavigationCatalogData.SHOP_CATALOGS_POS) {
                Toast.makeText(mActivity, "posCatalog : " +  posCatalog + "catalog : " +
                        listData.get(position), Toast.LENGTH_SHORT).show();
            }
            else {

                intent = new Intent(mActivity, ProductListInCatalogActivity.class);
                bundle = new Bundle();
                bundle.putString(ConfigData.ARG_PRODUCT_CATALOG, listData.get(position));
                intent.putExtra(ConfigData.ARG_PACKAGE, bundle);
                mActivity.startActivity(intent);

            }
        }

    }

    public void updateData(String [] arrData, int posCatalog) {

        listData.removeAll(listData);
        for(int i = 0; i < arrData.length; i++) {
            listData.add(arrData[i]);
        }
        adapter.notifyDataSetChanged();
        this.posCatalog = posCatalog;
    }
}
