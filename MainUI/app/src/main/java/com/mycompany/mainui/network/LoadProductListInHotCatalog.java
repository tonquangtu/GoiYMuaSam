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
 * Created by Dell on 4/20/2016.
 */
public class LoadProductListInHotCatalog implements Serializable{

    private ArrayList<ShortProduct> products;
    private boolean isLoadProductListComplete ;
    private int numCall;
    private List<ShortProduct> loadMoreList;
    private String message;

    public LoadProductListInHotCatalog() {
        products = new ArrayList<>();
        numCall = 0;
        isLoadProductListComplete = false;
        message = "";
        loadMoreList = null;
    }

    public LoadProductListInHotCatalog(ArrayList<ShortProduct> listProduct) {
        if(listProduct != null) {
            this.products = listProduct;
        }
        numCall = 1;
        message = "";
        loadMoreList = null;
    }

    public void loadProductListInHotCatalog(Activity activity, String hotCatalog) {

        isLoadProductListComplete = false;
        Map<String, String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_GET_PRODUCT_LIST_IN_HOT_CATALOG);
        tag.put(RequestId.TAG_HOT_CATALOG, hotCatalog);
        tag.put(RequestId.TAG_CITY, InfoAccount.getCity(activity));
        tag.put(RequestId.TAG_NUM_CALL, numCall + "");

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
                    if(data != null ) {

                        message = data.getMessage();
                        if(data.getSuccess() == RequestId.SUCCESS) {
                            loadMoreList = data.getProductList();
                        }else {
                            DisplayNotification.toast(activity, message);
                        }
                    }
                }

                isLoadProductListComplete = true;
            }

            @Override
            public void onFailure(Call<GetListProductData> call, Throwable t) {
                DisplayNotification.errorLoaddata(activity, t.getMessage() + " load produc hot");
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

    public boolean addLoadMore() {

        boolean isAdd = false;
        if(loadMoreList != null && loadMoreList.size() > 0) {

            for(int i = 0; i < loadMoreList.size(); i++) {
                products.add(loadMoreList.get(i));
            }
            loadMoreList = null;
            isAdd = true;
        }
        return isAdd;
    }
}
