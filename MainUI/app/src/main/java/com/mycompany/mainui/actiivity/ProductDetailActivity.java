package com.mycompany.mainui.actiivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mycompany.mainui.R;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.ShortProduct;

public class ProductDetailActivity extends AppCompatActivity {

    ProductDetailActivity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ConfigData.ARG_PACKAGE);
        ShortProduct shortProduct = (ShortProduct)bundle.getSerializable(ConfigData.ARG_SHORT_PRODUCT);


    }
}
