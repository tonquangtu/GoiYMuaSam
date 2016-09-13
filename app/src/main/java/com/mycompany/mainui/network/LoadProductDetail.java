package com.mycompany.mainui.network;

import android.app.Activity;

import com.mycompany.mainui.model.GetProductData;
import com.mycompany.mainui.model.InfoAccount;
import com.mycompany.mainui.model.ProductDetail;
import com.mycompany.mainui.model.RequestId;
import com.mycompany.mainui.model.ResponseFromServerData;
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.model.ShortShop;
import com.mycompany.mainui.networkutil.ConnectServer;
import com.mycompany.mainui.view.DisplayNotification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dell on 4/21/2016.
 */
public class LoadProductDetail implements Serializable{

    private ProductDetail product;
    private ArrayList<ShortProduct> relationProducts;
    private ShortShop shortShop;
    private boolean isLoadComplete;
    private int like;
    private String phone;

    public LoadProductDetail() {

        product = null;
        relationProducts = new ArrayList<>();
        shortShop = null;
        isLoadComplete = false;
        like = 0;
    }

    public void loadProduct(Activity activity, String idProduct) {

        isLoadComplete = false;
        Map<String , String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_GET_PRODUCT);
        tag.put(RequestId.TAG_ID_PRODUCT, idProduct);
        tag.put(RequestId.TAG_USERNAME, InfoAccount.getUsername(activity));
        tag.put(RequestId.TAG_CITY, InfoAccount.getCity(activity));

        loadProductFromServer(activity, tag);
    }


    private void loadProductFromServer(final Activity activity, Map<String, String> tag) {

        Call<GetProductData> call = ConnectServer.getResponseAPI().callGetProduct(tag);
        call.enqueue(new Callback<GetProductData>() {
            @Override
            public void onResponse(Call<GetProductData> call, Response<GetProductData> response) {
                if(response != null && response.isSuccessful()) {

                    GetProductData data = response.body();
                    if(data != null) {
                        if(data.getSuccess() == RequestId.SUCCESS) {

                            product = data.getProduct();
                            like = data.getLike();
                            shortShop = data.getShop();
                            relationProducts.addAll(data.getRelationProducts());
                            phone = data.getPhone();
                        }else {
                            DisplayNotification.toast(activity, data.getMessage());
                        }
                    }
                }
                isLoadComplete = true;
            }

            @Override
            public void onFailure(Call<GetProductData> call, Throwable t) {
                DisplayNotification.errorLoaddata(activity, t.getMessage());
                isLoadComplete = true;
            }
        });
    }

    public void sentUserViewProduct( String username, String idProduct) {

        Map<String , String> tag = new HashMap<>();
        tag.put(RequestId.ID,RequestId.ID_USER_VIEW_PRODUCT);
        tag.put(RequestId.TAG_USERNAME, username);
        tag.put(RequestId.TAG_ID_PRODUCT, idProduct);
        Call<ResponseFromServerData> call = ConnectServer.getResponseAPI().callUserViewProduct(tag);
        call.enqueue(new Callback<ResponseFromServerData>() {
            @Override
            public void onResponse(Call<ResponseFromServerData> call, Response<ResponseFromServerData> response) {

            }

            @Override
            public void onFailure(Call<ResponseFromServerData> call, Throwable t) {

            }
        });
    }

    public void sentLike( String  username, String idProduct, int like) {

        Map<String, String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_USER_LIKE_PRODUCT);
        tag.put(RequestId.TAG_USERNAME, username);
        tag.put(RequestId.TAG_ID_PRODUCT, idProduct);
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


    public ProductDetail getProduct() {
        return product;
    }

    public ArrayList<ShortProduct> getRelationProducts() {
        return relationProducts;
    }

    public ShortShop getShortShop() {
        return shortShop;
    }

    public boolean isLoadComplete() {
        return isLoadComplete;
    }

    public int getLike() {
        return like;
    }

    public String getPhone() {
        return phone;
    }
}
