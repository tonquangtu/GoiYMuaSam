package com.mycompany.mainui.dataaccess;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Dell on 4/12/2016.
 */
public class DatabaseConnect {

    private static final String DATABASE_NAME = "database.db";
    private static SQLiteDatabase sqlite = null;

    private DatabaseConnect() {

    }

    public static SQLiteDatabase getConnect() {

        if(sqlite == null) {
            sqlite = SQLiteDatabase.openDatabase(DATABASE_NAME,
                    null, SQLiteDatabase.CREATE_IF_NECESSARY);
        }
        return sqlite;
    }

    public void close() {
        if(sqlite != null) {
            sqlite.close();
            sqlite = null;
        }
    }
}
