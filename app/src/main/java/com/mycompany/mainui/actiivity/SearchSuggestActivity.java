package com.mycompany.mainui.actiivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mycompany.mainui.R;
import com.mycompany.mainui.adapter.RecyclerAdapterSearchSuggest;
import com.mycompany.mainui.SpaceItem;
import com.mycompany.mainui.dataaccess.SuggestionDbUtil;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.GetSuggestSearchPro;
import com.mycompany.mainui.model.GetSuggestSearchShop;
import com.mycompany.mainui.model.InfoAccount;
import com.mycompany.mainui.model.RequestId;
import com.mycompany.mainui.model.StringUtil;
import com.mycompany.mainui.model.SuggestProduct;
import com.mycompany.mainui.model.SuggestShop;
import com.mycompany.mainui.networkutil.ConnectServer;
import com.mycompany.mainui.networkutil.StatusInternet;
import com.mycompany.mainui.view.DisplayNotification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchSuggestActivity extends AppCompatActivity {

    EditText editSearch;
    Button btnCancel;
    Spinner spinnerType;
    Toolbar toolbar;

    RecyclerView recyclerView;
    ArrayAdapter<String> spinAdapter;
    RecyclerAdapterSearchSuggest adapterSearch;
    ArrayList listData;
    RecyclerAdapterSearchSuggest.OnItemClickListener onItemListener;

    boolean loading = false;
    public static final long TIME_SLEEP = 1000;
    public static final int PRODUCT_TYPE = 0;
    public static final int SHOP_TYPE = 1;

    long currentTimeSearch = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_suggest);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        editSearch = (EditText)toolbar.findViewById(R.id.edit_search);
        btnCancel = (Button)toolbar.findViewById(R.id.btn_cancel_search);
        spinnerType = (Spinner)toolbar.findViewById(R.id.spiner_search_type);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_suggest_search);

        initBtnCancel();
        initSpinner();
        initRecyclerView();
        initEditText();


        new LoadHistory().execute();

    }

    public void initBtnCancel() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchSuggestActivity.this.finish();
            }
        });
    }

    public void initSpinner(){

        String [] types = new String [] {
            "Sản phẩm","Cửa hàng"
        };
        spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, types);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerType.setAdapter(spinAdapter);

    }

    public void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        // Add divider
        SpaceItem decoration =
                new SpaceItem(5, SpaceItem.VERTICAL);
        recyclerView.addItemDecoration(decoration);

        listData = new ArrayList();
        adapterSearch = new RecyclerAdapterSearchSuggest(this, listData);
        onItemListener = new RecyclerAdapterSearchSuggest.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                Object obj = listData.get(position);
                String query;
                if(obj instanceof String) {
                    query = (String)obj;
                    handleSearch(query);
                }else if(obj instanceof SuggestProduct) {

                    if(StatusInternet.isInternet(SearchSuggestActivity.this)) {
                        SuggestProduct suggestProduct = (SuggestProduct)obj;
                        Intent intent = new Intent(SearchSuggestActivity.this, ProductDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(ConfigData.ARG_ID_PRODUCT, suggestProduct.getIdProduct());
                        bundle.putString(ConfigData.ARG_PRODUCT_NAME, suggestProduct.getProductName());
                        intent.putExtra(ConfigData.ARG_PACKAGE, bundle);
                        SearchSuggestActivity.this.startActivity(intent);
                    }else {
                        DisplayNotification.displayNotifiNoNetwork(SearchSuggestActivity.this);
                    }

                    // load product
                }else if(obj instanceof SuggestShop) {

                    if(StatusInternet.isInternet(SearchSuggestActivity.this)) {
                        SuggestShop suggestShop = (SuggestShop)obj;
                        Intent intent = new Intent(SearchSuggestActivity.this, ShopDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(ConfigData.ARG_ID_SHOP, suggestShop.getIdShop());
                        bundle.putString(ConfigData.ARG_SHOP_NAME, suggestShop.getShopName());
                        intent.putExtra(ConfigData.ARG_PACKAGE, bundle);
                        SearchSuggestActivity.this.startActivity(intent);
                    }else {
                        DisplayNotification.displayNotifiNoNetwork(SearchSuggestActivity.this);
                    }
                }
            }
        };

        adapterSearch.setOnItemClickListener(onItemListener);
        recyclerView.setAdapter(adapterSearch);

    }

    public void initEditText() {

        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    handleSearch(editSearch.getText().toString());
                }
                return false;
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                currentTimeSearch = System.currentTimeMillis();
                handleUpdateSuggest(currentTimeSearch);

            }
        });

    }

    public void handleSearch(String query) {
        if(StatusInternet.isInternet(this)) {

            final String temp = query.trim();
            query = query.trim().toLowerCase();
            if(query.length() > 0) {
                query = StringUtil.standardString(query);
                query = StringUtil.removeAccent(query);
                Intent intent;
                Bundle bundle = new Bundle();

                int type = spinnerType.getSelectedItemPosition();
                if(type == PRODUCT_TYPE) {
                    intent = new Intent(SearchSuggestActivity.this, SearchResultProductActivity.class);
                }else {
                    intent = new Intent(SearchSuggestActivity.this, SearchResultShopActivity.class);
                }

                bundle.putString(ConfigData.ARG_SEARCH_QUERY, query);
                intent.putExtra(ConfigData.ARG_PACKAGE, bundle);
                SearchSuggestActivity.this.startActivity(intent);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SuggestionDbUtil.addRowSuggestion(SearchSuggestActivity.this, temp);
                    }
                }).start();
            }
        }
    }


    public void handleUpdateSuggest(final long time) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(TIME_SLEEP);
                    if(time == currentTimeSearch) {

                        String query = editSearch.getText().toString();
                        if(query.length() > 0) {
                            query = query.trim().toLowerCase();
                            if(query.length() > 0) {
                                int type = spinnerType.getSelectedItemPosition();
                                query = StringUtil.standardString(query);
                                query = StringUtil.removeAccent(query);
//                                Log.d("query search", query);
                                loading = true;
                                if(type == PRODUCT_TYPE) {
                                    handleUpdateSuggestProduct(query);
                                }else {
                                    handleUpdateSuggestShop(query);
                                }
                            }
                        }else {
                            loading = true;
                            new LoadHistory().execute();
                        }

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void handleUpdateSuggestProduct(String query) {

        loading = false;
        Map<String, String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_GET_SUGGEST_SEARCH_PRODUCT);
        tag.put(RequestId.TAG_QUERY, query);
        tag.put(RequestId.TAG_CITY, InfoAccount.getCity(this));

        Call<GetSuggestSearchPro> call = ConnectServer.getResponseAPI().callGetSuggestSearchPro(tag);
        call.enqueue(new Callback<GetSuggestSearchPro>() {
            @Override
            public void onResponse(Call<GetSuggestSearchPro> call, Response<GetSuggestSearchPro> response) {
                if(response.isSuccessful()) {
                    GetSuggestSearchPro data = response.body();
                    if(data != null && data.getSuccess() == RequestId.SUCCESS) {
                        if(!loading) {
                            if(data.getSuggestionProducts() != null) {
                                listData.removeAll(listData);
                                listData.addAll(data.getSuggestionProducts());
                                adapterSearch.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetSuggestSearchPro> call, Throwable t) {

            }
        });
    }

    public void handleUpdateSuggestShop(String query) {

        loading = false;
        Map<String, String> tag = new HashMap<>();
        tag.put(RequestId.ID, RequestId.ID_GET_SUGGEST_SEARCH_SHOP);
        tag.put(RequestId.TAG_CITY, InfoAccount.getCity(this));
        tag.put(RequestId.TAG_QUERY, query);

        Call<GetSuggestSearchShop> call = ConnectServer.getResponseAPI().callGetSuggestSearchShop(tag);
        call.enqueue(new Callback<GetSuggestSearchShop>() {
            @Override
            public void onResponse(Call<GetSuggestSearchShop> call, Response<GetSuggestSearchShop> response) {
                if(response.isSuccessful()) {
                    GetSuggestSearchShop data = response.body();
                    if(data!= null && data.getSuccess() == RequestId.SUCCESS) {
                        if(!loading) {
                            if(data.getSuggestionShops() != null) {
                                listData.removeAll(listData);
                                listData.addAll(data.getSuggestionShops());
                                adapterSearch.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetSuggestSearchShop> call, Throwable t) {

            }
        });
    }

    public class LoadHistory extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            loading = false;
            ArrayList<String> oldSearch = SuggestionDbUtil.getAllSuggestion(SearchSuggestActivity.this);
            if(oldSearch != null && !loading) {
                listData.removeAll(listData);
                listData.addAll(oldSearch);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(!loading) {
                super.onPostExecute(aVoid);
                adapterSearch.notifyDataSetChanged();
            }
        }
    }


}
