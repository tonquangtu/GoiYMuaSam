package com.mycompany.mainui.model;

import java.util.List;

/**
 * Created by Dell on 4/13/2016.
 */
public class GetShopListSuggestionData extends ResponseFromServerData {

    private List<ShortShop> shopList;

    private int start;

    public List<ShortShop> getShopList() {
        return shopList;
    }

    public void setShopList(List<ShortShop> shopList) {
        this.shopList = shopList;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
