package com.mycompany.mainui.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.mainui.R;
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.model.StringUtil;
import com.mycompany.mainui.networkutil.LoadImageFromUrl;

import java.util.List;

/**
 * Created by Dell on 08-May-16.
 */
public class RelationProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static Activity activity;
    List<ShortProduct> listData;

    public interface OnItemClickListener {
        public void onItemClick(int position);
    }

    public OnItemClickListener listener;

    public RelationProductAdapter(Activity activity, List<ShortProduct> listData) {
        this.listData = listData;
        this.activity = activity;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

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
        }

        public void populate(ShortProduct data) {

            name.setText(data.getProductName() );
            address.setText(data.getAddress());
            price.setText(data.getPrice() + "");
            sale.setText(data.getPromotionPercent() + " %");
            newPrice.setText(data.getNewPrice()+ "");
            price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            number_view.setText(data.getNumOfView() + " lượt xem");
            LoadImageFromUrl.loadImageFromResourse(R.drawable.icon_view1, icon_view, activity);

            LoadImageFromUrl.loadImageFromURL(StringUtil.convertURL(data.getImageURL()), icon, activity);
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater
                .inflate(R.layout.card_view_relation_product, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ShortProduct dataRow = listData.get(position);
        ViewHolder vh = (ViewHolder)holder;
        vh.populate(dataRow);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
