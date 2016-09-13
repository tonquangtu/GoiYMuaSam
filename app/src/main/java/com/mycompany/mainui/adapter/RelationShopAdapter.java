package com.mycompany.mainui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.mainui.R;
import com.mycompany.mainui.model.ShortShop;
import com.mycompany.mainui.model.StringUtil;
import com.mycompany.mainui.networkutil.LoadImageFromUrl;

import java.util.List;

/**
 * Created by Dell on 09-May-16.
 */
public class RelationShopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static Activity activity;
    List<ShortShop> listData;

    public interface OnItemClickListener {
        public void onItemClick(int position);
    }

    public OnItemClickListener listener;

    public RelationShopAdapter(Activity activity, List<ShortShop> listData) {
        this.activity = activity;
        this.listData = listData;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ShopHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView txtShopName;
        TextView txtShopAddress;
        TextView txtShopNote;
        TextView txtNumView;


        public ShopHolder(View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.shop_icon);
            txtShopName = (TextView)itemView.findViewById(R.id.shop_name);
            txtShopAddress = (TextView)itemView.findViewById(R.id.shop_address);
            txtShopNote = (TextView)itemView.findViewById(R.id.shop_note);
            txtNumView = (TextView)itemView.findViewById(R.id.shop_number_view);
        }

        public void populate(ShortShop shortShop) {

            txtShopName.setText(shortShop.getShopName());
            txtShopAddress.setText(shortShop.getAddress());
            txtNumView.setText(shortShop.getNumOfView() + " lượt xem");
            String note = shortShop.getNote();
            String titleNote = "Thỏa sức mua sắm cùng " + shortShop.getShopName();
            if(shortShop.getSales() <= 0.0) {
                if(note != null && note.length() > 0) {
                    txtShopNote.setText(shortShop.getNote());
                }else {
                    txtShopNote.setText(titleNote);
                }
            }else {
                txtShopNote.setText(note + " + Khuyến mại : " + shortShop.getSales() + " %");
            }

            String url = StringUtil.convertURL(shortShop.getImageURL());
            LoadImageFromUrl.loadImageFromURL(url, imageView, activity);
        }

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.card_view_relation_shop, parent, false);
        ShopHolder shopHolder = new ShopHolder(view);
        return shopHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ShopHolder shopHolder = (ShopHolder)holder;
        shopHolder.populate(listData.get(position));
        shopHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onItemClick(position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
