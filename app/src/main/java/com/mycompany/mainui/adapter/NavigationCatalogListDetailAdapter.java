package com.mycompany.mainui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mycompany.mainui.R;

import java.util.ArrayList;

/**
 * Created by Dell on 4/28/2016.
 */
public class NavigationCatalogListDetailAdapter extends ArrayAdapter<String> {

    Context context;
    int res;
    ArrayList<String> listData;

    public NavigationCatalogListDetailAdapter(Context context, int resource,  ArrayList<String> listData) {
        super(context, resource, listData);

        this.context = context;
        this.listData = listData;
        this.res = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        Holder holder;
        if(convertView != null) {
            view = convertView;
            holder = (Holder) view.getTag();
        }else {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(res, parent, false);
            holder = new Holder(view);
            view.setTag(holder);
        }

        holder.setText(listData.get(position));
        return view;
    }

    public class Holder {

        TextView txt;
        public Holder(View v) {
            txt = (TextView)v.findViewById(R.id.navigation_catalog_list_detail_txt);
        }

        public void setText(String text){
            txt.setText(text);
        }
    }
}
