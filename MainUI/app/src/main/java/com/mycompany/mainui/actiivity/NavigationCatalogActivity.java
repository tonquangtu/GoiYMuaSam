package com.mycompany.mainui.actiivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mycompany.mainui.Fragment.FragmentNavigationCatalog;
import com.mycompany.mainui.Fragment.FragmentNavigationCatalogListDetail;
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
        int postion = bundle.getInt(ARG_POSITION);
        setTitle(postion);
        listCatalogDetail = getListCatalogDetail(postion);

        // create fragment
        initFragment(postion);


    }

    public void initFragment(int position) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        fnc = FragmentNavigationCatalog.newInstance(position);
        fd = FragmentNavigationCatalogListDetail.newInstance(listCatalogDetail);

        ft.replace(R.id.navigation_catalog_place_holder, fnc);
        ft.replace(R.id.navigation_catalog_list_detail_place_holder, fd);

        ft.commit();

    }

    public String [] getListCatalogDetail(int position) {

        String [] listCatalogDetail;
        switch (position) {

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
            case NavigationCatalogData.SPORT_POS :
                listCatalogDetail = NavigationCatalogData.SPORT;
                break;
            default:
                listCatalogDetail = null;
                break;

        }

        return  listCatalogDetail;
    }


    public void handleMesFromNavigationFragment(int postion) {

        // search
        if(postion == NavigationCatalogData.SEARCH_POS ||
                postion == NavigationCatalogData.OTHER_POS ) {

            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            finish();
        }else  {
            listCatalogDetail = getListCatalogDetail(postion);
            fd.updateData(listCatalogDetail);
        }
        setTitle(postion);
    }

    public void setTitle(int position) {

        String title = NavigationCatalogData.CATALOGS[position];
        actionBar.setTitle(title);
    }

}
