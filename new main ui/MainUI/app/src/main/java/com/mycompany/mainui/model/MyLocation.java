package com.mycompany.mainui.model;

/**
 * Created by Dell on 4/10/2016.
 */
public class MyLocation {

    private float longitude;
    private float latitude;

    public MyLocation(float longitude, float latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }


}
