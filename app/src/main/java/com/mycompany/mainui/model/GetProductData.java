package com.mycompany.mainui.model;

import java.util.List;

/**
 * Created by Dell on 4/10/2016.
 */
public class GetProductData extends ResponseFromServerData {

    private ProductDetail product;
    private ShortShop shop;
    private List<ShortProduct> relationProducts;
    private int like;
    private String phone;

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public ProductDetail getProduct() {
        return product;
    }

    public void setProduct(ProductDetail product) {
        this.product = product;
    }

    public ShortShop getShop() {
        return shop;
    }

    public void setShop(ShortShop shop) {
        this.shop = shop;
    }

    public List<ShortProduct> getRelationProducts() {
        return relationProducts;
    }

    public void setRelationProducts(List<ShortProduct> relationProducts) {
        this.relationProducts = relationProducts;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
