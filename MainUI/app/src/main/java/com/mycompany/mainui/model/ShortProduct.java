package com.mycompany.mainui.model;

import java.io.Serializable;

/**
 * Created by Dell on 4/10/2016.
 */
public class ShortProduct extends ProductBase implements Serializable {

    private String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

}
