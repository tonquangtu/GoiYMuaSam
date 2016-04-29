package com.mycompany.mainui.ViewPagerAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.mainui.MainFragment;
import com.mycompany.mainui.PageFragment;
import com.mycompany.mainui.R;
import com.mycompany.mainui.network.LoadHotList;
import com.mycompany.mainui.network.LoadProductListSuggestion;
import com.mycompany.mainui.network.LoadSales;
import com.mycompany.mainui.network.LoadShopListSuggestion;

import java.util.ArrayList;


/**
 * Created by Mr.T on 4/6/2016.
 */
public class PagerAdapterMain extends FragmentStatePagerAdapter{

    final int PAGE_COUNT = 4;
    public static final String[] TAB_TITLE = new String[] { "Trang chủ", "Giao vặt", "Yêu thích", "Thông báo"};
    public static int[] tabIcons_df = {R.drawable.home_df,R.drawable.docu_df,R.drawable.favorite_df,
    R.drawable.megaphone_df};
    public static int[] tabIcons_slc = {R.drawable.home_slc3,R.drawable.docu_slc3,
            R.drawable.favorite_slc3,R.drawable.megaphone_slc3};
    private Context context;
    // Holder for change icon tap when click
    public static ArrayList<TabHolder> TAB_HOLDER = new ArrayList<>(5);

    private LoadProductListSuggestion loadProductListSuggestion;
    private LoadShopListSuggestion loadShopListSuggestion;
    private LoadHotList loadHotList;
    private LoadSales loadSales;



    public PagerAdapterMain(FragmentManager fm, Context context,
                            LoadProductListSuggestion loadProductListSuggestion,
                            LoadShopListSuggestion loadShopListSuggestion,
                            LoadHotList loadHotList,
                            LoadSales loadSales) {
        super(fm);
        this.context = context;
        this.loadProductListSuggestion = loadProductListSuggestion;
        this.loadShopListSuggestion = loadShopListSuggestion;
        this.loadHotList = loadHotList;
        this.loadSales = loadSales;

    }

    public static class TabHolder{

        public ImageView tabIcon;
        public TextView  tabTitle;

        public TabHolder(ImageView tabIcon, TextView tabTitle){
            this.tabIcon = tabIcon;
            this.tabTitle = tabTitle;
        }
    }
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){

            case 0: fragment = MainFragment.newInstance(
                    1,
                    loadProductListSuggestion,
                    loadShopListSuggestion,
                    loadHotList,
                    loadSales
                    );
                break;

            case 1: fragment = PageFragment.newInstance(2); break;
            case 2: fragment = PageFragment.newInstance(3); break;
            case 3: fragment = PageFragment.newInstance(4); break;
            case 4: fragment = PageFragment.newInstance(5); break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return TAB_TITLE[position];
    }
    // return custom tabs
    public View getTabView(int position) {
        View tab;
            tab = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
            ImageView img = (ImageView)tab.findViewById(R.id.image_tab_custom);
            TextView tv = (TextView) tab.findViewById(R.id.title_tab_custom);
            img.setImageResource(tabIcons_df[position]);
            TAB_HOLDER.add(position, new TabHolder(img, tv));
        return tab;
    }
}
