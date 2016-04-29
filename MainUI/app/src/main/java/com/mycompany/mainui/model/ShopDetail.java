package com.mycompany.mainui.model;


import java.util.List;

/**
 * Created by Dell on 4/10/2016.
 */
public class ShopDetail extends ShopBase {

    private MyLocation myLocation;
    private List<String> imageURLs;
    private int numOfLike;

    public MyLocation getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(MyLocation myLocation) {
        this.myLocation = myLocation;
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
}
