package com.mycompany.mainui.model;

import java.util.List;

/**
 * Created by Dell on 4/10/2016.
 */
public class GetListProductData extends ResponseFromServerData {

    private List<ShortProduct> productList;

    public List<ShortProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<ShortProduct> productList) {
        this.productList = productList;
    }
}
