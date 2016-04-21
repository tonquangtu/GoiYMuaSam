package com.mycompany.mainui.model;

import java.util.List;

/**
 * Created by Dell on 4/15/2016.
 */
public class GetSalesData extends ResponseFromServerData {

    private List<ShortProduct> productList;
    private List<ShortShop> shopList;

    public List<ShortProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<ShortProduct> productList) {
        this.productList = productList;
    }

    public List<ShortShop> getShopList() {
        return shopList;
    }

    public void setShopList(List<ShortShop> shopList) {
        this.shopList = shopList;
    }
}
