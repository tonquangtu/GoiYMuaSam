package com.mycompany.mainui.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.mycompany.mainui.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Mr.T on 5/18/2016.
 */
public class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter {
    LayoutInflater inflater;
    InfoWindow infoWindow;
    Context context;

    public CustomWindowAdapter(Context context, LayoutInflater inflater, InfoWindow infoWindow){
        this.inflater = inflater;
        this.infoWindow = infoWindow;
        this.context = context;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        String title = marker.getTitle();
        View v = inflater.inflate(R.layout.custom_info_window, null);
        ImageView image = (ImageView)v.findViewById(R.id.image_store);
        TextView name = (TextView)v.findViewById(R.id.name);
        TextView snippet = (TextView)v.findViewById(R.id.snippet);
        if(title != null){
            Picasso.with(context)
                    .load(R.drawable.people)
                    .resize(450, 400)
                    .into(image);
            name.setText(title);
            snippet.setText(marker.getSnippet());
        }
        else{
            Picasso.with(context)
                    .load(infoWindow.getImage())
                    .resize(450, 400)
                    .into(image);
            name.setText(infoWindow.getName());
            snippet.setText(infoWindow.getAddress());
        }
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
