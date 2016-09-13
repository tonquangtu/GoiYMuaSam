package com.mycompany.mainui.actiivity;

import android.app.Activity;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.mycompany.mainui.R;
import com.mycompany.mainui.ViewPagerAdapter.PagerAdapterMain;
import com.mycompany.mainui.fragment.FragmentFavorite;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.NavigationCatalogData;
import com.mycompany.mainui.network.LoadHotList;
import com.mycompany.mainui.network.LoadProductListSuggestion;
import com.mycompany.mainui.network.LoadSales;
import com.mycompany.mainui.network.LoadShopListSuggestion;
import com.mycompany.mainui.networkutil.LoadImageFromUrl;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;


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
    private View toolSearch;
    CircleImageView imageProfile;
    TextView txtProfileName;
    public static int FAVORITE_POS = 2;
    Activity mActivity = this;

    private int weightProfile = 62;
    private int heightProfile = 62;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
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
        // Find the toolbar icon_view1 inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolSearch();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //------------------------------------------------------------------------------------------
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView = (NavigationView)findViewById(R.id.navigation_view_main_activity);
        navigationView.setNavigationItemSelectedListener(this);

        initProfile();



//-------------------------------------------------------------------------------------------------
        // Get the ViewPager and set it's PagerAdapterMain so that it can display items
        // man hinh trang chu, chua nhung thu nhu hot, sales..
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

        // Iterate over all tabs and set the custom icon_view1
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

                if(previous != position) {
                    PagerAdapterMain.TabHolder holder;
                    holder = adapter.TAB_HOLDER.get(position);

                    holder.tabIcon.setImageResource(adapter.tabIcons_slc[position]);
                    holder.tabTitle.setText("");

                    PagerAdapterMain.TabHolder oldHolder;
                    oldHolder = adapter.TAB_HOLDER.get(previous);
                    oldHolder.tabIcon.setImageResource(adapter.tabIcons_df[previous]);
                    oldHolder.tabTitle.setText("");
                    previous = position;
                }

                // loi o day.
                // phai tim hieu lai co che cua adapter nay
                // thu 1. Tai sao cac fragment khac no khong can phai vao ham nay de loadd lai
                //  thu bo doan code nay va di tim nguyen nhan gay nen hien tuong kia.
                // nghi ngo la con 1 ham nao do trong adapter lam nhiem vu load lại nội dung cua fragment
                // xem xet lai ham getItem cua adapter
                if(position == FAVORITE_POS) {

                    FragmentFavorite fragmentFavorite = adapter.getFragmentFavorite();
                    if(fragmentFavorite != null) {
                        fragmentFavorite.loadFavorite();
                    }else {
                        Toast.makeText(mActivity, "null", Toast.LENGTH_SHORT).show();
                    }
                }
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

        if(position == NavigationCatalogData.OTHER_POS) {
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

    public void initToolSearch() {

        toolSearch = toolbar.findViewById(R.id.tool_search_main);
        toolSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchSuggestActivity.class);
                MainActivity.this.startActivity(intent);

            }
        });
    }

    public void handleClickProfile() {

        Intent intent = new Intent(this, AccountActivity.class);
        this.startActivity(intent);
    }

    /**
     * ini profile , contain image and name
     */
    public void initProfile() {

        imageProfile = (CircleImageView) navigationView.findViewById(R.id.profile_image);
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickProfile();
            }
        });
        txtProfileName = (TextView)navigationView.findViewById(R.id.profile_name);
        Profile profile = Profile.getCurrentProfile();

        if(profile != null) {
            String url = profile.getProfilePictureUri(weightProfile, weightProfile).toString();
            LoadImageFromUrl.loadAvatar(url, imageProfile, MainActivity.this);
            txtProfileName.setText(profile.getName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Profile profile = Profile.getCurrentProfile();
        if(profile != null) {
            String url = profile.getProfilePictureUri(weightProfile, heightProfile).toString();
            LoadImageFromUrl.loadAvatar(url, imageProfile, MainActivity.this);
            txtProfileName.setText(profile.getName());
        }else {
            imageProfile.setImageResource(R.drawable.avatar);
            txtProfileName.setText("Vô danh");
        }
    }
}
