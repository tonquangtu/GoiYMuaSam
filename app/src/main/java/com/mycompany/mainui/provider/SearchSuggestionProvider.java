package com.mycompany.mainui.provider;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import com.mycompany.mainui.dataaccess.SuggestionDbUtil;

import java.util.ArrayList;

/**
 * Created by Dell on 4/30/2016.
 */
public class SearchSuggestionProvider extends ContentProvider {

    ArrayList<String> suggestions = null;

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(final Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final MatrixCursor cursor = new MatrixCursor(
                new String[] {
                        BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA
                }
        );

        if(suggestions == null || suggestions.isEmpty()) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    suggestions = SuggestionDbUtil
                            .getAllSuggestion(getContext());
                }
            }).start();
        }

        if(suggestions != null) {
            String query = uri.getLastPathSegment().toLowerCase();
            int limit = Integer.parseInt(uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT));
            int size = suggestions.size();
            int count = size < limit ? size : limit;

            for(int i = 0; i < count ; i++) {

                String suggestion = suggestions.get(i);
                if(suggestion.toLowerCase().contains(query)) {
                    cursor.addRow(new Object[] {i, suggestion, i});
                }
            }
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
