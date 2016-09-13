package com.mycompany.mainui.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mycompany.mainui.R;
import com.mycompany.mainui.model.AdProductCatalog;
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.model.StringUtil;
import com.mycompany.mainui.networkutil.LoadImageFromUrl;

import java.util.ArrayList;

/**
 * Created by Mr.T on 4/12/2016.
 */
public class RecyclerAdapterProductListInAd extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity context;
    // -----------   Data  -----------------
    private ArrayList<ShortProduct> listData;
    private AdProductCatalog adProductCatalog;


    // icon_view1 of data - icon_view1 of progressBar
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final int VIEW_TITLE = 2;

    int visibleThreshold = 1;
    final int DISTANCE_SWIPE = 30;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private OnLoadMoreListener onLoadMoreListener;

    // ------------  Interface ------------------
    // Define listener member variable
    private static OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Load when Endless Recycler
    public interface OnLoadMoreListener {
        void onLoadMore();
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public RecyclerAdapterProductListInAd(final Activity context,
                                          RecyclerView recyclerView,
                                          ArrayList<ShortProduct> data,
                                          AdProductCatalog adProductCatalog){
        this.listData = data;
        this.context = context;
        this.adProductCatalog = adProductCatalog;

        // Handle Endless RecyclerView
        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    StaggeredGridLayoutManager sgLayout = (StaggeredGridLayoutManager)recyclerView.getLayoutManager();
                    int totalItem = listData.size();
                    int [] lastVisibleItems = sgLayout.findLastVisibleItemPositions(null);
                    int lastItemPos = lastVisibleItems[0] > lastVisibleItems[1] ? lastVisibleItems[0]:lastVisibleItems[1];

                    if(dy > DISTANCE_SWIPE && lastItemPos + visibleThreshold >= totalItem) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                    }
                }
            });
        }
    }

    public static class TitleAdHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public TitleAdHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.product_list_in_ad_image_view);

        }
    }


    // progress holder
    public static class ProgressBarHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public ProgressBarHolder(final View itemView){
            super(itemView);
            progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);
        }
    }
    // item holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any icon_view1 that will be set as you render a row
        ImageView icon;
        TextView name;
        TextView address;
        Button sale;
        TextView price;
        TextView newPrice;
        ImageView icon_view;
        TextView number_view;

        public ViewHolder(final View itemView) {
            super(itemView);
            icon = (ImageView)itemView.findViewById(R.id.product_icon);
            name = (TextView)itemView.findViewById(R.id.product_name);
            address = (TextView)itemView.findViewById(R.id.product_address);
            sale = (Button)itemView.findViewById(R.id.product_sale);
            icon_view = (ImageView)itemView.findViewById(R.id.product_icon_view);
            number_view = (TextView)itemView.findViewById(R.id.product_number_view);
            price = (TextView)itemView.findViewById(R.id.product_oldPrice);
            newPrice = (TextView)itemView.findViewById(R.id.product_price);

            // Setup the click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition() - 1);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(position == 0) {
            return VIEW_TITLE;
        }else if(listData.get(position -1) != null) {
            return VIEW_ITEM;
        }else {
            return VIEW_PROG;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = inflater
                    .inflate(R.layout.card_view_product_recommend, parent, false);

            vh = new ViewHolder(v);
        } else if(viewType == VIEW_PROG){
            View v = inflater
                    .inflate(R.layout.progress_item, parent, false);

            vh = new ProgressBarHolder(v);
        }else {
            View v = inflater.inflate(R.layout.card_view_title_product_list_in_ad, parent, false);
            vh = new TitleAdHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
            ShortProduct data = listData.get(position -1);
            ViewHolder hd = (ViewHolder)holder;

            hd.name.setText(data.getProductName());
            hd.address.setText(data.getAddress());
            hd.price.setText(data.getPrice() + "");
            hd.sale.setText(data.getPromotionPercent() + " %");
            hd.newPrice.setText(data.getNewPrice()+ "");
            hd.price.setPaintFlags(hd.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            hd.number_view.setText(data.getNumOfView() + " lượt xem");
//            LoadImageFromUrl.loadImageFromResourse(R.drawable.icon_view1, hd.icon_view, context);

            LoadImageFromUrl.loadImageFromURLMulti(StringUtil.convertURL(data.getImageURL()), hd.icon, context);

        }else if(holder instanceof ProgressBarHolder){
            ((ProgressBarHolder) holder).progressBar.setIndeterminate(true);
        }else {


            TitleAdHolder titleAdHolder = (TitleAdHolder)holder;

            LoadImageFromUrl.loadImageFromURL(StringUtil.convertURL(adProductCatalog.getImageURL()),
                    titleAdHolder.imageView, context);

            StaggeredGridLayoutManager.LayoutParams layoutParams1 = ((StaggeredGridLayoutManager.LayoutParams) titleAdHolder
                    .itemView.getLayoutParams());
            layoutParams1.setFullSpan(true);


        }
    }
    @Override
    public int getItemCount() {
        return listData.size() + 1;
    }

}

