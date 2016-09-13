package com.mycompany.mainui.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Dell on 5/1/2016.
 */
public class SuggestionDbUtil {

    public static void addRowSuggestion(Context context, String suggestion) {

        SQLiteDatabase db = SuggestionDbHelper.getDbWriter(context);
        ContentValues values = new ContentValues();
        values.put(SuggestionDbHelper.COLUMN_NAME, suggestion);

        db.insert(SuggestionDbHelper.TABLE_SUGGEST, null, values);
    }

    public static ArrayList<String> getSuggestions(Context context, String world, int limit) {

        SQLiteDatabase db = SuggestionDbHelper.getDbReader(context);
        String columnName = SuggestionDbHelper.COLUMN_NAME;
        String sql = "SELECT " + columnName + " FROM "
                + SuggestionDbHelper.DATABASE_NAME + " WHERE "
                + columnName + " like "  + "?";
        String [] args = new String [1];
        args[0] = "%" + world + "%";

        Cursor cursor = db.rawQuery(sql, args);
        ArrayList<String> suggestions = new ArrayList<>();
        int count = 0;
        while (!cursor.isAfterLast() && count < limit) {
            suggestions.add(cursor.getString(0));
            cursor.moveToNext();
            count ++;
        }

        cursor.close();
        return suggestions;
    }

    public static void deleteAll(Context context) {

        SQLiteDatabase db = SuggestionDbHelper.getDbWriter(context);
        db.delete(SuggestionDbHelper.TABLE_SUGGEST,null, null );
    }

    public static ArrayList<String> getAllSuggestion(Context context) {

        SQLiteDatabase db = SuggestionDbHelper.getDbReader(context);
        String columnName = SuggestionDbHelper.COLUMN_NAME;
        String sql = "SELECT " + columnName + " FROM " + SuggestionDbHelper.TABLE_SUGGEST;
        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<String> totalSuggest = new ArrayList<>();
        cursor.moveToPosition(-1);
        while(cursor.moveToNext()) {
            totalSuggest.add(cursor.getString(0));
        }
        cursor.close();
        return totalSuggest;
    }
}
