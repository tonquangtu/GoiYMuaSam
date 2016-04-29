package com.mycompany.mainui.controller3;


import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.mycompany.mainui.actiivity.ShopDetailActivity;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.InfoAccount;
import com.mycompany.mainui.model.MyLocation;
import com.mycompany.mainui.model.ShortShop;
import com.mycompany.mainui.network.GPSTracker;
import com.mycompany.mainui.network.LoadShopListSuggestion;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell on 4/18/2016.
 */
public class ShopListSuggestionController implements Serializable{

    public static final float DISTANCE = 2000; // 2000 meter
    private LoadShopListSuggestion loadSuggestion;

    public ShopListSuggestionController(LoadShopListSuggestion loadSuggestion) {
        this.loadSuggestion = loadSuggestion;
    }


    public void handleOnCLickItem(Activity activity, ShortShop shortShop) {

        Intent intent = new Intent(activity, ShopDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConfigData.ARG_SHOP_SUGGESTION, shortShop);
        intent.putExtra(ConfigData.ARG_PACKAGE_SHOP_SUGGESTION, bundle);
        activity.startActivity(intent);
    }

    public void handleOnClickCaptureLocation(Activity activity, int option) {

        // lay vi tri bang gps
        if(option == ConfigData.OPTION_GPS) {
            GPSTracker gpsTracker = new GPSTracker(activity);
            if(gpsTracker.isGetLocation()) {
                Location newLocation = gpsTracker.updateLocation();

                if(newLocation != null) {

                    MyLocation mOldLocation = InfoAccount.getLocation(activity);

                    Location oldLocation = new Location("old_location");
                    oldLocation.setLongitude(mOldLocation.getLongitude());
                    oldLocation.setLatitude(mOldLocation.getLatitude());
                    double distance = newLocation.distanceTo(oldLocation);
                    // dang lam gio o day
                    if(distance >= DISTANCE) {

                        //update list shop
                        ArrayList<ShortShop> shopList = loadSuggestion.getShopList();
                        while(shopList.size() > 0) {
                            shopList.remove(shopList.size() -1);
                        }

                  //      loadSuggestion.resetLoadShopSuggestion(activity, new MyLocation(newLocation.getLongitude(), newLocation.getLatitude()));



                    }



                }


            }else {
                // turn on GPS

            }


        }else {

            // lay vi tri bang cach chon toa do thong qua map
        }


    }




}
