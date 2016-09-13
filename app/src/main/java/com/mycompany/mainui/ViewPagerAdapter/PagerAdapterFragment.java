package com.mycompany.mainui.ViewPagerAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mycompany.mainui.fragment.HotProductFragment;
import com.mycompany.mainui.fragment.RecommendFragment;
import com.mycompany.mainui.fragment.SaleFragment;
import com.mycompany.mainui.fragment.ShopNearestFragment;
import com.mycompany.mainui.network.LoadHotList;
import com.mycompany.mainui.network.LoadProductListSuggestion;
import com.mycompany.mainui.network.LoadSales;
import com.mycompany.mainui.network.LoadShopListSuggestion;

/**
 * Created by Mr.T on 4/12/2016.
 */
public class PagerAdapterFragment extends FragmentStatePagerAdapter{
    private static int NUM_ITEMS = 4;
    private String[] title = {"Gợi ý cho bạn", "Cửa hàng gần đây", "Sản phẩm HOT", "Khuyến mại", };
    Context context;
    LoadProductListSuggestion loadProductListSuggestion;
    LoadShopListSuggestion loadShopListSuggestion;
    LoadHotList loadHotList;
    LoadSales loadSales;

    public PagerAdapterFragment(FragmentManager fragmentManager,
                                Context context,
                                LoadProductListSuggestion loadProductListSuggestion,
                                LoadShopListSuggestion loadShopListSuggestion,
                                LoadHotList loadHotList,
                                LoadSales loadSales) {

        super(fragmentManager);
        this.context = context;
        this.loadProductListSuggestion = loadProductListSuggestion;
        this.loadShopListSuggestion = loadShopListSuggestion;
        this.loadHotList = loadHotList;
        this.loadSales = loadSales;

    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return RecommendFragment.newInstance(0, loadProductListSuggestion);
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return ShopNearestFragment.newInstance(1, loadShopListSuggestion);
            case 2: // Fragment # 1 - This will show SecondFragment
                return HotProductFragment.newInstance(2, loadHotList);
            case 3:
                return SaleFragment.newInstance(3, loadSales);
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

}

