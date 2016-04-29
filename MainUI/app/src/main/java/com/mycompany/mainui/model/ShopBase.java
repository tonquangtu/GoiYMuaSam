package com.mycompany.mainui.model;

import java.io.Serializable;

/**
 * Created by Dell on 4/10/2016.
 */
public class ShopBase implements Serializable {

    private String idShop;
    private String shopName;
    private String address;
    private int numOfView;
    private float sales;
    private String note;

    public int getNumOfView() {
        return numOfView;
    }

    public void setNumOfView(int numOfView) {
        this.numOfView = numOfView;
    }

    public String getIdShop() {
        return idShop;
    }

    public void setIdShop(String idShop) {
        this.idShop = idShop;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public float getSales() {
        return sales;
    }

    public void setSales(float sales) {
        this.sales = sales;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
