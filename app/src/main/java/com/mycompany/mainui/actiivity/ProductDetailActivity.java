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

import com.mycompany.mainui.R;
import com.mycompany.mainui.fragment.FragmentProductDetail;
import com.mycompany.mainui.fragment.LoadingFragment;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.network.LoadProductDetail;

public class ProductDetailActivity extends AppCompatActivity {

    ProductDetailActivity mActivity;
    String idProduct;
    String productName;
    Toolbar toolbar;
    LoadProductDetail loadProductDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        // init facebook sdk

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ConfigData.ARG_PACKAGE);
        idProduct = bundle.getString(ConfigData.ARG_ID_PRODUCT);
        productName = bundle.getString(ConfigData.ARG_PRODUCT_NAME);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(productName);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        loadProductDetail = new LoadProductDetail();
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
            ft.replace(R.id.product_detail_place_holder, loadingFragment);
            ft.commit();
        }

        @Override
        protected Void doInBackground(Void... params) {

            loadProductDetail.loadProduct(mActivity, idProduct);
            while(!loadProductDetail.isLoadComplete()) {
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
                FragmentProductDetail fragmentProductDetail = FragmentProductDetail.newInstance(loadProductDetail);

                ft.replace(R.id.product_detail_place_holder, fragmentProductDetail);
                ft.commit();
            }catch (Exception e){
                mActivity.finish();
            }

        }
    }
}
