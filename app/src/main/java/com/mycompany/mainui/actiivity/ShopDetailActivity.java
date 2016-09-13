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
import android.view.MenuItem;

import com.facebook.FacebookSdk;
import com.mycompany.mainui.R;
import com.mycompany.mainui.fragment.FragmentShopDetail;
import com.mycompany.mainui.fragment.LoadingFragment;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.network.LoadShopDetail;

public class ShopDetailActivity extends AppCompatActivity {

    ShopDetailActivity mActivity;
    String idShop;
    String shopName;
    Toolbar toolbar;
    LoadShopDetail loadShopDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        // init facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ConfigData.ARG_PACKAGE);
        idShop = bundle.getString(ConfigData.ARG_ID_SHOP);
        shopName = bundle.getString(ConfigData.ARG_SHOP_NAME);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(shopName);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        loadShopDetail = new LoadShopDetail();
        mActivity = this;
        new LoadProduct().execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class LoadProduct extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            FragmentManager fm = mActivity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            LoadingFragment loadingFragment = LoadingFragment.newInstance();
            ft.replace(R.id.shop_detail_place_holder, loadingFragment);
            ft.commit();
        }

        @Override
        protected Void doInBackground(Void... params) {

            loadShopDetail.loadShop(mActivity, idShop);
            while(!loadShopDetail.isLoadComplete()) {
                SystemClock.sleep(ConfigData.TIME_SLEEP);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                FragmentManager fm = mActivity.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                FragmentShopDetail fragmentShopDetail = FragmentShopDetail.newInstance(loadShopDetail);

                ft.replace(R.id.shop_detail_place_holder, fragmentShopDetail);
                ft.commit();
            }catch (Exception e) {
                mActivity.finish();
            }
        }
    }
}
