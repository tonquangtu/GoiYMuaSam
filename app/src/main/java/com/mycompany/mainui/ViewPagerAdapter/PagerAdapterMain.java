package com.mycompany.mainui.ViewPagerAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mycompany.mainui.MainFragment;
import com.mycompany.mainui.PageFragment;
import com.mycompany.mainui.R;
import com.mycompany.mainui.fragment.FragmentFavorite;
import com.mycompany.mainui.fragment.FragmentOldProductCatalog;
import com.mycompany.mainui.network.LoadHotList;
import com.mycompany.mainui.network.LoadProductListSuggestion;
import com.mycompany.mainui.network.LoadSales;
import com.mycompany.mainui.network.LoadShopListSuggestion;

import java.util.ArrayList;


/**
 * Created by Mr.T on 4/6/2016.
 */
public class PagerAdapterMain extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;
    public static final String[] TAB_TITLE = new String[] { "Trang chủ", "Giao vặt", "Yêu thích", "Thông báo"};
    public static int[] tabIcons_slc = {R.drawable.icon_home,R.drawable.icon_old_product,R.drawable.icon_favorite,
    R.drawable.icon_notification};
    public static int[] tabIcons_df = {R.drawable.icon_home_1,R.drawable.icon_old_product_1,
            R.drawable.icon_favorite_1,R.drawable.icon_notification_1};
    private Context context;
    // Holder for change icon tap when click
    public static ArrayList<TabHolder> TAB_HOLDER = new ArrayList<>(5);

    private LoadProductListSuggestion loadProductListSuggestion;
    private LoadShopListSuggestion loadShopListSuggestion;
    private LoadHotList loadHotList;
    private LoadSales loadSales;
    FragmentFavorite fragmentFavorite;



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
        Toast.makeText(context,"Position : " + position, Toast.LENGTH_SHORT).show();
        switch (position){

            case 0: fragment = MainFragment.newInstance(
                    1,
                    loadProductListSuggestion,
                    loadShopListSuggestion,
                    loadHotList,
                    loadSales
                    );
                break;

            case 1:
                fragment = FragmentOldProductCatalog.newInstance(); break;

            case 2:
                fragmentFavorite = FragmentFavorite.newInstance();
                fragment = fragmentFavorite;
                break;

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

    public FragmentFavorite getFragmentFavorite() {
        return fragmentFavorite;
    }


}
