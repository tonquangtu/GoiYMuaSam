package com.mycompany.mainui.Fragment;

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
import com.mycompany.mainui.RecyclerAdapter.NavigationCatalogListDetailAdapter;
import com.mycompany.mainui.actiivity.NavigationCatalogActivity;

import java.util.ArrayList;

/**
 * Created by Dell on 4/28/2016.
 */
public class FragmentNavigationCatalogListDetail extends Fragment {

    public static final String ARG = "ARG";
    NavigationCatalogActivity mActivity;
    ArrayList<String> listData;
    ListView listView;
    NavigationCatalogListDetailAdapter adapter;
    int currentPos;

    public static FragmentNavigationCatalogListDetail newInstance(String [] arrData) {
        FragmentNavigationCatalogListDetail fragment = new FragmentNavigationCatalogListDetail();
        Bundle args = new Bundle();
        ArrayList<String> lisData = new ArrayList<>();
        for(int i = 0; i < arrData.length; i++) {
            lisData.add(arrData[i]);
        }
        args.putSerializable(ARG, lisData);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listData = (ArrayList<String>) getArguments().getSerializable(ARG);
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

        Toast.makeText(mActivity, "pos : " + position + ", " + listData.get(position), Toast.LENGTH_SHORT).show();
    }

    public void updateData(String [] arrData) {

        listData.removeAll(listData);
        if(arrData != null) {
            for(int i = 0; i < arrData.length; i++) {
                listData.add(arrData[i]);
            }
        }

        adapter.notifyDataSetChanged();
    }
}
