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
import com.mycompany.mainui.fragment.FragmentResultSearchShop;
import com.mycompany.mainui.fragment.LoadingFragment;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.network.LoadSearchShop;

public class SearchResultShopActivity extends AppCompatActivity {

    String query;
    Toolbar toolbar;
    ActionBar actionBar;
    SearchResultShopActivity mActivity;
    LoadSearchShop loadSearchShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_shop);
        mActivity = SearchResultShopActivity.this;

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ConfigData.ARG_PACKAGE);
        query = bundle.getString(ConfigData.ARG_SEARCH_QUERY);
        loadSearchShop = new LoadSearchShop();

        initToolbar();

        new LoadShop().execute();

    }

    public void initToolbar() {

        toolbar = (Toolbar)findViewById(R.id.search_shop_toolbar);
        setSupportActionBar(toolbar);
        setTitle(query);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
    }

    private class LoadShop extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            FragmentManager fm = mActivity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            LoadingFragment loadingFragment = LoadingFragment.newInstance();
            ft.replace(R.id.search_shop_place_holder, loadingFragment);
            ft.commit();
        }

        @Override
        protected Void doInBackground(Void... params) {

            loadSearchShop.loadShops(mActivity, query);
            while(!loadSearchShop.isLoadComplete()) {
                SystemClock.sleep(10);
            }
            loadSearchShop.addLoadMore();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            FragmentManager fm = mActivity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            FragmentResultSearchShop fragment = FragmentResultSearchShop.newInstance(query, loadSearchShop);
            ft.replace(R.id.search_shop_place_holder, fragment);
            ft.commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
