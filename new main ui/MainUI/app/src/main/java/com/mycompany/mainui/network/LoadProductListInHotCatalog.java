package com.mycompany.mainui.network;

import android.app.Activity;

import com.mycompany.mainui.model.GetListProductData;
import com.mycompany.mainui.model.HotProduct;
import com.mycompany.mainui.model.InfoAccount;
import com.mycompany.mainui.model.RequestId;
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.networkutil.ConnectServer;
import com.mycompany.mainui.view.DisplayNotification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dell on 4/20/2016.
 */
public class LoadProductListInHotCatalog {

    private ArrayList<ShortProduct> products;
    private boolean isLoadProductListComplete ;
    private int start;

    public LoadProductListInHotCatalog() {
        products = new ArrayList<>();
        start = 0;
        isLoadProductListComplete = false;
    }

    public void loadProductListInHotCatalog(Activity activity, HotProduct hotProduct) {

        isLoadProductListComplete = false;
        Map<String, String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_GET_PRODUCT_LIST_IN_HOT_CATALOG);
        tag.put(RequestId.TAG_USERNAME, InfoAccount.getUsername(activity));
        tag.put(RequestId.TAG_CITY, InfoAccount.getCity(activity));
        tag.put(RequestId.TAG_START, start + "");

        loadProductListInHotCatalogFromServer(activity, tag);

    }


    public void loadProductListInHotCatalogFromServer(final Activity activity, Map<String, String> tag) {

        Call<GetListProductData> call = ConnectServer
                .getResponseAPI()
                .callGetProductListInHotCatalog(tag);

        call.enqueue(new Callback<GetListProductData>() {
            @Override
            public void onResponse(Call<GetListProductData> call, Response<GetListProductData> response) {

                if(response != null && response.isSuccessful()) {
                    GetListProductData data = response.body();
                    if(data != null && data.getProductList() != null) {
                        if(data.getProductList().size() > 0) {
                            products.addAll(data.getProductList());
                        }else {
                            DisplayNotification.toast(activity, data.getMessage());
                        }
                    }
                }

                isLoadProductListComplete = true;
            }

            @Override
            public void onFailure(Call<GetListProductData> call, Throwable t) {
                DisplayNotification.errorLoaddata(activity, t.getMessage());
                isLoadProductListComplete = true;
            }
        });
    }

    public ArrayList<ShortProduct> getProducts() {
        return products;
    }

    public boolean isLoadProductListComplete() {
        return isLoadProductListComplete;
    }
}
