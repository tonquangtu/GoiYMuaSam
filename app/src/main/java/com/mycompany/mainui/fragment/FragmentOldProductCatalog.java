package com.mycompany.mainui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycompany.mainui.R;
import com.mycompany.mainui.SpaceItemRecyclerView;
import com.mycompany.mainui.actiivity.MainActivity;
import com.mycompany.mainui.adapter.AdapterOldProduct;
import com.mycompany.mainui.adapter.Banner;

import java.util.ArrayList;

/**
 * Created by Dell on 18-May-16.
 */
public class FragmentOldProductCatalog extends Fragment {



    private ArrayList<Banner> listData;
    Activity activity;
    private int[] image = {R.drawable.tatca, R.drawable.xeco, R.drawable.batdongsan,
            R.drawable.thoitrang, R.drawable.noithat, R.drawable.thethao,R.drawable.dientu,
            R.drawable.vanphong, R.drawable.dichvu, R.drawable.khac};
    private String[] title = {"TẤT CẢ", "XE CỘ", "BẤT ĐỘNG SẢN", "THỜI TRANG",
            "NỘI THẤT", "THỂ THAO","ĐỒ ĐIỆN TỬ",
            "VĂN PHÒNG", "DỊCH VỤ", "KHÁC"};

    public static FragmentOldProductCatalog newInstance() {

        FragmentOldProductCatalog fragment = new FragmentOldProductCatalog();
        return fragment;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        listData = Banner.createBanner(title, image);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_old_product, container, false);

        RecyclerView list = (RecyclerView) view.findViewById(R.id.list);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        if(fab != null){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivity(intent);
                }
            });
        }

        // add layout manager
        GridLayoutManager layoutManager = new GridLayoutManager(activity,2);
        list.setLayoutManager(layoutManager);

        // add space
        SpaceItemRecyclerView space =
                new SpaceItemRecyclerView(10, SpaceItemRecyclerView.GRID, listData.size());
        list.addItemDecoration(space);
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        // add adapter
        AdapterOldProduct adapter = new AdapterOldProduct(activity, listData);
        list.setAdapter(adapter);
        return view;
    }
}
