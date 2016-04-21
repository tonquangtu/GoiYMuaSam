package com.mycompany.mainui.model;

import java.util.List;

/**
 * Created by Dell on 4/14/2016.
 */
public class HotProduct {

    private String hotProductCatalogName;

    private List<ShortProduct> productList;

    public String getHotProductCatalogName() {
        return hotProductCatalogName;
    }

    public void setHotProductCatalogName(String hotProductCatalogName) {
        this.hotProductCatalogName = hotProductCatalogName;
    }

    public List<ShortProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<ShortProduct> productList) {
        this.productList = productList;
    }
}
