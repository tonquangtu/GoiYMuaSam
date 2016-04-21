package com.mycompany.mainui.actiivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mycompany.mainui.PageFragment;
import com.mycompany.mainui.R;
import com.mycompany.mainui.ViewPagerAdapter.PagerAdapterMain;
import com.mycompany.mainui.network.LoadHotList;
import com.mycompany.mainui.network.LoadProductListSuggestion;
import com.mycompany.mainui.network.LoadSales;
import com.mycompany.mainui.network.LoadShopListSuggestion;


public class MainActivity extends AppCompatActivity {

    public static TabLayout tabs;
    ViewPager pager;
    Toolbar toolbar;
    DrawerLayout mDrawer;
    ActionBarDrawerToggle drawerToggle;
    private LoadSales loadSales;
    private LoadHotList loadHotList;
    private LoadShopListSuggestion loadShopListSuggestion;
    private LoadProductListSuggestion loadProductListSuggestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


//-------------------------------------------------------------------------------------------------
private ActionBarDrawerToggle setupDrawerToggle() {
    return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
}
private void setupDrawerContent(NavigationView navigationView) {
    navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    selectDrawerItem(menuItem);
                    return true;
                }
            });
}
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = PageFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = PageFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = PageFragment.class;
                break;
            default:
                fragmentClass = PageFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }






    // Menu icons are inflated just as they were with actionbar
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

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }



}
