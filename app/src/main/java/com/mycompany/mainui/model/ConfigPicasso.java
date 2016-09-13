package com.mycompany.mainui.model;

import android.app.Activity;

import com.squareup.picasso.Downloader;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by Dell on 4/13/2016.
 */
public class ConfigPicasso {
    final static long PICASSO_DISK_CACHE_SIZE = 1024 * 1024 * 50;
    final static int PICASSO_CACHE_SIZE = 1024 * 1024 * 10;

    private static Picasso picasso = null;

    private ConfigPicasso() {

    }

    public static Picasso getPicasso(Activity context) {
        if(picasso == null) {

            Downloader downloader = new OkHttpDownloader(context,
                    PICASSO_DISK_CACHE_SIZE);

            // xet lai kich thuoc cache
            picasso = new Picasso.Builder(context)
                    .downloader(downloader)
                    .memoryCache(new LruCache(PICASSO_CACHE_SIZE))
                    .build();



        }
        return  picasso;
    }

}
