package com.mycompany.mainui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycompany.mainui.ViewPagerAdapter.PagerAdapterFragment;
import com.mycompany.mainui.actiivity.SplashActivity;
import com.mycompany.mainui.network.LoadHotList;
import com.mycompany.mainui.network.LoadProductListSuggestion;
import com.mycompany.mainui.network.LoadSales;
import com.mycompany.mainui.network.LoadShopListSuggestion;

/**
 * Created by Mr.T on 4/9/2016.
 */
public class MainFragment extends Fragment {

    // attr of Page
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_LOAD_SALES = "ARG_LOAD_SALES";
    public static final String ARG_LOAD_HOT_LIST = "ARG_LOAD_HOT_LIST";
    public static final String ARG_LOAD_PRODUCT_SUGGESTION = "ARG_LOAD_PRODUCT_SUGGESTION";
    public static final String ARG_LOAD_SHOP_SUGGESTION = "ARG_LOAD_SHOP_SUGGESTION";
    public static final String ARG_PACKAGE = "ARG_PACKAGE";
    private LoadProductListSuggestion loadProductListSuggestion;
    private LoadShopListSuggestion loadShopListSuggestion;
    private LoadHotList loadHotList;
    private LoadSales loadSales;

    private int mPage;
    Context context;

    PagerAdapterFragment pagerAdapterFragment;


    public static MainFragment newInstance(
            int page,
            LoadProductListSuggestion loadProductListSuggestion,
            LoadShopListSuggestion loadShopListSuggestion,
            LoadHotList loadHotList,
            LoadSales loadSales
            ) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(ARG_LOAD_PRODUCT_SUGGESTION, loadProductListSuggestion);
        args.putSerializable(ARG_LOAD_SHOP_SUGGESTION, loadShopListSuggestion);
        args.putSerializable(ARG_LOAD_HOT_LIST, loadHotList);
        args.putSerializable(ARG_LOAD_SALES, loadSales);



        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mPage = args.getInt(ARG_PAGE);

        loadProductListSuggestion = (LoadProductListSuggestion)args.getSerializable(SplashActivity.ARG_LOAD_PRODUCT_SUGGESTION);
        loadShopListSuggestion = (LoadShopListSuggestion)args.getSerializable(SplashActivity.ARG_LOAD_SHOP_SUGGESTION);
        loadHotList = (LoadHotList)args.getSerializable(SplashActivity.ARG_LOAD_HOT_LIST);
        loadSales = (LoadSales)args.getSerializable(SplashActivity.ARG_LOAD_SALES);

        context = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate layout of fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        // set ViewPager
        ViewPager subPager = (ViewPager) view.findViewById(R.id.vpPager);
        pagerAdapterFragment = new PagerAdapterFragment(getChildFragmentManager(),
                context,
                loadProductListSuggestion,
                loadShopListSuggestion,
                loadHotList,
                loadSales);

        subPager.setAdapter(pagerAdapterFragment);
        // set TabLayout
        TabLayout tabs = (TabLayout)view.findViewById(R.id.tab_layout_fragment);
        tabs.setupWithViewPager(subPager);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
