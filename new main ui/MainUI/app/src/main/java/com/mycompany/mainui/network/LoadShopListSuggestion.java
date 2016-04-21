package com.mycompany.mainui.network;

import android.app.Activity;

import com.mycompany.mainui.model.GetShopListSuggestionData;
import com.mycompany.mainui.model.InfoAccount;
import com.mycompany.mainui.model.MyLocation;
import com.mycompany.mainui.model.RequestId;
import com.mycompany.mainui.model.ShortShop;
import com.mycompany.mainui.networkutil.ConnectServer;
import com.mycompany.mainui.networkutil.ResponseFromServerAPI;
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
 * Created by Dell on 4/18/2016.
 */
public class LoadShopListSuggestion implements Serializable{

    private int start;
    private boolean isLoadShopComplete;
    private ArrayList<ShortShop> shopList;
    private List<ShortShop> loadMoreShopList = null;

    public LoadShopListSuggestion() {
        start = 0;
        isLoadShopComplete = false;
        shopList = new ArrayList<>();
    }

    public void loadShopListSuggestion(Activity activity) {

        isLoadShopComplete = false;
        Map<String, String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_GET_SHOP_LIST_SUGGESTION);
        tag.put(RequestId.TAG_USERNAME, InfoAccount.getUsername(activity));
        tag.put(RequestId.TAG_CITY, InfoAccount.getCity(activity));
        tag.put(RequestId.TAG_DISTANCE, InfoAccount.getDistance(activity) + "");
        MyLocation myLocation = InfoAccount.getLocation(activity);
        tag.put(RequestId.TAG_LONGIUDE, myLocation.getLongitude() + "");
        tag.put(RequestId.TAG_LATITUDE, myLocation.getLatitude() + "");
        tag.put(RequestId.TAG_START, start + "");

        loadFromServer(tag,activity);

    }

    public void resetLoadShopSuggestion(Activity activity, MyLocation myLocation) {

        isLoadShopComplete = false;
        start = 0;
        Map<String, String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_GET_SHOP_LIST_SUGGESTION);
        tag.put(RequestId.TAG_USERNAME, InfoAccount.getUsername(activity));
        tag.put(RequestId.TAG_CITY, InfoAccount.getCity(activity));
        tag.put(RequestId.TAG_DISTANCE, InfoAccount.getDistance(activity) + "");
        tag.put(RequestId.TAG_LONGIUDE, myLocation.getLongitude() + "");
        tag.put(RequestId.TAG_LATITUDE, myLocation.getLatitude() + "");
        tag.put(RequestId.TAG_START, start + "");

        loadFromServer(tag,activity);
    }


    /**
     * Load tu server ve
     * @param tag : Thong diep client gui toi server
     */
    public void loadFromServer(Map<String, String> tag, final Activity activity) {

        ResponseFromServerAPI responseAPI = ConnectServer.getResponseAPI();
        Call<GetShopListSuggestionData> call = responseAPI.callGetShopListSuggestion(tag);

        call.enqueue(new Callback<GetShopListSuggestionData>() {
            @Override
            public void onResponse(Call<GetShopListSuggestionData> call,
                                   Response<GetShopListSuggestionData> response) {

                if(response != null) {
                    GetShopListSuggestionData data = response.body();
                    if(data != null){
                        if(data.getSuccess() == RequestId.SUCCESS) {
                            start = data.getStart();
                            loadMoreShopList = data.getShopList();
                        }
                        else
                            DisplayNotification.toast(activity, data.getMessage());
                    }
                }
                isLoadShopComplete = true;
            }

            @Override
            public void onFailure(Call<GetShopListSuggestionData> call, Throwable t) {
                DisplayNotification.displayNotifiNoNetwork(activity);
                isLoadShopComplete = true;
            }
        });
    }

    public boolean isLoadShopComplete() {
        return isLoadShopComplete;
    }

    public ArrayList<ShortShop> getShopList() {
        return shopList;
    }

    public List<ShortShop> getLoadMoreShopList() {
        return loadMoreShopList;
    }

    public boolean addLoadMoreList() {

        if(loadMoreShopList != null) {
            shopList.addAll(loadMoreShopList);
            loadMoreShopList = null;
            return true;
        }
        return false;
    }
}
