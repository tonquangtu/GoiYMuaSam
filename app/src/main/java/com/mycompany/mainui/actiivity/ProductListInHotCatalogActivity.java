package com.mycompany.mainui.actiivity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.mycompany.mainui.R;
import com.mycompany.mainui.adapter.FilterAdapter;
import com.mycompany.mainui.dataaccess.SuggestionDbUtil;
import com.mycompany.mainui.fragment.FragmentProductListInHotCatalog;
import com.mycompany.mainui.fragment.LoadingFragment;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.network.LoadProductListInHotCatalog;

import java.util.ArrayList;

public class ProductListInHotCatalogActivity extends AppCompatActivity {

    ProductListInHotCatalogActivity mActivity;
    LoadProductListInHotCatalog loadProductListInHotCatalog;
    ArrayList<ShortProduct> listProduct;
    ActionBar actionBar;
    String hotCatalogName;
    Toolbar toolbar;
    View viewFilterPrice;
    View viewFilterSales;
    View viewFilterTool;
    View viewFilterDetail;
    ListView lvFilterDetail;
    Button btnUpdateFilter;
//    FilterAdapter adapterPrice;
//    FilterAdapter adapterSales;
    FilterAdapter adapter;
    int curPosFilterPrice ;
    int curPosFilterSales;


    int mToolContainerHeight;
    public static final int SPACE = 50;
    public View viewToolContainer;
//    FragmentFilterPrice fragmentFilterPrice;
//    FragmentFilterSales fragmentFilterSales;

    ArrayList<String> listTitleFilterPrice;
    ArrayList<String> listTitleFilterSales;
    ArrayList<String> listTitle;

    public static final String FILTER_PRICE_ID = "FILTER_PRICE";
    public static final String FILTER_SALES_ID = "FILTER_SALES";
    public static final int FILTER_PRICE = 1;
    public static final int FILTER_SALES = 2;
    public static final int NONE_FILTER = 0;
    int curChoice = NONE_FILTER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_in_catalog);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ConfigData.ARG_PACKAGE);
        hotCatalogName = bundle.getString(ConfigData.ARG_HOT_CATALOG_NAME);
        listProduct = (ArrayList<ShortProduct>) bundle.getSerializable(ConfigData.ARG_HOT_PRODUCT_LIST);

        mActivity = ProductListInHotCatalogActivity.this;
        loadProductListInHotCatalog = new LoadProductListInHotCatalog(listProduct);

        initToolbar();
        initFilter();

        int toolbarHeight = toolbar.getLayoutParams().height;
        int viewFilterToolheight = viewFilterTool.getLayoutParams().height;

        mToolContainerHeight = toolbarHeight + viewFilterToolheight;
        new LoadTask().execute();


    }

    public void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.product_list_in_hot_catalog_toolbar);
        setSupportActionBar(toolbar);
        setTitle(hotCatalogName);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        viewToolContainer = findViewById(R.id.tool_container_product_in_catalog);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
    }

    public void initFilter() {

        viewFilterDetail = this.findViewById(R.id.filter_detail);
        viewFilterTool = this.findViewById(R.id.filter_tool);
        viewFilterPrice = viewFilterTool.findViewById(R.id.view_filter_price);
        viewFilterSales = viewFilterTool.findViewById(R.id.view_filter_sales);

        lvFilterDetail = (ListView)findViewById(R.id.list_view_filter);
        lvFilterDetail.setItemsCanFocus(false);

        btnUpdateFilter = (Button)findViewById(R.id.btn_filter);

        viewFilterPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnClickFilterPrice();
            }
        });

        viewFilterSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnClickFilterSales();
            }
        });

        btnUpdateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnClickBtnUpdateFilter();
            }
        });

        curPosFilterPrice = -1;
        curPosFilterSales = -1;
        listTitleFilterPrice = new ArrayList<>();
        listTitleFilterSales = new ArrayList<>();
        listTitle = new ArrayList<>();

        //-----------------------------------------------
        listTitleFilterPrice.add("Từ thấp đến cao");
        listTitleFilterPrice.add("Từ cao xuống thấp");
        listTitleFilterPrice.add("Dưới 200k");
        listTitleFilterPrice.add("200k - 500k");
        listTitleFilterPrice.add("Trên 500k");

        //----------------------------------------------
        listTitleFilterSales.add("Sales từ thấp đến cao");
        listTitleFilterSales.add("Sales từ cao xuống thấp");
        listTitleFilterSales.add("Sales dưới 10%");
        listTitleFilterSales.add("Sales 10% - 50%");
        listTitleFilterSales.add("Sales > 50%");
        //---------------------------------------------------
        // init adapter
