package com.mycompany.mainui.actiivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mycompany.mainui.fragment.FragmentNavigationCatalog;
import com.mycompany.mainui.fragment.FragmentNavigationCatalogListDetail;
import com.mycompany.mainui.R;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.NavigationCatalogData;

public class NavigationCatalogActivity extends AppCompatActivity {

    public static final String ARG_POSITION = "ARG_POSITION";
    String [] listCatalogDetail;
    FragmentNavigationCatalog fnc;
    FragmentNavigationCatalogListDetail fd;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_navigation_catalog);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ConfigData.ARG_PACKAGE);
        int postionCatalog = bundle.getInt(ARG_POSITION);
        setTitle(postionCatalog);
        listCatalogDetail = getListCatalogDetail(postionCatalog);

        // create fragment
        initFragment(postionCatalog);


    }

    public void initFragment(int postionCatalog) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        fnc = FragmentNavigationCatalog.newInstance(postionCatalog);
        fd = FragmentNavigationCatalogListDetail.newInstance(listCatalogDetail, postionCatalog);

        ft.replace(R.id.navigation_catalog_place_holder, fnc);
        ft.replace(R.id.navigation_catalog_list_detail_place_holder, fd);

        ft.commit();

    }

    public String [] getListCatalogDetail(int position) {

        String [] listCatalogDetail;
        switch (position) {

            case NavigationCatalogData.SEARCH_POS :
                listCatalogDetail = NavigationCatalogData.SEARCH_CATALOG;
                break;
            case NavigationCatalogData.CITY_SET_POS :
                listCatalogDetail = NavigationCatalogData.CITY_SET;
                break;
            case NavigationCatalogData.SHOP_CATALOGS_POS :
                listCatalogDetail = NavigationCatalogData.SHOP_CATALOGS;
                break;
            case NavigationCatalogData.TRADE_MARK_POS :
                listCatalogDetail = NavigationCatalogData.TRADE_MARK;
                break;
            case NavigationCatalogData.MEN_FASHION_POS :
                listCatalogDetail = NavigationCatalogData.MEN_FASHION;
                break;
            case NavigationCatalogData.GIRL_FASHION_POS :
                listCatalogDetail = NavigationCatalogData.GIRL_FASHION;
                break;
            case NavigationCatalogData.SHOES_POS :
                listCatalogDetail = NavigationCatalogData.SHOES;
                break;
            case NavigationCatalogData.BAG_POS :
                listCatalogDetail = NavigationCatalogData.BAG;
                break;
            case NavigationCatalogData.WATCH_POS :
                listCatalogDetail = NavigationCatalogData.WATCH;
                break;
            case NavigationCatalogData.MOTHER_BABY_POS :
                listCatalogDetail = NavigationCatalogData.MOTHER_BABY;
                break;
            case NavigationCatalogData.ELECTRONICS_POS :
                listCatalogDetail = NavigationCatalogData.ELECTRONICS;
                break;
            case NavigationCatalogData.ACCESSORY_POS :
                listCatalogDetail = NavigationCatalogData.ACCESSORY;
                break;

            default :
                listCatalogDetail = NavigationCatalogData.SPORT;
                break;
        }

        return  listCatalogDetail;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home :
                finish();
                break;
        }
        return true;
    }

    public void handleMesFromNavigationFragment(int postion) {

        if(postion != NavigationCatalogData.OTHER_POS) {
            listCatalogDetail = getListCatalogDetail(postion);
            fd.updateData(listCatalogDetail, postion);
            setTitle(postion);
        }else {

        }

    }

    public void setTitle(int position) {

        String title = NavigationCatalogData.CATALOGS[position];
        actionBar.setTitle(title);
    }

}
