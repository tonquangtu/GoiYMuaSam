package com.mycompany.mainui.actiivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mycompany.mainui.R;
import com.mycompany.mainui.model.InfoAccount;
import com.mycompany.mainui.network.LoadHotList;
import com.mycompany.mainui.network.LoadProductListSuggestion;
import com.mycompany.mainui.network.LoadSales;
import com.mycompany.mainui.network.LoadShopListSuggestion;

public class SplashActivity extends AppCompatActivity {

    private final int CHOOSE_CITY_LAYOUT = 1;
    private final int LOADING_LAYOUT = 2;
    int viewType;
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
        if(city == null || city.equals("")) {
            viewType = CHOOSE_CITY_LAYOUT;
            InfoAccount.putCity(this, "Ha Noi");

        }else {
            viewType = LOADING_LAYOUT;
        }
    }

   public void loadDataFromServer() {

       loadSales = new LoadSales();
       loadHotList = new LoadHotList();
       loadShopListSuggestion = new LoadShopListSuggestion();
       loadProductListSuggestion = new LoadProductListSuggestion();

       // loading data
       loadShopListSuggestion.loadShopListSuggestion(this);
       loadProductListSuggestion.loadProductListSuggestion(this);
       loadProductListSuggestion.loadAdProduct(this);
       loadHotList.loadHotProduct(this);
       loadSales.loadSales(this);

       new LoadTask().execute();

   }

    private class LoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            // looper here, until load data complete
            while(!loadProductListSuggestion.isLoadProductComplete() ||
                    !loadProductListSuggestion.isLoadAdComplete() ||
                    !loadShopListSuggestion.isLoadShopComplete() ||
                    !loadHotList.isLoadHotCatalogComplete() ||
                    !loadSales.isLoadComplete()
                    );

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
        }
    }


}
