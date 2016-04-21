package com.mycompany.mainui.RecyclerAdapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mycompany.mainui.R;
import com.mycompany.mainui.model.ShortShop;
import com.mycompany.mainui.model.StringUtil;
import com.mycompany.mainui.networkutil.LoadImageFromUrl;

import java.util.ArrayList;

/**
 * Created by Mr.T on 4/12/2016.
 */
public class RecyclerAdapterShopNearest extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    // -----------   Data  -----------------
    private ArrayList<ShortShop> listData;
    public static final String TAG_NUM_VIEW = " lượt xem";

    // icon_view of data - icon_view of progressBar
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
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



    public RecyclerAdapterShopNearest(Context context, ArrayList<ShortShop> listData
            , RecyclerView recyclerView){
        this.listData = listData;
        this.context = context;

        // Handle Endless RecyclerView
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager =
                        (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
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
        // for any icon_view that will be set as you render a row
        ImageView icon;
        TextView name;
        TextView address;
        TextView note;
        ImageView icon_view;
        TextView number_view;

        public ViewHolder(final View itemView) {
            super(itemView);
            icon = (ImageView)itemView.findViewById(R.id.shop_icon);
            name = (TextView)itemView.findViewById(R.id.shop_name);
            address = (TextView)itemView.findViewById(R.id.shop_address);
            note = (TextView)itemView.findViewById(R.id.shop_note);
            icon_view = (ImageView)itemView.findViewById(R.id.shop_icon_view);
            number_view = (TextView) itemView.findViewById(R.id.shop_number_view);
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
        return listData.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = inflater
                    .inflate(R.layout.card_view_shop_nearest, parent, false);

            vh = new ViewHolder(v);
        } else {
            View v = inflater
                    .inflate(R.layout.progress_item, parent, false);

            vh = new ProgressBarHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
            ShortShop data = listData.get(position);

            ((ViewHolder)holder).name.setText(data.getShopName());
            ((ViewHolder)holder).address.setText(data.getAddress());
            LoadImageFromUrl.loadImageFromURL(StringUtil.convertURL(data.getImageURL()),
                    ((ViewHolder) holder).icon,context);

            LoadImageFromUrl.loadImageFromResourse(R.drawable.icon_view, ((ViewHolder) holder).icon_view, context);

            ((ViewHolder)holder).note.setText(data.getNote());
            ((ViewHolder)holder).number_view.setText(data.getNumOfView() + TAG_NUM_VIEW);

        }else{
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
