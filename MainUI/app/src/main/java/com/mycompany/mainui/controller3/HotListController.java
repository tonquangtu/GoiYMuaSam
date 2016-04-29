package com.mycompany.mainui.controller3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mycompany.mainui.actiivity.ProductDetailActivity;
import com.mycompany.mainui.actiivity.ProductListInHotCatalogActivity;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.networkutil.StatusInternet;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Dell on 4/20/2016.
 */
public class HotListController implements Serializable {


    // event when user click item
    public void handle(Activity activity, ArrayList hotObjects, int position) {

        if(StatusInternet.isInternet(activity)) {
            Object objItem = hotObjects.get(position);
            if(objItem != null) {
                Intent intent;
                Bundle bundle = new Bundle();
                if(objItem instanceof String) {

                    ArrayList<ShortProduct> listProduct = new ArrayList<>();
                    String hotCatalog = (String)objItem;
                    for(int i = position + 1 ; i < hotObjects.size(); i++) {

                        Object obj = hotObjects.get(i);
                        if(obj instanceof ShortProduct) {
                            listProduct.add((ShortProduct)obj);
                        }else {
                            break;
                        }
                    }

                    intent = new Intent(activity, ProductListInHotCatalogActivity.class);

                    bundle.putString(ConfigData.ARG_HOT_CATALOG_NAME, hotCatalog);
                    bundle.putSerializable(ConfigData.ARG_HOT_PRODUCT_LIST, listProduct);



                }else  {

                    intent = new Intent(activity, ProductDetailActivity.class);
                    ShortProduct shortProduct = (ShortProduct)objItem;
                    bundle.putSerializable(ConfigData.ARG_SHORT_PRODUCT, shortProduct);
                }
                intent.putExtra(ConfigData.ARG_PACKAGE, bundle);
                activity.startActivity(intent);
            }
        }
    }


}
