package com.mycompany.mainui.controller3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mycompany.mainui.actiivity.LoadingActivity;
import com.mycompany.mainui.model.ShortProduct;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Dell on 4/20/2016.
 */
public class HotListController implements Serializable {

    public static final String ARG_HOT_CATALOG = "ARG_HOT_CATALOG";
    public static final String ARG_SHORT_PRODUCT = "ARG_SHORT_PRODUCT";
    public static final String ARG_PACKAGE = "ARG_PACKAGE";

    // event when user click item
    public void handle(Activity activity, ArrayList hotObjects, int position) {

        Object objItem = hotObjects.get(position);
        if(objItem != null) {
           Intent intent = new Intent(activity, LoadingActivity.class);
           Bundle bundle = new Bundle();
           if(objItem instanceof String) {

               String hotCatalog = (String)objItem;
               bundle.putString(ARG_HOT_CATALOG, hotCatalog);
           }else  {

               ShortProduct shortProduct = (ShortProduct)objItem;
               bundle.putSerializable(ARG_SHORT_PRODUCT, shortProduct);
           }
           intent.putExtra(ARG_PACKAGE, bundle);
           activity.startActivity(intent);
       }
    }





}
