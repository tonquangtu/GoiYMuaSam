package com.mycompany.mainui.model;

import java.util.List;

/**
 * Created by Dell on 4/14/2016.
 */
public class GetHotProductData extends ResponseFromServerData {

    private List<HotProduct> hotProductList;
    private int start;

    public List<HotProduct> getHotProductList() {
        return hotProductList;
    }

    public void setHotProductList(List<HotProduct> hotProductList) {
        this.hotProductList = hotProductList;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
