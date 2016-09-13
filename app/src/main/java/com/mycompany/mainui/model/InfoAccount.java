package com.mycompany.mainui.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dell on 4/13/2016.
 */
public class InfoAccount {

    public  static final  String USERNAME = "username";
    public  static final String FILE_NAME = "infoAccount";
    public  static final String LOGIN = "isLogin";
    public  static final String DISTANCE = "distance";
    public  static final String CITY = "city";
    public  static final int BASE_DISTANCE = 8;
    public  static final String LONGITUDE = "longitude";
    public  static final String LATITUDE = "latitude";


    public static String getUsername(Context context) {
        SharedPreferences pre = context.
                getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        String username = pre.getString(USERNAME, "");
        return username;

    }

    public static boolean isLogin(Context context) {
        SharedPreferences pre = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return pre.getBoolean(LOGIN, false);
//        return false;
    }

    public static String getCity(Context context) {

        SharedPreferences pre = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return pre.getString(CITY, "");

    }

    public static int getDistance(Context context) {
        SharedPreferences pre = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return pre.getInt(DISTANCE, BASE_DISTANCE);
    }

    public static MyLocation getLocation(Context context) {
        SharedPreferences pre = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        float longitude = pre.getFloat(LONGITUDE, -1);
        float latitude = pre.getFloat(LATITUDE, -1);

        return new MyLocation(longitude, latitude);
    }

    public static void putLocation(Context context, MyLocation location) {
        SharedPreferences pre = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putFloat(LONGITUDE, location.getLocationY());
        editor.putFloat(LATITUDE, location.getLocationX());
        editor.commit();
    }

    public static void putCity(Context context, String city) {

        SharedPreferences pre = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putString(CITY, city);
        editor.commit();

    }

    public static void login(Context context, String username) {

        SharedPreferences pre = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putBoolean(LOGIN, true);
        editor.putString(USERNAME, username);
        editor.apply();
    }

    public static void logout(Context context) {

        SharedPreferences pre = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putBoolean(LOGIN, false);
        editor.apply();
    }




}
