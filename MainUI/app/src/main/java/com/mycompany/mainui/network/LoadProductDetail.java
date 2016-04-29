package com.mycompany.mainui.network;

import android.app.Activity;

import com.mycompany.mainui.model.GetProductData;
import com.mycompany.mainui.model.InfoAccount;
import com.mycompany.mainui.model.ProductDetail;
import com.mycompany.mainui.model.RequestId;
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.model.ShortShop;
import com.mycompany.mainui.networkutil.ConnectServer;
import com.mycompany.mainui.view.DisplayNotification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dell on 4/21/2016.
 */
public class LoadProductDetail {

    private ProductDetail product;
    private ArrayList<ShortProduct> relationProducts;
    private ShortShop shortShop;
    private boolean isLoadComplete;
    private int like;

    public LoadProductDetail() {

        product = new ProductDetail();
        relationProducts = new ArrayList<>();
        shortShop = new ShortShop();
        isLoadComplete = false;
        like = 0;
    }

    public void loadProduct(Activity activity, String idProduct) {

        isLoadComplete = false;
        Map<String , String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_GET_PRODUCT);
        tag.put(RequestId.TAG_ID_PRODUCT, idProduct);
        tag.put(RequestId.TAG_USERNAME, InfoAccount.getUsername(activity));

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

//    public void sentLikeRequest(Activity activity, int like) {
//
//        Map<String, String> tag = new HashMap<>();
//        tag.put(RequestId.ID, RequestId.ID_LIKE_PRODUCT_REQUEST);
//        tag.put(RequestId.TAG_ID_PRODUCT, product.getIdProduct());
//        tag.put(RequestId.TAG_USERNAME, InfoAccount.getUsername(activity));
//        tag.put(RequestId.TAG_LIKE_PRODUCT, like + "");
//
//        Call<ResponseFromServerData> call = ConnectServer
//                .getResponseAPI().callLikeProduct(tag);
//        call.enqueue(new Callback<ResponseFromServerData>() {
//            @Override
//            public void onResponse(Call<ResponseFromServerData> call, Response<ResponseFromServerData> response) {
//
//                if (response != null && response.isSuccessful()) {
//                    ResponseFromServerData data = response.body();
//                    if (data != null || data.getSuccess() != RequestId.SUCCESS) {
//                        productDetailView.changeStatusLikeButton();
//                    }
//                }else {
//                    productDetailView.changeStatusLikeButton();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseFromServerData> call, Throwable t) {
//                productDetailView.changeStatusLikeButton();
//            }
//        });
//
//
//
//    }





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
}
