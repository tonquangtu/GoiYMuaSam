package com.mycompany.mainui.dataaccess;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mycompany.mainui.model.AdProductCatalog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 4/12/2016.
 */
public class DatabaseUtil {


    public static ArrayList<AdProductCatalog> getAllAdProductCatalog() {

        Cursor cursor = DatabaseConnect.getConnect().
                rawQuery(SqlSatement.GET_ALL_AD_PRODUCT_CATALOG, null);

        cursor.moveToPosition(-1);
        ArrayList<AdProductCatalog> adProductCatalogs = new ArrayList<>();
        while(cursor.moveToNext()) {

            String adProductCatalogName = cursor.getString(0);
            String imageURl = cursor.getString(1);
            adProductCatalogs.add(new AdProductCatalog(adProductCatalogName, imageURl));

        }

        return adProductCatalogs;
    }

    public static boolean updateNewAdProductCatalog(List<AdProductCatalog> adProductCatalogs) {

        if(adProductCatalogs != null) {
            boolean isDelete = deleteAllAdProductCatalog();
            boolean isInsert = insertAllAdProductCatalog(adProductCatalogs);

            return isDelete && isInsert;
        }

        return false;

    }

    private static boolean deleteAllAdProductCatalog() {

        int numDelete = DatabaseConnect.getConnect().delete(
                SqlSatement.TABLE_AD_PRODUCT_CATALOG_NAME,
                null,
                null
        );
        return numDelete != 0;
    }

    private static boolean insertAllAdProductCatalog(List<AdProductCatalog> adProductCatalogs) {

        SQLiteDatabase database = DatabaseConnect.getConnect();
        ContentValues value;
        String columnName0 = "ad_product_catalog_name";
        String columnName1 = "imageURL";
        int count = 0;

        for (AdProductCatalog temp : adProductCatalogs) {

            value = new ContentValues();
            value.put(columnName0, temp.getAdProductCatalogName());
            value.put(columnName1, temp.getImageURL());
            if(database.insert(SqlSatement.TABLE_AD_PRODUCT_CATALOG_NAME, null, value) != -1) {
                count ++;
            };

        }
        if(count == adProductCatalogs.size())
            return true;

        return false;
    }

}
