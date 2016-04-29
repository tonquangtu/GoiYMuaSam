package com.mycompany.mainui.actiivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mycompany.mainui.Fragment.FragmentProductListInHotCatalog;
import com.mycompany.mainui.Fragment.LoadingFragment;
import com.mycompany.mainui.R;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.network.LoadProductListInHotCatalog;

import java.util.ArrayList;

public class ProductListInHotCatalogActivity extends AppCompatActivity {

    ProductListInHotCatalogActivity mActivity;
    LoadProductListInHotCatalog loadProductListInHotCatalog;
    ArrayList<ShortProduct> listProduct;
    ActionBar actionBar;
    String hotCatalogName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_in_catalog);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ConfigData.ARG_PACKAGE);
        hotCatalogName = bundle.getString(ConfigData.ARG_HOT_CATALOG_NAME);
        listProduct = (ArrayList<ShortProduct>) bundle.getSerializable(ConfigData.ARG_HOT_PRODUCT_LIST);

        mActivity = ProductListInHotCatalogActivity.this;
        loadProductListInHotCatalog = new LoadProductListInHotCatalog(listProduct);

        Toolbar toolbar = (Toolbar)findViewById(R.id.product_list_in_hot_catalog_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(hotCatalogName);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        new LoadTask().execute();


    }

    public class LoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            FragmentManager fm = mActivity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            LoadingFragment loadingFragment = LoadingFragment.newInstance();
            ft.replace(R.id.product_list_in_hot_catalog_place_holder, loadingFragment);
            ft.commit();
        }

        @Override
        protected Void doInBackground(Void... params) {

            loadProductListInHotCatalog.loadProductListInHotCatalog(mActivity, hotCatalogName);
            while(!loadProductListInHotCatalog.isLoadProductListComplete()) {
                SystemClock.sleep(ConfigData.TIME_SLEEP);
            }
            loadProductListInHotCatalog.addLoadMore();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            FragmentManager fm = mActivity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            FragmentProductListInHotCatalog fragmentHot = FragmentProductListInHotCatalog.newInstance(loadProductListInHotCatalog);
            ft.replace(R.id.product_list_in_hot_catalog_place_holder, fragmentHot);
            ft.commit();
        }
    }

}
