package com.mycompany.mainui.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dell on 5/1/2016.
 */
public class SuggestionDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Suggest.db";
    public static final String TABLE_SUGGEST = "suggestions";
    public static final String COLUMN_NAME = "suggest";


    public static final String SQL_CREATE_TABLE = "CREATE TABLE " +
            TABLE_SUGGEST + "(" + COLUMN_NAME + " TEXT NOT NULL)";

    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_SUGGEST;
    private static SQLiteDatabase dbWriter = null;
    private static SQLiteDatabase dbReader = null;
    private static SuggestionDbHelper suggestionDbHelper = null;


    private SuggestionDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public static SQLiteDatabase getDbWriter(Context context) {
        if(dbWriter == null) {
            if(suggestionDbHelper == null) {
                suggestionDbHelper = new SuggestionDbHelper(context);
            }
            dbWriter = suggestionDbHelper.getWritableDatabase();
        }
        return dbWriter;
    }

    public static SQLiteDatabase getDbReader(Context context) {
        if(dbReader == null) {
            if(suggestionDbHelper == null) {
                suggestionDbHelper = new SuggestionDbHelper(context);
            }
            dbReader = suggestionDbHelper.getReadableDatabase();
        }
        return dbReader;
    }
}
