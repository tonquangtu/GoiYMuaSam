package com.mycompany.mainui.map;

import java.io.Serializable;

/**
 * Created by Mr.T on 5/18/2016.
 */
public class InfoWindow implements Serializable {
    private String name;
    private String address;
    private String image;
    private double lat;
    private double lng;

    public InfoWindow(String name, String address, String image, double lat, double lng){
        this.name = name;
        this.address = address;
        this.image = image;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}



