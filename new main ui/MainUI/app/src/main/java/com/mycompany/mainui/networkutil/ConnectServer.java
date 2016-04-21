package com.mycompany.mainui.networkutil;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dell on 4/7/2016.
 */
public class ConnectServer {


    private static ResponseFromServerAPI responseAPI = null;
    public static final String LINK = "http://192.168.43.47/Server_StartUp/";
    public static final int CONNECTION_TIMEOUT = 15 * 1000; //15s
    public static final int READ_TIMEOUT = 15 * 1000; //15s

    public static ResponseFromServerAPI getResponseAPI() {

        if (responseAPI == null) {

            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl(LINK);
            builder.addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            responseAPI = retrofit.create(ResponseFromServerAPI.class);
        }
        return responseAPI;
    }

}
