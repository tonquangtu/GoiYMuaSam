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

import com.mycompany.mainui.Fragment.LoadingFragment;
import com.mycompany.mainui.Fragment.ProductListInAdFragment;
import com.mycompany.mainui.R;
import com.mycompany.mainui.model.AdProductCatalog;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.network.LoadProductListInAd;

public class ProductListInAdActivity extends AppCompatActivity {

    AdProductCatalog adProductCatalog;
    ProductListInAdActivity mActivity;
    LoadProductListInAd loadProductListInAd;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_in_ad);


        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ConfigData.ARG_PACKAGE_AD_CATALOG);
        adProductCatalog = (AdProductCatalog)bundle.getSerializable(ConfigData.ARG_AD_CATALOG);
        mActivity = ProductListInAdActivity.this;
        loadProductListInAd = new LoadProductListInAd();
        toolbar = (Toolbar)findViewById(R.id.product_list_in_ad_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(adProductCatalog.getAdProductCatalogName());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);



        new LoadingTask().execute();

    }

    private class LoadingTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            FragmentManager fm = mActivity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            LoadingFragment loadingFragment = LoadingFragment.newInstance();
            ft.replace(R.id.product_list_in_ad_place_holder, loadingFragment);
            ft.commit();

        }

        @Override
        protected Void doInBackground(Void... params) {

            loadProductListInAd.loadProductListInAd(mActivity, adProductCatalog.getAdProductCatalogName());
            while(!loadProductListInAd.isLoadComplete()) {
                SystemClock.sleep(10);
            }
            loadProductListInAd.addLoadMore();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            FragmentManager fm = mActivity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ProductListInAdFragment adFragment = ProductListInAdFragment.newInstance(loadProductListInAd, adProductCatalog);
            ft.replace(R.id.product_list_in_ad_place_holder, adFragment);
            ft.commit();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        clearBackStack();
        finish();
    }

    public void clearBackStack() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ft.commit();
    }






//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.product_list_in_ad_menu, menu);
//        return true;
//    }
}
