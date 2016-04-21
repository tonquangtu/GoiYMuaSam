package com.mycompany.mainui.RecyclerAdapter;

import android.content.Context;
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
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.model.StringUtil;
import com.mycompany.mainui.networkutil.LoadImageFromUrl;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mr.T on 4/12/2016.
 */
public class RecyclerAdapterHotProduct extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    // -----------   Data  -----------------
    private ArrayList<Object> listData;

    // icon_view of data - icon_view of progressBar
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final int VIEW_TITLE = 2;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private boolean loading;
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

    public RecyclerAdapterHotProduct(Context context, ArrayList<Object> listData
            , RecyclerView recyclerView){
        this.listData = listData;
        this.context = context;

        // Handle Endless RecyclerView
        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    View last = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
                    int distanceToEnd = (last.getBottom() - (recyclerView.getHeight() + dy));
                    if(!loading && distanceToEnd <= 30){
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    // Title holder
    public static class TitleHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView title;
        Button more;
        public TitleHolder(final View itemView){
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title_product_hot);
            more = (Button)itemView.findViewById(R.id.btn_more);

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onItemClick(itemView, getLayoutPosition());
                    }
                }
            });

            this.itemView = itemView;
        }
    }


    // progress holder
    public static class ProgressBarHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        View itemView;
        public ProgressBarHolder(final View itemView){
            super(itemView);
            progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);
            this.itemView = itemView;
        }
    }
    // item holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any icon_view that will be set as you render a row
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
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(listData.get(position) instanceof ShortProduct){
            return VIEW_ITEM;
        }
        else if(listData.get(position) instanceof String){
            return VIEW_TITLE;
        }
        else return VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = inflater
                    .inflate(R.layout.card_view_hot, parent, false);

            vh = new ViewHolder(v);
        } else if (viewType == VIEW_TITLE){
            View v = inflater.inflate(R.layout.title_product_hot, parent, false);
            vh = new TitleHolder(v);

        }else{
            View v = inflater
                    .inflate(R.layout.progress_item, parent, false);

            vh = new ProgressBarHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
            ShortProduct data = (ShortProduct) listData.get(position);
            ViewHolder hd = (ViewHolder)holder;

            hd.name.setText(data.getProductName());
            hd.address.setText(data.getAddress());
            hd.newPrice.setText(data.getNewPrice());
            hd.sale.setText(data.getPromotionPercent() + "%");
            hd.price.setText(data.getPrice() + "");
            hd.price.setPaintFlags(hd.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            Picasso.with(context)
                    .load(R.drawable.icon_view)
                    .into(hd.icon_view);
            hd.number_view.setText(data.getNumOfView());
            LoadImageFromUrl.loadImageFromURL(StringUtil.convertURL(data.getImageURL()), hd.icon, context);
            LoadImageFromUrl.loadImageFromResourse(R.drawable.icon_view, hd.icon_view, context);

            /////////////////////////////////

        }else if (holder instanceof TitleHolder) {
            StaggeredGridLayoutManager.LayoutParams layoutParams
                    = (StaggeredGridLayoutManager.LayoutParams)
                        ((TitleHolder)holder).itemView.getLayoutParams();
            layoutParams.setFullSpan(true);

            String catalog = (String)listData.get(position);
            ((TitleHolder) holder).title.setText(catalog);
        }
        else{
            StaggeredGridLayoutManager.LayoutParams layoutParams
                    = (StaggeredGridLayoutManager.LayoutParams)
                    ((ProgressBarHolder)holder).itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
            ((ProgressBarHolder) holder).progressBar.setIndeterminate(true);
        }
    }
    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void setLoaded() {
        loading = false;
    }
}

