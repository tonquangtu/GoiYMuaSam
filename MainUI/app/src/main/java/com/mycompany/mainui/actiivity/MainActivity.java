package com.mycompany.mainui.actiivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mycompany.mainui.R;
import com.mycompany.mainui.ViewPagerAdapter.PagerAdapterMain;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.NavigationCatalogData;
import com.mycompany.mainui.network.LoadHotList;
import com.mycompany.mainui.network.LoadProductListSuggestion;
import com.mycompany.mainui.network.LoadSales;
import com.mycompany.mainui.network.LoadShopListSuggestion;

import java.io.File;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public static TabLayout tabs;
    ViewPager pager;
    Toolbar toolbar;
    DrawerLayout mDrawer;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    private LoadSales loadSales;
    private LoadHotList loadHotList;
    private LoadShopListSuggestion loadShopListSuggestion;
    private LoadProductListSuggestion loadProductListSuggestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deleteDirectoryTree(getCacheDir());
        //get data from
//-------------------------------------------------------------------------------------------------
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(SplashActivity.ARG_PACKAGE);
        loadProductListSuggestion = (LoadProductListSuggestion)bundle.getSerializable(SplashActivity.ARG_LOAD_PRODUCT_SUGGESTION);
        loadShopListSuggestion = (LoadShopListSuggestion)bundle.getSerializable(SplashActivity.ARG_LOAD_SHOP_SUGGESTION);
        loadHotList = (LoadHotList)bundle.getSerializable(SplashActivity.ARG_LOAD_HOT_LIST);
        loadSales = (LoadSales)bundle.getSerializable(SplashActivity.ARG_LOAD_SALES);

//-------------------------------------------------------------------------------------------------
        // Find the toolbar icon_view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        // Disable title toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.appBar);
        // appBarLayout.setExpanded(false);

        // Find our drawer icon_view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView = (NavigationView)findViewById(R.id.navigation_view_main_activity);
        navigationView.setNavigationItemSelectedListener(this);


//-------------------------------------------------------------------------------------------------
        // Get the ViewPager and set it's PagerAdapterMain so that it can display items
        pager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapterMain adapter = new PagerAdapterMain(getSupportFragmentManager(),
                this,
                loadProductListSuggestion,
                loadShopListSuggestion,
                loadHotList,
                loadSales);

        pager.setAdapter(adapter);


        // Give the TabLayout the ViewPager
        tabs = (TabLayout) findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(pager);

        // Iterate over all tabs and set the custom icon_view
        for (int i = 0; i < tabs.getTabCount(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int previous = 0;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                PagerAdapterMain.TabHolder holder;
                holder = adapter.TAB_HOLDER.get(position);
                // make image_height up
                ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(
                        holder.tabIcon.getLayoutParams());
                marginParams.setMargins(4, 4, 4, 4);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
                // change image and set title
                holder.tabIcon.setLayoutParams(layoutParams);
                holder.tabIcon.setImageResource(adapter.tabIcons_slc[position]);
                holder.tabTitle.setText(adapter.TAB_TITLE[position]);

                if(previous != position){
                    holder = adapter.TAB_HOLDER.get(previous);
                    // make height_image down
                    RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(marginParams);
                    layoutParams2.addRule(RelativeLayout.CENTER_IN_PARENT);
                    holder.tabIcon.setLayoutParams(layoutParams2);
                    // change image and delete title
                    holder.tabIcon.setImageResource(adapter.tabIcons_df[previous]);
                    holder.tabTitle.setText("");
                }
                previous = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static void deleteDirectoryTree(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteDirectoryTree(child);
            }
        }

        fileOrDirectory.delete();
    }
//-------------------------------------------------------------------------------------------------
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(
                this,
                mDrawer,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
    }


//--------------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        if(mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        int position = -1;
        switch (id) {

            case R.id.nav_menu_main_search :
                position = NavigationCatalogData.SEARCH_POS;
                break;
            case R.id.nav_menu_main_choose_city :
                position = NavigationCatalogData.CITY_SET_POS;
                break;
            case R.id.nav_menu_main_shop :
                position = NavigationCatalogData.SHOP_CATALOGS_POS;
                break;
            case R.id.nav_menu_main_trade_mark :
                position = NavigationCatalogData.TRADE_MARK_POS;
                break;
            case R.id.nav_menu_main_men_fashion :
                position = NavigationCatalogData.MEN_FASHION_POS;
                break;
            case R.id.nav_menu_main_girl_fashion :
                position = NavigationCatalogData.GIRL_FASHION_POS;
                break;
            case R.id.nav_menu_main_shoes :
                position = NavigationCatalogData.SHOES_POS;
                break;
            case R.id.nav_menu_main_bag :
                position = NavigationCatalogData.BAG_POS;
                break;
            case R.id.nav_menu_main_watch :
                position = NavigationCatalogData.WATCH_POS;
                break;
            case R.id.nav_menu_main_mother_and_baby :
                position = NavigationCatalogData.MOTHER_BABY_POS;
                break;
            case R.id.nav_menu_main_electronics :
                position = NavigationCatalogData.ELECTRONICS_POS;
                break;
            case R.id.nav_menu_main_accessory :
                position = NavigationCatalogData.ACCESSORY_POS;
                break;

            case R.id.nav_menu_main_sport :
                position = NavigationCatalogData.SPORT_POS;
                break;
            case R.id.nav_menu_main_other :
                position = NavigationCatalogData.OTHER_POS;
                break;

        }

        if(position == NavigationCatalogData.SEARCH_POS) {
            //search
            mDrawer.closeDrawer(GravityCompat.START);
        }else if(position == NavigationCatalogData.OTHER_POS) {
            // do nothing
            mDrawer.closeDrawer(GravityCompat.START);
        }else {
            Intent intent = new Intent(this, NavigationCatalogActivity.class);
            Bundle bundle  = new Bundle();
            bundle.putInt(NavigationCatalogActivity.ARG_POSITION, position);
            intent.putExtra(ConfigData.ARG_PACKAGE, bundle);
            startActivity(intent);
        }

        return true;
    }
}
