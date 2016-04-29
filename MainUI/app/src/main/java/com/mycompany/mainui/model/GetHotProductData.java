package com.mycompany.mainui.model;

import java.util.List;

/**
 * Created by Dell on 4/14/2016.
 */
public class GetHotProductData extends ResponseFromServerData {

    private List<HotProduct> hotProductList;

    public List<HotProduct> getHotProductList() {
        return hotProductList;
    }

    public void setHotProductList(List<HotProduct> hotProductList) {
        this.hotProductList = hotProductList;
    }

}
