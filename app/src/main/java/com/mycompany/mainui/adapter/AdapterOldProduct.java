package com.mycompany.mainui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.mainui.R;
import com.mycompany.mainui.networkutil.LoadImageFromUrl;

import java.util.ArrayList;

/**
 * Created by Mr.T on 5/17/2016.
 */
public class AdapterOldProduct extends RecyclerView.Adapter<AdapterOldProduct.ViewHolder> {
    private Context context;
    // -----------   Data  -----------------
    private ArrayList<Banner> listData;

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


    public AdapterOldProduct(Context context, ArrayList<Banner> listData){
        this.listData = listData;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public ViewHolder(final View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.image);
            title = (TextView)itemView.findViewById(R.id.title);

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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.banner, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Banner data = listData.get(position);
        TextView title = holder.title;
        title.setText(data.getTitle());
        ImageView image = holder.image;
        LoadImageFromUrl.loadImageFromResourse(data.getImage(), image, 700, 700, context);
//        Picasso.with(context).load(data.getImage()).resize(400, 400).into(image);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

}
