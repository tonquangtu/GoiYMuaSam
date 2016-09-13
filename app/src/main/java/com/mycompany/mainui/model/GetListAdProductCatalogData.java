package com.mycompany.mainui.model;

import java.util.List;

/**
 * Created by Dell on 4/10/2016.
 */
public class GetListAdProductCatalogData extends ResponseFromServerData {

    private List<AdProductCatalog> adProductCatalogList;

    public List<AdProductCatalog> getAdProductCatalogList() {
        return adProductCatalogList;
    }

    public void setAdProductCatalogList(List<AdProductCatalog> adProductCatalogList) {
        this.adProductCatalogList = adProductCatalogList;
    }
}
