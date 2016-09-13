package com.mycompany.mainui.model;

import java.io.Serializable;

/**
 * Created by Dell on 4/10/2016.
 */
public class ProductBase implements Serializable {

    private String idProduct;
    private String productName;
    private float price;
    private float promotionPercent;
    private int numOfView;
    private String address;


    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPromotionPercent() {
        return promotionPercent;
    }

    public void setPromotionPercent(float promotionPercent) {
        this.promotionPercent = promotionPercent;
    }

    public int getNumOfView() {
        return numOfView;
    }

    public void setNumOfView(int numOfView) {
        this.numOfView = numOfView;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNewPrice(){
        return (int)(price * (100 - promotionPercent)/100.0);
    }
}
