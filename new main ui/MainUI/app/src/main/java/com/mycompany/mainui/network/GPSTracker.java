package com.mycompany.mainui.network;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.mycompany.mainui.model.InfoAccount;

/**
 * Created by Dell on 4/19/2016.
 */
public class GPSTracker implements LocationListener {

    private Context mContext;
    private LocationManager locationManager;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5000;// 5 kilometer

    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 60; // 60 minute


    public GPSTracker(Context context) {
        this.mContext = context;
        locationManager = (LocationManager)mContext.getSystemService(mContext.LOCATION_SERVICE);

    }

    public Location updateLocation() {

        Location location = null;
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        boolean permission = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if(permission) {

            if(isGPSEnabled) {

                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        GPSTracker.this);
                location = locationManager.getLastKnownLocation(
                        LocationManager.GPS_PROVIDER);

            }else if(isNetworkEnabled) {

                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        GPSTracker.this
                );
                location = locationManager.getLastKnownLocation(
                        LocationManager.NETWORK_PROVIDER);
            }
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            SharedPreferences pre = mContext.getSharedPreferences(
                    InfoAccount.FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pre.edit();
            editor.putFloat(InfoAccount.LONGITUDE, (float) location.getLongitude());
            editor.putFloat(InfoAccount.LATITUDE, (float) location.getLatitude());
            editor.apply();
        }
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


//    private class UpdateLocationAsyncTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//
//
//
//
//
//        }
//    }

    public boolean isGetLocation() {
        boolean isGetLocation = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return isGetLocation;
    }



}
