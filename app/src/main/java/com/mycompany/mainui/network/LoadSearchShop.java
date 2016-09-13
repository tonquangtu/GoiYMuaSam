package com.mycompany.mainui.network;

import android.app.Activity;

import com.mycompany.mainui.model.GetShopListData;
import com.mycompany.mainui.model.InfoAccount;
import com.mycompany.mainui.model.RequestId;
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
 * Created by Dell on 07-May-16.
 */
public class LoadSearchShop implements Serializable{

    private String message;
    private List<ShortShop> loadMore;
    private ArrayList<ShortShop> listData;
    private boolean isLoadComplete;
    private int numCall;

    public LoadSearchShop() {
        this.listData = new ArrayList<>();
        numCall = 0;
        message = "";
        loadMore = null;
        isLoadComplete = false;
    }

    public void loadShops(Activity activity, String query) {

        isLoadComplete = false;
        Map<String, String > tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_SEARCH_SHOP);
        tag.put(RequestId.TAG_CITY, InfoAccount.getCity(activity));
        tag.put(RequestId.TAG_QUERY, query);
        tag.put(RequestId.TAG_NUM_CALL, numCall + "");

        loadShopsFromServer(activity, tag);

    }

    private void loadShopsFromServer(final Activity activity, Map<String, String> tag) {

        Call<GetShopListData> call = ConnectServer.getResponseAPI().callSearchShop(tag);
        call.enqueue(new Callback<GetShopListData>() {
            @Override
            public void onResponse(Call<GetShopListData> call, Response<GetShopListData> response) {

                if(response.isSuccessful()) {
                    GetShopListData data = response.body();
                    if(data != null) {
                        message = data.getMessage();
                        if (data.getSuccess() == RequestId.SUCCESS) {
                            loadMore = data.getShopList();
                            numCall ++;
                        } else {
                            DisplayNotification.toast(activity, message);
                        }
                    }
                }
                isLoadComplete = true;
            }

            @Override
            public void onFailure(Call<GetShopListData> call, Throwable t) {
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

    public ArrayList<ShortShop> getListData() {
        return listData;
    }

    public boolean isLoadComplete() {
        return isLoadComplete;
    }
}
