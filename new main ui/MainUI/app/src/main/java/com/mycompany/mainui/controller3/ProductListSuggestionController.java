package com.mycompany.mainui.controller3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mycompany.mainui.actiivity.LoadingActivity;
import com.mycompany.mainui.actiivity.ProductListInAdActivity;
import com.mycompany.mainui.model.AdProductCatalog;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.ShortProduct;

import java.io.Serializable;

/**
 * Created by Dell on 4/18/2016.
 */
public class ProductListSuggestionController implements Serializable {



    public void handleClickAdProductCatalog( Activity activity, AdProductCatalog adProductCatalog) {

        Intent intent = new Intent(activity, ProductListInAdActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConfigData.ARG_AD_CATALOG, adProductCatalog);
        intent.putExtra(ConfigData.ARG_PACKAGE_AD_CATALOG, bundle);
        activity.startActivity(intent);

    }

    public void handleClickProductSuggestion(Activity activity, ShortProduct shortProduct) {

        // do something
        Intent intent = new Intent(activity, LoadingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConfigData.ARG_PRODUCT_SUGGESTION, shortProduct);
        intent.putExtra(ConfigData.ARG_PACKAGE_PRODUCT_SUGGESTION, bundle);
        activity.startActivity(intent);
    }






}
