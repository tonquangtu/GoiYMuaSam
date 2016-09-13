package com.mycompany.mainui.network;

import android.app.Activity;

import com.mycompany.mainui.model.GetShopData;
import com.mycompany.mainui.model.InfoAccount;
import com.mycompany.mainui.model.RequestId;
import com.mycompany.mainui.model.ResponseFromServerData;
import com.mycompany.mainui.model.ShopDetail;
import com.mycompany.mainui.model.ShortShop;
import com.mycompany.mainui.networkutil.ConnectServer;
import com.mycompany.mainui.view.DisplayNotification;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dell on 08-May-16.
 */
public class LoadShopDetail implements Serializable {

    private ShopDetail shopDetail;
    private List<ShortShop> shortShops;
    int like;
    private boolean isLoadComplete;

    public LoadShopDetail() {
        shopDetail = null;
        shortShops = null;
        like = 0;
        isLoadComplete = false;
    }

    public void loadShop(Activity activity, String idShop) {

        isLoadComplete = false;
        Map<String, String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_GET_SHOP);
        tag.put(RequestId.TAG_ID_SHOP, idShop);
        tag.put(RequestId.TAG_USERNAME, InfoAccount.getUsername(activity));
        tag.put(RequestId.TAG_CITY, InfoAccount.getCity(activity));
        loadShopFromServer(activity, tag);

    }

    public void loadShopFromServer(final Activity activity, Map<String, String> tag) {

        Call<GetShopData> call = ConnectServer.getResponseAPI().callGetShop(tag);
        call.enqueue(new Callback<GetShopData>() {
            @Override
            public void onResponse(Call<GetShopData> call, Response<GetShopData> response) {
                if(response.isSuccessful()) {
                    GetShopData data = response.body();
                    if(data != null && data.getSuccess() == RequestId.SUCCESS) {
                        shopDetail = data.getShop();
                        shortShops = data.getRelationShops();
                        like = data.getLike();
                    }
                }
                isLoadComplete = true;
            }

            @Override
            public void onFailure(Call<GetShopData> call, Throwable t) {
                DisplayNotification.errorLoaddata(activity, t.getMessage());
                isLoadComplete = true;
            }
        });

    }

    public void sentUserViewShop(String username, String idShop) {

        Map<String, String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_USER_VIEW_SHOP);
        tag.put(RequestId.TAG_USERNAME, username);
        tag.put(RequestId.TAG_ID_SHOP, idShop);

        Call<ResponseFromServerData> call = ConnectServer.getResponseAPI().callUserViewShop(tag);
        call.enqueue(new Callback<ResponseFromServerData>() {
            @Override
            public void onResponse(Call<ResponseFromServerData> call, Response<ResponseFromServerData> response) {

            }

            @Override
            public void onFailure(Call<ResponseFromServerData> call, Throwable t) {

            }
        });

    }

    public void sentLikeShop(String username, String idShop, int like) {

        Map<String, String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_USER_LIKE_SHOP);
        tag.put(RequestId.TAG_USERNAME, username);
        tag.put(RequestId.TAG_ID_SHOP, idShop);
        tag.put(RequestId.TAG_LIKE, like + "");

        Call<ResponseFromServerData> call = ConnectServer.getResponseAPI().callSentLike(tag);
        call.enqueue(new Callback<ResponseFromServerData>() {
            @Override
            public void onResponse(Call<ResponseFromServerData> call, Response<ResponseFromServerData> response) {

            }

            @Override
            public void onFailure(Call<ResponseFromServerData> call, Throwable t) {

            }
        });
    }

    public void pinShop(String idShop, String username, Activity activity) {

        Map<String, String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_PIN_SHOP);
        tag.put(RequestId.TAG_USERNAME, username);
        tag.put(RequestId.TAG_ID_SHOP, idShop);

        Call<ResponseFromServerData> call = ConnectServer.getResponseAPI().callPin(tag);
        call.enqueue(new Callback<ResponseFromServerData>() {
            @Override
            public void onResponse(Call<ResponseFromServerData> call, Response<ResponseFromServerData> response) {

            }

            @Override
            public void onFailure(Call<ResponseFromServerData> call, Throwable t) {

            }
        });

    }




    public ShopDetail getShopDetail() {
        return shopDetail;
    }

    public void setShopDetail(ShopDetail shopDetail) {
        this.shopDetail = shopDetail;
    }

    public List<ShortShop> getShortShops() {
        return shortShops;
    }

    public void setShortShops(List<ShortShop> shortShops) {
        this.shortShops = shortShops;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public boolean isLoadComplete() {
        return isLoadComplete;
    }

    public void setLoadComplete(boolean loadComplete) {
        isLoadComplete = loadComplete;
    }
}
