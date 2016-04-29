package com.mycompany.mainui.actiivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.mycompany.mainui.Fragment.ChooseCityFragment;
import com.mycompany.mainui.Fragment.LoadingFragment;
import com.mycompany.mainui.R;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.InfoAccount;
import com.mycompany.mainui.network.LoadHotList;
import com.mycompany.mainui.network.LoadProductListSuggestion;
import com.mycompany.mainui.network.LoadSales;
import com.mycompany.mainui.network.LoadShopListSuggestion;
import com.mycompany.mainui.networkutil.StatusInternet;
import com.mycompany.mainui.view.DisplayNotification;

public class SplashActivity extends AppCompatActivity implements SplashCallback {

    private LoadSales loadSales;
    private LoadHotList loadHotList;
    private LoadShopListSuggestion loadShopListSuggestion;
    private LoadProductListSuggestion loadProductListSuggestion;
    public static final String ARG_LOAD_SALES = "ARG_LOAD_SALES";
    public static final String ARG_LOAD_HOT_LIST = "ARG_LOAD_HOT_LIST";
    public static final String ARG_LOAD_PRODUCT_SUGGESTION = "ARG_LOAD_PRODUCT_SUGGESTION";
    public static final String ARG_LOAD_SHOP_SUGGESTION = "ARG_LOAD_SHOP_SUGGESTION";
    public static final String ARG_PACKAGE = "ARG_PACKAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String city = InfoAccount.getCity(this);
        if(city.equals("")) {
            initFragmentChooseCity();
        }else {
            initFragmentLoading();
        }

    }

    public void initFragmentChooseCity() {

       if(StatusInternet.isInternet(this)) {
           FragmentManager fm = getSupportFragmentManager();
           FragmentTransaction ft = fm.beginTransaction();
           ChooseCityFragment chooseCityFragment = ChooseCityFragment.newInstance();

           ft.replace(R.id.splash_place_holder,chooseCityFragment);
           ft.addToBackStack("ChooseCityFragment");
           ft.commit();
       }else {
           DisplayNotification.displayNotifiNoNetwork(this);
       }
    }

    public void initFragmentLoading() {

        if(StatusInternet.isInternet(this)) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            LoadingFragment loadingFragment = LoadingFragment.newInstance();

            ft.replace(R.id.splash_place_holder,loadingFragment);
            ft.addToBackStack("LoadingFragment");
            ft.commit();
            loadDataFromServer();
        }else {
            DisplayNotification.displayNotifiNoNetwork(this);
        }

    }

    public void loadDataFromServer() {

        loadSales = new LoadSales();
        loadHotList = new LoadHotList();
        loadShopListSuggestion = new LoadShopListSuggestion();
        loadProductListSuggestion = new LoadProductListSuggestion();

        // loading data
        new LoadTask().execute();

    }

    private class LoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            loadShopListSuggestion.loadShopListSuggestion(SplashActivity.this);
            loadProductListSuggestion.loadProductListSuggestion(SplashActivity.this);
            loadProductListSuggestion.loadAdProduct(SplashActivity.this);
            loadHotList.loadHotProduct(SplashActivity.this);
            loadSales.loadSales(SplashActivity.this);

//            // looper here, until load data complete
            while(!loadProductListSuggestion.isLoadProductComplete() ||
                    !loadProductListSuggestion.isLoadAdComplete() ||
                    !loadShopListSuggestion.isLoadShopComplete() ||
                    !loadHotList.isLoadHotCatalogComplete() ||
                    !loadSales.isLoadComplete()
                    ) {
                SystemClock.sleep(ConfigData.TIME_SLEEP); // sleep 100 ms
            }

            loadProductListSuggestion.addLoadMoreList();
            loadShopListSuggestion.addLoadMoreList();
            loadHotList.addLoadMoreList();
            loadSales.addSalesList();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // mo len MainActivity
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_LOAD_PRODUCT_SUGGESTION, loadProductListSuggestion);
            bundle.putSerializable(ARG_LOAD_SHOP_SUGGESTION, loadShopListSuggestion);
            bundle.putSerializable(ARG_LOAD_HOT_LIST, loadHotList);
            bundle.putSerializable(ARG_LOAD_SALES, loadSales);

            intent.putExtra(ARG_PACKAGE, bundle);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void msgFromFragToActivity(String city) {
        InfoAccount.putCity(SplashActivity.this, city);
        initFragmentLoading();
    }

}
