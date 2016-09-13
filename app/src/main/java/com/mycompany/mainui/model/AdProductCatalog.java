package com.mycompany.mainui.model;

import java.io.Serializable;

/**
 * Created by Dell on 4/10/2016.
 */
public class AdProductCatalog implements Serializable {

    private String adProductCatalogName;
    private String imageURL;

    public AdProductCatalog(String adProductCatalogName, String imageURL) {
        this.adProductCatalogName = adProductCatalogName;
        this.imageURL = imageURL;
    }


    public String getAdProductCatalogName() {
        return adProductCatalogName;
    }

    public void setAdProductCatalogName(String adProductCatalogName) {
        this.adProductCatalogName = adProductCatalogName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


}
