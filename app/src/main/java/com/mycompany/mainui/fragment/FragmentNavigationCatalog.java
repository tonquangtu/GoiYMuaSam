package com.mycompany.mainui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mycompany.mainui.R;
import com.mycompany.mainui.adapter.NavigationCatalogAdapter;
import com.mycompany.mainui.actiivity.NavigationCatalogActivity;
import com.mycompany.mainui.model.NavigationCatalogData;

/**
 * Created by Dell on 4/27/2016.
 */
public class FragmentNavigationCatalog extends Fragment {

    public int initPos;
    public static final String ARG = "ARG";
    NavigationCatalogActivity mActivity;
    ListView listView;
    NavigationCatalogAdapter adapter;
    public int currentPos ;
    public int oldPos = -1;

    public static FragmentNavigationCatalog newInstance(int selectedPos) {

        FragmentNavigationCatalog fragment = new FragmentNavigationCatalog();
        Bundle args = new Bundle();
        args.putInt(ARG,selectedPos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initPos = getArguments().getInt(ARG);
        mActivity = (NavigationCatalogActivity)getActivity();
        currentPos = initPos;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_navigation_catalog, container, false);
        listView = (ListView)view.findViewById(R.id.navigation_catalog_list_view);

        adapter = new NavigationCatalogAdapter(mActivity, this,
                R.layout.navigation_catalog_row, NavigationCatalogData.ICON_CATALOG_NO_OTHER_CHANGE);
        listView.setAdapter(adapter);
        listView.setSelection(initPos);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentPos != position) {
                    oldPos = currentPos;
                    mActivity.handleMesFromNavigationFragment(position);
                    adapter.notifyDataSetChanged();
//                    view.setBackgroundColor(mActivity.getResources().getColor(R.color.blue_200));
                    currentPos = position;

                }
            }
        });
        return view;
    }


}
