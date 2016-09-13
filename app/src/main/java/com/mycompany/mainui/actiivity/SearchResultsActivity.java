package com.mycompany.mainui.actiivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mycompany.mainui.R;

public class SearchResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        String query = "";
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);

        }else if(Intent.ACTION_VIEW.equals(intent.getAction())) {
            query = intent.getDataString();

            // query o day chu y. no duoc noi voi 1 doan o dau , doan o dau nay duoc mo ta trong file xml searchable
        }

        Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
    }
}
