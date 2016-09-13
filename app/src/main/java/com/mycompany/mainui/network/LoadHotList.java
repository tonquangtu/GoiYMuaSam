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

    private boolean isLoadHotCatalogComplete = false;
    private List<HotProduct> loadMoreList;
    int numCall;
    private ArrayList listData;
    private String message;

    public LoadHotList() {
        numCall  = 0;
        listData = new ArrayList();
        message = "";
    }

    public void loadHotProduct(Activity activity) {

        isLoadHotCatalogComplete = false;
        Map<String, String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_GET_HOT_PRODUCT);
        tag.put(RequestId.TAG_CITY, InfoAccount.getCity(activity));
        tag.put(RequestId.TAG_NUM_CALL, numCall + "");
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
                        message = data.getMessage();
                        if(data.getSuccess() == RequestId.SUCCESS) {
                            numCall ++;
                            loadMoreList = data.getHotProductList();
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
                DisplayNotification.errorLoaddata(activity, t.getMessage() + " load hot");
                isLoadHotCatalogComplete = true;
            }
        });
    }

    public boolean addLoadMoreList() {

        boolean isLoadMore = false;
        if(loadMoreList != null && loadMoreList.size() > 0) {

            isLoadMore = true;
            for(int i = 0; i < loadMoreList.size(); i++) {
                HotProduct hotProduct = loadMoreList.get(i);
                listData.add(hotProduct.getHotProductCatalogName());
                List<ShortProduct> shortProducts = hotProduct.getProductList();
                if(shortProducts != null) {
                    for( int j = 0; j < shortProducts.size() ; j++) {
                        listData.add(shortProducts.get(j));
                    }
                }
            }
        }
        loadMoreList = null;
        return isLoadMore;

    }

    public boolean isLoadHotCatalogComplete() {
        return isLoadHotCatalogComplete;
    }

    public ArrayList getListData() {
        return listData;
    }

    public String getMessage() {
        return message;
    }
}
