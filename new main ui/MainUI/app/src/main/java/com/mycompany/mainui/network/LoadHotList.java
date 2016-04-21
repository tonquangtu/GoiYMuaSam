package com.mycompany.mainui.network;

import android.app.Activity;

import com.mycompany.mainui.model.GetHotProductData;
import com.mycompany.mainui.model.HotProduct;
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
 * Created by Dell on 4/20/2016.
 */
public class LoadHotList implements Serializable {

    private ArrayList<HotProduct> hotProducts;
    private boolean isLoadHotCatalogComplete = false;


    public LoadHotList() {
        hotProducts = new ArrayList<>();

    }

    public void loadHotProduct(Activity activity) {

        isLoadHotCatalogComplete = false;
        Map<String, String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_GET_HOT_PRODUCT);
        tag.put(RequestId.TAG_CITY, InfoAccount.getCity(activity));

        loadHotProductFromServer(activity, tag);

    }


    public void loadHotProductFromServer(final Activity activity, Map<String, String> tag) {

        Call<GetHotProductData> call = ConnectServer
                .getResponseAPI().callGetHotProduct(tag);
        call.enqueue(new Callback<GetHotProductData>() {
            @Override
            public void onResponse(Call<GetHotProductData> call, Response<GetHotProductData> response) {

                if(response != null && response.isSuccessful()) {
                    GetHotProductData data = response.body();
                    if(data != null) {
                        if(data.getSuccess() == RequestId.SUCCESS) {
                            hotProducts.addAll(data.getHotProductList());
                        }else {
                            DisplayNotification.toast(activity, data.getMessage());
                        }
                    }else {
                        DisplayNotification.toast(activity,"Khong nhan duoc du lieu");
                    }
                }
                isLoadHotCatalogComplete = true;
            }

            @Override
            public void onFailure(Call<GetHotProductData> call, Throwable t) {
                DisplayNotification.displayNotifiNoNetwork(activity);
                isLoadHotCatalogComplete = true;
            }
        });
    }

    public ArrayList<HotProduct> getHotProducts() {
        return hotProducts;
    }

    public ArrayList getHotObject() {

        ArrayList hotObjects = new ArrayList();
        for(int i = 0; i < hotProducts.size(); i++) {

            HotProduct hotProduct = hotProducts.get(i);
            hotObjects.add(hotProduct.getHotProductCatalogName());
            List<ShortProduct> shortProducts = hotProduct.getProductList();
            for(int j = 0; j < shortProducts.size(); j++) {
                hotObjects.add(shortProducts.get(j));
            }
        }

        return hotObjects;
    }

    public boolean isLoadHotCatalogComplete() {
        return isLoadHotCatalogComplete;
    }
}
