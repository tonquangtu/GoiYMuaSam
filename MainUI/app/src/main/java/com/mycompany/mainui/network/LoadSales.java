package com.mycompany.mainui.network;

import android.app.Activity;

import com.mycompany.mainui.model.GetSalesData;
import com.mycompany.mainui.model.InfoAccount;
import com.mycompany.mainui.model.RequestId;
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.model.ShortShop;
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
public class LoadSales implements Serializable{

    public static final String SALES_SHOP = "Cửa hàng khuyến mại";
    public static final String SALES_PRODUCT = "Sản phẩm khuyến mại";
    private boolean isLoadComplete;
    private List<ShortProduct> shortProducts;
    private List<ShortShop> shortShops;
    private ArrayList listData;


    public LoadSales() {

        shortProducts = new ArrayList<>();
        shortShops = new ArrayList<>();
        listData = new ArrayList();
    }
    public void loadSales(Activity activity) {

        isLoadComplete = false;
        Map<String , String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_GET_SALES);
        tag.put(RequestId.TAG_USERNAME, InfoAccount.getUsername(activity));
        tag.put(RequestId.TAG_CITY, InfoAccount.getCity(activity));

        loadSalesFromServer(activity, tag);

    }

    public void loadSalesFromServer(final Activity activity, Map<String, String> tag) {

        Call<GetSalesData> call = ConnectServer.getResponseAPI().callGetSales(tag);
        call.enqueue(new Callback<GetSalesData>() {
            @Override
            public void onResponse(Call<GetSalesData> call, Response<GetSalesData> response) {

                if(response != null && response.isSuccessful()) {
                    GetSalesData data = response.body();
                    if(data != null && data.getSuccess() == RequestId.SUCCESS) {
                        shortProducts = data.getProductList();
                        shortShops = data.getShopList();
                    }else {
                        DisplayNotification.toast(activity, "Khong co du lieu");
                    }
                }
                isLoadComplete = true;
            }

            @Override
            public void onFailure(Call<GetSalesData> call, Throwable t) {
                DisplayNotification.errorLoaddata(activity, t.getMessage() + " load sales");
                isLoadComplete = true;
            }
        });
    }

    public boolean isLoadComplete() {
        return isLoadComplete;
    }

    public boolean addSalesList() {
        boolean isAdd = false;
        if(shortShops != null && shortShops.size() > 0) {
            isAdd = true;
            listData.add(SALES_SHOP);
            for(int i = 0; i < shortShops.size(); i++) {
                if(shortShops.get(i) != null) {
                    listData.add(shortShops.get(i));
                }
            }
        }
        if(shortProducts != null && shortProducts.size() > 0) {
            isAdd = true;
            listData.add(SALES_PRODUCT);
            for( int i = 0; i < shortProducts.size(); i++) {
                if(shortProducts.get(i) != null) {
                    listData.add(shortProducts.get(i));
                }
            }
        }
        shortProducts = null;
        shortShops = null;
        return  isAdd;
    }

    public ArrayList getListData() {
        return listData;
    }
}
