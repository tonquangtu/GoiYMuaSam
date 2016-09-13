package com.mycompany.mainui.networkutil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Dell on 4/12/2016.
 */
public class StatusInternet {

    public static boolean isInternet(Context context){
        ConnectivityManager cm = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if(info != null) {
            return true;
        }
        return false;
    }
}
