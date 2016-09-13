package com.mycompany.mainui.model;

import java.util.List;

/**
 * Created by Dell on 07-May-16.
 */
public class GetShopListData extends ResponseFromServerData {

    private List<ShortShop> shopList;

    public List<ShortShop> getShopList() {
        return shopList;
    }

    public void setShopList(List<ShortShop> shopList) {
        this.shopList = shopList;
    }
}
