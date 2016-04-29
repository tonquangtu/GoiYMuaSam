package com.mycompany.mainui.view;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Dell on 4/15/2016.
 */
public class DisplayNotification {

    public static void displayNotifiNoNetwork(Activity context) {

        Toast.makeText(context, "No network, please check your network again",
                Toast.LENGTH_LONG).show();
    }

    public static void toast(Activity context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    public  static void notifiLogin(Activity context) {
        Toast.makeText(context,
                "Ban chua dang nhap, hay dang nhap de thuc hien chuc nang nay",
                Toast.LENGTH_LONG).show();
    }

    public static void errorLoaddata(Activity context) {
        Toast.makeText(context, "Khong the tai duoc du lieu", Toast.LENGTH_LONG).show();
    }

    public static void errorLoaddata(Activity context, String message) {
        Toast.makeText(context, "Khong the tai duoc du lieu : " + message, Toast.LENGTH_LONG).show();
    }



}
