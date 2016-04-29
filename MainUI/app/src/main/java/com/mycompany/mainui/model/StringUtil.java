package com.mycompany.mainui.model;

/**
 * Created by Dell on 4/14/2016.
 */
public class StringUtil {

    public static String convertURL(String path) {
        return path.replace(",", "/");
    }
}
