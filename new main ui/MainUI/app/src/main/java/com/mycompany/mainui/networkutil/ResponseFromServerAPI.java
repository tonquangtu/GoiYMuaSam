package com.mycompany.mainui.networkutil;


import com.mycompany.mainui.model.CreateAccountData;
import com.mycompany.mainui.model.GetCommentData;
import com.mycompany.mainui.model.GetHotProductData;
import com.mycompany.mainui.model.GetListAdProductCatalogData;
import com.mycompany.mainui.model.GetListProductData;
import com.mycompany.mainui.model.GetProductData;
import com.mycompany.mainui.model.GetSalesData;
import com.mycompany.mainui.model.GetShopData;
import com.mycompany.mainui.model.GetShopListSuggestionData;
import com.mycompany.mainui.model.LoginData;
import com.mycompany.mainui.model.ResponseFromServerData;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 *  //dang lam do, dang viet cac interface de dinh nghia cac loi goi server
 // tuong ung moi loi goi se co 1 lop trong model
 // tuong ung de chua du lieu tra ve tu server
 // va tuong ung se co 1 lop o controller de bat lay su kien
 * define methods to call server follow protocol
 * Created by Dell on 4/9/2016.
 */
public interface ResponseFromServerAPI {

    //  chung ta can 1 interface de dinh nghia cac loi goi
    // Chung ta can model de chua du lieu truyen ve
    // Chung ta can cac controller de bat lay su kien ket qua tra ve

    /** feature 1
     * call login to server
     * @param id : id of tag
     * @param username : username of user
     * @param pass : password of user
     * @param type : type of user
     * @return : a instance of Call<LoginData> contain data sent from server
     */
    @POST(ServerLink.LINK_LOGIN)
    Call<LoginData> callLogin(@Field("id") String id,
                              @Field("username") String username,
                              @Field("password") String pass,
                              @Field("type") int type);

    /**
     * feature 2
     * require call create account to server
     * @param id : id of tag
     * @param username : username of user
     * @param pass : password of user
     * @param type : type of account
     * @param lock : figure account be locked or not ?
     * @return : Call<CreateAccountData> contain data sent from server.
     */
    @POST(ServerLink.LINK_LOGIN)
    Call<CreateAccountData> callCreateAccount(@Field("id") String id,
                                              @Field("username") String username,
                                              @Field("password") String pass,
                                              @Field("type") int type,
                                              @Field("lock") int lock);


    /**
     * feature 3
     * call get info product from server
     * @param tag : contain data sent to server
     * @return : a instance of Call<GetProductData> contain info sent from server
     */
    @GET(ServerLink.LINK_LOGIN)
    Call<GetProductData> callGetProduct(@QueryMap Map<String, String> tag);


    /**
     * feature 4
     * call get shop
     * @param tag : containt data sent to Server
     * @return : a instance of Call<GetListProductInShop>
     */
    @GET(ServerLink.LINK_LOGIN)
    Call<GetShopData> callGetShop(@QueryMap Map<String, String> tag);


    /** feature 5
     * call get list product in shop data
     * @param tag : contain data sent to server
     * @return : a
     */
    @GET(ServerLink.LINK_LOGIN)
    Call<GetListProductData> callGetProductListInShop(@QueryMap Map<String, String> tag);

    /**
     * feature 8
     * call get list ad product catalog
     * @param tag : contain data sent to server
     * @return : a instance of Call<GetListAdProductCatalogData>
     */
    @GET(ServerLink.LINK_LOGIN)
    Call<GetListAdProductCatalogData> callGetListAdProductCatalog(@QueryMap Map<String, String> tag);

    // lay ve danh sach cac san pham goi y
    @GET(ServerLink.LINK_LOGIN)
    Call<GetListProductData> callGetProductListSuggestion(@QueryMap Map<String, String> tag);

    // lay ve danh sach cac shop goi y
    @GET(ServerLink.LINK_LOGIN)
    Call<GetShopListSuggestionData> callGetShopListSuggestion(@QueryMap Map<String, String> tag);
//
    @GET(ServerLink.LINK_LOGIN)
    Call<GetHotProductData> callGetHotProduct(@QueryMap Map<String, String> tag);

    @GET(ServerLink.LINK_LOGIN)
    Call<GetListProductData> callProductListInAd(@QueryMap Map<String, String> tag);

    @GET(ServerLink.LINK_LOGIN)
    Call<ResponseFromServerData> callLikeProduct(@QueryMap Map<String, String> tag);

    @GET(ServerLink.LINK_LOGIN)
    Call<GetCommentData> callGetComments(@QueryMap Map<String, String> tag);

    @GET(ServerLink.LINK_LOGIN)
    Call<ResponseFromServerData> callSentComment(@QueryMap Map<String, String> tag);

    @GET(ServerLink.LINK_LOGIN)
    Call<GetListProductData> callGetProductListInHotCatalog(@QueryMap Map<String, String> tag);

    @GET(ServerLink.LINK_LOGIN)
    Call<GetSalesData> callGetSales(@QueryMap Map<String, String> tag);
}

