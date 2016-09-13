package com.mycompany.mainui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.mainui.R;
import com.mycompany.mainui.model.StringUtil;
import com.mycompany.mainui.model.SuggestProduct;
import com.mycompany.mainui.model.SuggestShop;
import com.mycompany.mainui.networkutil.LoadImageFromUrl;

import java.util.ArrayList;

/**
 * Created by Dell on 05-May-16.
 */
public class RecyclerAdapterSearchSuggest extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static Activity activity;
    ArrayList listData;
    public static final int VIEW_SHOP = 2;
    public static final int VIEW_PRODUCT = 1;
    public static final int VIEW_OLD = 0;


    public  OnItemClickListener listener;
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }



    public RecyclerAdapterSearchSuggest(Activity activity, ArrayList listData) {
        this.activity = activity;
        this.listData = listData;
    }

    @Override
    public int getItemViewType(int position) {

        Object obj = listData.get(position);
        if(obj instanceof SuggestProduct) {
            return VIEW_PRODUCT;
        }else if(obj instanceof SuggestShop) {
            return VIEW_SHOP;
        }else {
            return VIEW_OLD;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        LayoutInflater inflater = LayoutInflater.from(activity);

        if(viewType == VIEW_PRODUCT) {
            View v = inflater.inflate(R.layout.card_view_search_suggest_product, parent, false);
            vh = new SuggestProductHolder(v);
        }else if(viewType == VIEW_SHOP) {
            View v = inflater.inflate(R.layout.card_view_search_suggest_shop, parent, false);
            vh = new SuggestShopHolder(v);
        }else {
            View v = inflater.inflate(R.layout.card_view_search_suggest_product, parent, false);
            vh = new OldSearchHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof SuggestProductHolder) {
            SuggestProduct suggestProduct = (SuggestProduct)listData.get(position);
            final SuggestProductHolder suggestProHolder = (SuggestProductHolder)holder;
            suggestProHolder.populate(suggestProduct.getProductName());
            suggestProHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onItemClick(suggestProHolder.itemView, position);
                    }
                }
            });
        }else if(holder instanceof SuggestShopHolder) {
            SuggestShop suggestShop = (SuggestShop)listData.get(position);
            final SuggestShopHolder suggestShopHolder = (SuggestShopHolder)holder;
            suggestShopHolder.populate(suggestShop);
            suggestShopHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onItemClick(suggestShopHolder.itemView, position);
                    }
                }
            });
        }else {

            String oldSearch = (String)listData.get(position);
            final OldSearchHolder oldSearchHolder = (OldSearchHolder)holder;
            oldSearchHolder.populate(oldSearch);
            oldSearchHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onItemClick(oldSearchHolder.itemView, position);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class SuggestProductHolder extends RecyclerView.ViewHolder {

        TextView txt;
        public SuggestProductHolder(View itemView) {
            super(itemView);
            txt = (TextView)itemView.findViewById(R.id.txt_suggest_product);
        }

        public void populate(String name) {
            txt.setText(name);
        }
    }

    public static class SuggestShopHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView txtShopName;
        TextView txtShopAddress;

        public SuggestShopHolder(View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.image_view_suggest_shop);
            txtShopAddress = (TextView)itemView.findViewById(R.id.txt_shop_address_row_search_suggest_shop);
            txtShopName = (TextView)itemView.findViewById(R.id.txt_shop_name_row_search_suggest_shop);
        }

        public void populate(SuggestShop suggestShop) {

            txtShopName.setText(suggestShop.getShopName());
            txtShopAddress.setText(suggestShop.getAddress());

            String url = StringUtil.convertURL(suggestShop.getImageURL());
            LoadImageFromUrl.loadImageFromURL(url, imageView, activity);
        }
    }

    public static class OldSearchHolder extends RecyclerView.ViewHolder {

        TextView txt;
        public OldSearchHolder(View itemView) {
            super(itemView);
            txt = (TextView)itemView.findViewById(R.id.txt_suggest_product);
        }

        public void populate(String oldSearch) {
            txt.setText(oldSearch);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }




}
