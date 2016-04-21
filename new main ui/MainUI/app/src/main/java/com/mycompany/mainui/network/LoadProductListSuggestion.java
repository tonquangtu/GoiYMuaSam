package com.mycompany.mainui.network;

import android.app.Activity;

import com.mycompany.mainui.model.AdProductCatalog;
import com.mycompany.mainui.model.GetListAdProductCatalogData;
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
 * Created by Dell on 4/17/2016.
 */
public class LoadProductListSuggestion implements Serializable {

    private ArrayList<ShortProduct> shortProducts;
    private ArrayList<AdProductCatalog> adProductCatalogs;
    private List<ShortProduct> loadMoreList;
    private int numCall;
    private boolean isLoadProductComplete = false;
    private boolean isLoadAdComplete = false;
    private long timeLoadMore = 0;
    private long tempTime = 0;

    public LoadProductListSuggestion() {
        numCall = 0;

        shortProducts = new ArrayList<>();
        adProductCatalogs = new ArrayList<>();
    }

    public void loadProductListSuggestion(Activity activity) {

        tempTime = System.currentTimeMillis();
        isLoadProductComplete = false;
        Map<String, String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_GET_PRODUCT_LIST_SUGGESTION);
        tag.put(RequestId.TAG_USERNAME, InfoAccount.getUsername(activity));
        tag.put(RequestId.TAG_CITY, "ha noi");
        tag.put(RequestId.TAG_NUM_CALL, numCall + "");

        loadProductListSuggestionFromServer(activity, tag);

    }


    public void loadAdProduct(Activity activity) {

        isLoadAdComplete = false;
        HashMap<String, String> tag = new HashMap<String,String>();
        tag.put(RequestId.ID, RequestId.ID_GET_LIST_AD_PRODUCT_CATALOG);

        loadListAdProductCatalogFromServer(activity, tag);

    }


    public void loadListAdProductCatalogFromServer(final  Activity activity,
            HashMap<String, String> tag ) {

        Call<GetListAdProductCatalogData> call = ConnectServer.getResponseAPI()
                .callGetListAdProductCatalog(tag);
        call.enqueue(new Callback<GetListAdProductCatalogData>() {

            @Override
            public void onResponse(Call<GetListAdProductCatalogData> call,
                                   Response<GetListAdProductCatalogData> response) {

                if (response != null) {
                    GetListAdProductCatalogData responseData = response.body();
                    if (responseData != null ) {

                        if(responseData.getSuccess() == RequestId.SUCCESS) {
                            adProductCatalogs.addAll(responseData.getAdProductCatalogList());
                        }else {
                            DisplayNotification.toast(activity, responseData.getMessage());
                        }
                    }
                }
                isLoadAdComplete = true;
            }

            @Override
            public void onFailure(Call<GetListAdProductCatalogData> call, Throwable t) {
                DisplayNotification.displayNotifiNoNetwork(activity);
                DisplayNotification.toast(activity, t.getMessage());
                isLoadAdComplete = true;
            }
        });
    }



    public void loadProductListSuggestionFromServer(final Activity activity, Map<String, String> tag) {

        Call<GetListProductData> call = ConnectServer.getResponseAPI()
                .callGetProductListSuggestion(tag);

        call.enqueue(new Callback<GetListProductData>() {
            @Override
            public void onResponse(Call<GetListProductData> call, Response<GetListProductData> response) {

                if(response != null) {
                    GetListProductData data = response.body();
                    if(data != null ) {
                        if(data.getSuccess() == RequestId.SUCCESS) {
                            if(data.getProductList().size() > 0) {
                                numCall ++;
                                loadMoreList = data.getProductList();

                            }
                        }else {
                            DisplayNotification.toast(activity, data.getMessage());
                        }

                    }
                }
                isLoadProductComplete = true;
                timeLoadMore = System.currentTimeMillis() - tempTime;
            }

            @Override
            public void onFailure(Call<GetListProductData> call, Throwable t) {
                DisplayNotification.displayNotifiNoNetwork(activity);
                DisplayNotification.toast(activity, t.getMessage());
                isLoadProductComplete = true;
                timeLoadMore = -1;
            }
        });
    }

    public ArrayList<ShortProduct> getShortProducts() {
        return shortProducts;
    }

    public void setShortProducts(ArrayList<ShortProduct> shortProducts) {
        this.shortProducts = shortProducts;
    }

    public ArrayList<AdProductCatalog> getAdProductCatalogs() {
        return adProductCatalogs;
    }

    public void setAdProductCatalogs(ArrayList<AdProductCatalog> adProductCatalogs) {
        this.adProductCatalogs = adProductCatalogs;
    }

    public boolean isLoadProductComplete() {
        return isLoadProductComplete;
    }

    public void setLoadProductComplete(boolean loadProductComplete) {
        isLoadProductComplete = loadProductComplete;
    }

    public boolean isLoadAdComplete() {
        return isLoadAdComplete;
    }

    public void setLoadAdComplete(boolean loadAdComplete) {
        isLoadAdComplete = loadAdComplete;
    }

    public long getTimeLoadMore() {
        return timeLoadMore;
    }

    public boolean addLoadMoreList() {
        if(loadMoreList != null) {
            loadMoreList.addAll(loadMoreList);
            loadMoreList = null;
            return true;
        }
        return false;
    }

}
