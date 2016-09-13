package com.mycompany.mainui.model;


import java.util.List;

/**
 * Created by Dell on 4/10/2016.
 */
public class ShopDetail extends ShopBase {

    private MyLocation location;
    private List<String> imageURLs;
    private int numOfLike;
    private String phone;

    public MyLocation getLocation() {
        return location;
    }

    public void setLocation(MyLocation location) {
        this.location = location;
    }

    public List<String> getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(List<String> imageURLs) {
        this.imageURLs = imageURLs;
    }

    public int getNumOfLike() {
        return numOfLike;
    }

    public void setNumOfLike(int numOfLike) {
        this.numOfLike = numOfLike;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
