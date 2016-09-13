package com.mycompany.mainui.network;

import android.app.Activity;

import com.mycompany.mainui.model.GetListProductData;
import com.mycompany.mainui.model.InfoAccount;
import com.mycompany.mainui.model.RequestId;
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.networkutil.ConnectServer;
import com.mycompany.mainui.view.DisplayNotification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dell on 4/23/2016.
 */
public class LoadProductListInAd implements Serializable{

    private ArrayList<ShortProduct> shortProducts;
    private int numCall;
    private List<ShortProduct> loadMoreList;
    private String message = "";
    private boolean isLoadComplete;


    public LoadProductListInAd() {
        shortProducts = new ArrayList<>();
        numCall = 0;
        loadMoreList = null;
        isLoadComplete = false;
    }


    public void loadProductListInAd(Activity activity, String adCatalog) {

        isLoadComplete = false;
        Map<String, String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_GET_LIST_PRODUCT_IN_AD_CATALOG);
        tag.put(RequestId.TAG_AD_PRODUCT_CATALOG_NAME, adCatalog);
        tag.put(RequestId.TAG_NUM_CALL, numCall + "");
        tag.put(RequestId.TAG_CITY, InfoAccount.getCity(activity));

        loadProductListInAdFromServer(activity, tag);
    }

    public void loadProductListInAdFromServer(final Activity activity, Map<String, String> tag) {

        Call<GetListProductData> call = ConnectServer.getResponseAPI()
                .callGetProductListInAd(tag);
        call.enqueue(new Callback<GetListProductData>() {
            @Override
            public void onResponse(Call<GetListProductData> call, Response<GetListProductData> response) {

                if(response != null && response.isSuccessful()) {
                    GetListProductData data = response.body();

                    if(data != null ) {
                        message = data.getMessage();
                        if(data.getSuccess() == RequestId.SUCCESS) {
                            loadMoreList = data.getProductList();
                            numCall ++;
                        }else {
                            DisplayNotification.toast(activity, data.getMessage());
                        }
                    }
                }
                isLoadComplete = true;
            }

            @Override
            public void onFailure(Call<GetListProductData> call, Throwable t) {
                DisplayNotification.errorLoaddata(activity, t.getMessage());
                loadMoreList = null;
                isLoadComplete = true;
            }
        });
    }

    public boolean addLoadMore() {

        boolean isAdd = false;
        if(loadMoreList != null && loadMoreList.size() > 0) {
            isAdd = true;
            for(int i = 0; i < loadMoreList.size(); i++) {
                ShortProduct s = loadMoreList.get(i);
                if(s != null) {
                    shortProducts.add(s);
                }
            }
        }
        loadMoreList = null;
        return isAdd;
    }



    public ArrayList<ShortProduct> getShortProducts() {
        return shortProducts;
    }

    public String getMessage() {
        return message;
    }

    public boolean isLoadComplete() {
        return isLoadComplete;
    }
}
