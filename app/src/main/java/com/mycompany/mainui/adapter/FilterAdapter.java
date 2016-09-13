package com.mycompany.mainui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mycompany.mainui.R;

import java.util.ArrayList;

/**
 * Created by Dell on 5/1/2016.
 */
public class FilterAdapter extends ArrayAdapter<String> {

    ArrayList<String > listData;
    Context context;
    int res;
    int curPos;

    public FilterAdapter(Context context, int resource, ArrayList<String> listData) {
        super(context, resource, listData);
        this.listData = listData;
        this.context = context;
        this.res = resource;
        this.curPos = -1;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

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

        /**
         * check if position == current pos then checked radio button
         * else set checked for radio button is false
         */
        boolean checked = false;
        if(position == curPos) {
            checked = true;
        }
        holder.populate(listData.get(position), checked);
        return view;
    }

    public static class Holder  {

        TextView txt;
        RadioButton radioButton;

        public Holder(View v) {

            txt = (TextView)v.findViewById(R.id.title_filter);
            radioButton = (RadioButton)v.findViewById(R.id.radio_filter);
        }

        public void populate(String title, boolean checked) {
            txt.setText(title);
            txt.setFocusable(false);
            radioButton.setFocusable(false);
            radioButton.setChecked(checked);
        }
    }

    public void setCurPos(int curPos) {
        this.curPos = curPos;
    }

}
