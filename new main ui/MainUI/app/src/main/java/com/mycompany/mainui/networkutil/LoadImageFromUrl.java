package com.mycompany.mainui.networkutil;

import android.content.Context;
import android.widget.ImageView;

import com.mycompany.mainui.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Dell on 4/10/2016.
 */
public class LoadImageFromUrl {

    public static void loadImageFromURL(String url,
                                        ImageView imageView, Context context) {

//        String url2 = "http://192.168.43.47/Server_StartUp/image/image_ad_catalog/gacon.jpg";
//        Picasso picasso = ConfigPicasso.getPicasso(context);
//        picasso.load(url)
//                .placeholder(R.drawable.place_holder)
//                .error(R.drawable.error)
//                .into(imageView);
////

        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.home_df)
                .into(imageView);
        System.out.println("url : " + url);



    }

    public static void loadImageFromResourse(int idRes,ImageView imageView, Context context) {
        Picasso.with(context)
                .load(idRes)
                .into(imageView);
    }

}
