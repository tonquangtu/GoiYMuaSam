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
 * Created by Dell on 5/2/2016.
 */
public class LoadProductListInSales implements Serializable{

    private String message;
    private ArrayList<ShortProduct> salesList;
    int numCall;
    private boolean isLoadComplete;
    private List<ShortProduct> loadMore;

    public ArrayList<ShortProduct> getSalesList() {
        return salesList;
    }

    public LoadProductListInSales(ArrayList shortProducts) {

        if(shortProducts == null) {
            salesList = new ArrayList<>();
            numCall = 0;
        }else {
            this.salesList = shortProducts;
            numCall = 1;
        }
        isLoadComplete = false;
        loadMore = null;
        message = "";
    }


    public void loadProductListInSales(Activity activity) {

        isLoadComplete = false;
        Map<String, String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_GET_PRODUCT_LIST_IN_SALES);
        tag.put(RequestId.TAG_CITY, InfoAccount.getCity(activity));
        tag.put(RequestId.TAG_NUM_CALL, numCall + "");
        loadProductListInSalesFromServer(activity, tag);

    }

    private void loadProductListInSalesFromServer(final Activity activity, Map<String, String> tag) {

        Call<GetListProductData> call = ConnectServer.getResponseAPI().callGetProductListInSales(tag);
        call.enqueue(new Callback<GetListProductData>() {
            @Override
            public void onResponse(Call<GetListProductData> call, Response<GetListProductData> response) {

                if(response.isSuccessful()) {
                    GetListProductData data = response.body();
                    if(data != null) {
                        message = data.getMessage();
                        if(data.getSuccess() == RequestId.SUCCESS) {
                            loadMore = data.getProductList();
                            numCall ++;
                        }else {
                            DisplayNotification.toast(activity, message);
                        }
                    }
                }
                isLoadComplete = true;
            }

            @Override
            public void onFailure(Call<GetListProductData> call, Throwable t) {
                DisplayNotification.errorLoaddata(activity, t.getMessage());
                loadMore = null;
                isLoadComplete = true;
            }
        });
    }

    public boolean addLoadMore() {

        boolean isAdd = false;
        if(loadMore != null && loadMore.size() > 0) {
            for(int i = 0; i < loadMore.size(); i++) {
                salesList.add(loadMore.get(i));
            }
            isAdd = true;
        }
        loadMore = null;
        return isAdd;
    }

    public boolean isLoadComplete() {
        return isLoadComplete;
    }

    public String getMessage() {
        return message;
    }
}
