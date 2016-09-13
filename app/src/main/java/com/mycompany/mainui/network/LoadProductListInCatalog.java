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
 * Created by Dell on 5/3/2016.
 */
public class LoadProductListInCatalog implements Serializable{

    private String message;
    private List<ShortProduct> loadMore;
    private ArrayList<ShortProduct> listData;
    private boolean isLoadComplete;
    private int numCall;

    public LoadProductListInCatalog(ArrayList<ShortProduct> shortProducts) {

        this.listData = shortProducts;
        numCall = 1;
        message = "";
        loadMore = null;
        isLoadComplete = false;
    }

    public LoadProductListInCatalog() {
        this.listData = new ArrayList<>();
        numCall = 0;
        message = "";
        loadMore = null;
        isLoadComplete = false;
    }

    public void loadProducts(Activity activity, String catalog) {

        isLoadComplete = false;
        Map<String, String > tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_GET_PRODUCT_LIST_IN_CATALOG);
        tag.put(RequestId.TAG_CITY, InfoAccount.getCity(activity));
        tag.put(RequestId.TAG_USERNAME, InfoAccount.getUsername(activity));
        tag.put(RequestId.TAG_PRODUCT_CATALOG_NAME, catalog);
        tag.put(RequestId.TAG_NUM_CALL, numCall + "");

        loadProductsFromServer(activity, tag);

    }

    private void loadProductsFromServer(final Activity activity, Map<String, String> tag) {

        Call<GetListProductData> call = ConnectServer.getResponseAPI().callGetProductListInCatalog(tag);
        call.enqueue(new Callback<GetListProductData>() {
            @Override
            public void onResponse(Call<GetListProductData> call, Response<GetListProductData> response) {

                if(response.isSuccessful()) {
                    GetListProductData data = response.body();
                    if(data != null) {
                        message = data.getMessage();
                        if (data.getSuccess() == RequestId.SUCCESS) {
                            loadMore = data.getProductList();
                            numCall ++;
                        } else {
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
                listData.add(loadMore.get(i));
            }
            isAdd = true;
        }
        loadMore = null;
        return isAdd;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<ShortProduct> getListData() {
        return listData;
    }

    public boolean isLoadComplete() {
        return isLoadComplete;
    }
}
