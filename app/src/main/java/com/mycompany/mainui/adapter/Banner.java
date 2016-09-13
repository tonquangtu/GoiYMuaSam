package com.mycompany.mainui.adapter;

import java.util.ArrayList;

/**
 * Created by Mr.T on 5/17/2016.
 */
public class Banner {
    private String title;
    private int image;

    public Banner(){};

    public Banner(String title, int image){
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public static ArrayList<Banner> createBanner(String[] title, int[] image){
        ArrayList<Banner> result = new ArrayList<>();

        for(int i = 0; i < image.length; i++){
            Banner temp = new Banner(title[i], image[i]);
            result.add(temp);
        }
        return result;
    }
}
