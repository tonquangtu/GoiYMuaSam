package com.mycompany.mainui.controller3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mycompany.mainui.actiivity.ProductDetailActivity;
import com.mycompany.mainui.actiivity.ProductListInAdActivity;
import com.mycompany.mainui.model.AdProductCatalog;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.networkutil.StatusInternet;
import com.mycompany.mainui.view.DisplayNotification;

import java.io.Serializable;

/**
 * Created by Dell on 4/18/2016.
 */
public class ProductListSuggestionController implements Serializable {



    public void handleClickAdProductCatalog( Activity activity, AdProductCatalog adProductCatalog) {

        if(StatusInternet.isInternet(activity)) {
            Intent intent = new Intent(activity, ProductListInAdActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConfigData.ARG_AD_CATALOG, adProductCatalog);
            intent.putExtra(ConfigData.ARG_PACKAGE_AD_CATALOG, bundle);
            activity.startActivityForResult(intent, 200);

        }else {
            DisplayNotification.displayNotifiNoNetwork(activity);
        }
    }

    public void handleClickProductSuggestion(Activity activity, ShortProduct shortProduct) {

        if(StatusInternet.isInternet(activity)) {
            // do something
            Intent intent = new Intent(activity, ProductDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConfigData.ARG_SHORT_PRODUCT, shortProduct);
            intent.putExtra(ConfigData.ARG_PACKAGE, bundle);
            activity.startActivity(intent);
        }
    }






}
