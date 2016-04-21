package com.mycompany.mainui.model;

import java.util.List;

/**
 * Created by Dell on 4/10/2016.
 */
public class GetShopData extends ResponseFromServerData{

    private ShopDetail shop;
    private List<ShortShop> relationShops;
    private int like;

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public ShopDetail getShop() {
        return shop;
    }

    public void setShop(ShopDetail shop) {
        this.shop = shop;
    }

    public List<ShortShop> getRelationShops() {
        return relationShops;
    }

    public void setRelationShops(List<ShortShop> relationShops) {
        this.relationShops = relationShops;
    }
}
