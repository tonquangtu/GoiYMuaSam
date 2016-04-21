package com.mycompany.mainui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.T on 4/16/2016.
 */
public class HotProductAndCatalog {

    private List<Object> list;

    public HotProductAndCatalog(List<HotProduct> hotProducts) {

        if(hotProducts != null ) {
            list = new ArrayList<>();
            for(HotProduct hotProduct : hotProducts) {

                list.add(hotProduct.getHotProductCatalogName());
                list.addAll(hotProduct.getProductList());
            }
        }

    }

    public List<Object> getList() {
        return list;
    }
}
