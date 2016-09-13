package com.mycompany.mainui.network;

import android.app.Activity;

import com.mycompany.mainui.model.RequestId;
import com.mycompany.mainui.model.ResponseFromServerData;
import com.mycompany.mainui.networkutil.ConnectServer;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dell on 17-May-16.
 */
public class Account {

    boolean isLoginComplete;
    ResponseFromServerData loginResult;

    public Account() {
        isLoginComplete = false;
        loginResult = null;
    }


    public void loginNormal(Activity activity, String username, String pass) {

        isLoginComplete = false;
        int typeLoginNormal = 0;
        Map<String, String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_LOGIN);
        tag.put(RequestId.TAG_USERNAME, username);
        tag.put(RequestId.TAG_PASSWORD, pass);
        tag.put(RequestId.TAG_LOGIN_TYPE, typeLoginNormal + "");

        Call<ResponseFromServerData> call = ConnectServer.getResponseAPI().callLogin(tag);

        call.enqueue(new Callback<ResponseFromServerData>() {
            @Override
            public void onResponse(Call<ResponseFromServerData> call, Response<ResponseFromServerData> response) {

                if(response.isSuccessful()) {
                    loginResult = response.body();
                }
                isLoginComplete = true;
            }

            @Override
            public void onFailure(Call<ResponseFromServerData> call, Throwable t) {
                isLoginComplete = true;
            }
        });
    }

    public void loginFacebook(String username, String pass) {

        int typeLoginFace = 1;
        Map<String, String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_LOGIN);
        tag.put(RequestId.TAG_USERNAME, username);
        tag.put(RequestId.TAG_PASSWORD, pass);
        tag.put(RequestId.TAG_LOGIN_TYPE, typeLoginFace + "");

        Call<ResponseFromServerData> call = ConnectServer.getResponseAPI().callLogin(tag);
        call.enqueue(new Callback<ResponseFromServerData>() {
            @Override
            public void onResponse(Call<ResponseFromServerData> call, Response<ResponseFromServerData> response) {
            }

            @Override
            public void onFailure(Call<ResponseFromServerData> call, Throwable t) {
            }
        });
    }



    public boolean isLoginComplete() {
        return isLoginComplete;
    }

    public ResponseFromServerData getLoginResult() {
        return loginResult;
    }
}
