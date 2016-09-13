package com.mycompany.mainui.map;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Mr.T on 5/12/2016.
 */
public class MyLocationListener implements LocationListener{
    private String myLocation = "";

    public String getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(String myLocation) {
        this.myLocation = myLocation;
    }


    @Override
    public void onLocationChanged(Location location) {
        myLocation = location.getLatitude() + "" + location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
