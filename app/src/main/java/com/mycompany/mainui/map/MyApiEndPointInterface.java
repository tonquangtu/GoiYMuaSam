package com.mycompany.mainui.map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mr.T on 4/28/2016.
 */
public interface MyApiEndPointInterface {
    @GET("/maps/api/directions/json")
    Call<DirectionResults> getJson(@Query("origin") String origin
            , @Query("destination") String destination
            , @Query("key") String key);
}
