package com.mycompany.mainui.controller3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mycompany.mainui.actiivity.ProductDetailActivity;
import com.mycompany.mainui.actiivity.ProductListInSalesActivity;
import com.mycompany.mainui.actiivity.ShopDetailActivity;
import com.mycompany.mainui.actiivity.ShopListInSalesActivity;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.model.ShortShop;
import com.mycompany.mainui.networkutil.StatusInternet;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell on 4/20/2016.
 */
public class SalesController implements Serializable{

    public void handleItemOnClick(Activity activity, ArrayList listData, int position) {

        if(StatusInternet.isInternet(activity)) {
            Object obj = listData.get(position);
            Intent intent;
            Bundle bundle = new Bundle();
            if(position == 0) {
                // handle view more shop
                intent = new Intent(activity, ShopListInSalesActivity.class);


            }else if(obj instanceof String) {

                ArrayList<ShortProduct> shortProducts = new ArrayList<>();
                int size = listData.size();
                for(int i = position + 1; i < size; i++) {
                    shortProducts.add((ShortProduct) listData.get(i));
                }
                bundle.putSerializable(ConfigData.ARG_SALES_PRODUCT_LIST, shortProducts);
                intent = new Intent(activity, ProductListInSalesActivity.class);

            }else if(obj instanceof ShortShop) {

                ShortShop shortShop = (ShortShop)obj;
                intent = new Intent(activity, ShopDetailActivity.class);
                bundle.putString(ConfigData.ARG_ID_SHOP, shortShop.getIdShop());
                bundle.putString(ConfigData.ARG_SHOP_NAME, shortShop.getShopName());

            }else {
                ShortProduct shortProduct = (ShortProduct)obj;
                intent = new Intent(activity, ProductDetailActivity.class);
                bundle.putString(ConfigData.ARG_ID_PRODUCT, shortProduct.getIdProduct());
                bundle.putString(ConfigData.ARG_PRODUCT_NAME, shortProduct.getProductName());
            }

            intent.putExtra(ConfigData.ARG_PACKAGE, bundle);
            activity.startActivity(intent);
        }

    }
}