//        adapterPrice = new FilterAdapter(this, R.layout.row_filter, listTitleFilterPrice);
//        adapterSales = new FilterAdapter(this, R.layout.row_filter, listTitleFilterSales);
        adapter = new FilterAdapter(this, R.layout.row_filter, listTitle);
        lvFilterDetail.setAdapter(adapter);
        lvFilterDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                adapter.setCurPos(position);
                adapter.notifyDataSetChanged();

                // store position of price or sales
                if(curChoice == FILTER_PRICE) {
                    curPosFilterPrice = position;
                }else if(curChoice == FILTER_SALES) {
                    curPosFilterSales = position;
                }
            }
        });

    }

    public void handleOnClickFilterPrice() {

        if(curChoice != FILTER_PRICE) {

            if(curChoice == FILTER_SALES) {
                viewFilterSales.setBackgroundColor(getResources().getColor(R.color.white));
            }
            viewFilterPrice.setBackgroundColor(getResources().getColor(R.color.blue_grey_300));
            if(!visibilityFilterDetail()) {
                showFilterDetail();
            }
            if(listTitle.size() != 0) {
                listTitle.removeAll(listTitle);
            }
            listTitle.addAll(listTitleFilterPrice);
            adapter.setCurPos(curPosFilterPrice);
            adapter.notifyDataSetChanged();
            curChoice = FILTER_PRICE;
        }
    }

    public void handleOnClickFilterSales() {

        if(curChoice != FILTER_SALES) {

            if(curChoice == FILTER_PRICE) {
                viewFilterPrice.setBackgroundColor(getResources().getColor(R.color.white));
            }
            viewFilterSales.setBackgroundColor(getResources().getColor(R.color.blue_grey_300));

            if(!visibilityFilterDetail()) {
                showFilterDetail();
            }
            if(listTitle.size() != 0) {
                listTitle.removeAll(listTitle);
            }
            listTitle.addAll(listTitleFilterSales);
            adapter.setCurPos(curPosFilterSales);
            adapter.notifyDataSetChanged();
            curChoice = FILTER_SALES;
        }
    }

    public void handleOnClickBtnUpdateFilter() {

        if(curChoice == FILTER_SALES) {
//            Toast.makeText(this, "sales", Toast.LENGTH_SHORT).show();
        }else if(curChoice == FILTER_PRICE) {
//            Toast.makeText(this, "price", Toast.LENGTH_SHORT).show();
        }
        hideFilterDetail();
        resetBackgroundFilterTool();
        curChoice = NONE_FILTER;
    }

    public void hideFilteTool() {
        viewFilterTool.setVisibility(View.INVISIBLE);
    }

    public void showFilterTool() {
        viewFilterTool.setVisibility(View.VISIBLE);
    }

    public void hideFilterDetail() {
        viewFilterDetail.setVisibility(View.INVISIBLE);
    }

    public void showFilterDetail() {
        viewFilterDetail.setVisibility(View.VISIBLE);
    }

    public boolean visibilityFilterDetail() {

        if(viewFilterDetail.getVisibility() == View.VISIBLE) {
            return true;
        }else {
            return  false;
        }
    }


    public class LoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            hideFilteTool();
            hideFilterDetail();
            FragmentManager fm = mActivity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            LoadingFragment loadingFragment = LoadingFragment.newInstance();
            ft.replace(R.id.product_list_in_catalog_place_holder, loadingFragment);
            ft.commit();
        }

        @Override
        protected Void doInBackground(Void... params) {

            loadProductListInHotCatalog.loadProductListInHotCatalog(mActivity, hotCatalogName);
            while(!loadProductListInHotCatalog.isLoadProductListComplete()) {
                SystemClock.sleep(ConfigData.TIME_SLEEP);
            }
            loadProductListInHotCatalog.addLoadMore();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                FragmentManager fm = mActivity.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                FragmentProductListInHotCatalog fragmentHot = FragmentProductListInHotCatalog
                        .newInstance(loadProductListInHotCatalog, mToolContainerHeight, hotCatalogName);

                ft.replace(R.id.product_list_in_catalog_place_holder, fragmentHot);
                ft.commit();

                showFilterTool();
            }catch (Exception e) {
                ProductListInHotCatalogActivity.this.finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_product_in_catalog, menu);
        MenuItem searchItem = menu.findItem(R.id.product_in_catalog_search);

        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)MenuItemCompat.getActionView(searchItem);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchResultsActivity.class)));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SuggestionDbUtil.addRowSuggestion(ProductListInHotCatalogActivity.this, query.trim());
                    }
                }).start();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(curChoice == NONE_FILTER) {
            super.onBackPressed();
        }else {
            hideFilterDetail();
           resetBackgroundFilterTool();
        }

        curChoice = NONE_FILTER;
    }

    public void mesFromFragmentFilterPrice(int position) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

    }

    public void mesFromFragmentFilterSales(int position){}

    public void onClickViewSpace(int idFragment) {};

    public void resetBackgroundFilterTool() {
        viewFilterPrice.setBackgroundColor(getResources().getColor(R.color.white));
        viewFilterSales.setBackgroundColor(getResources().getColor(R.color.white));
    }
}
