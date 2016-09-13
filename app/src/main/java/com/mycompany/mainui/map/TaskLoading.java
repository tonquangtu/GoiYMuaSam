package com.mycompany.mainui.map;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by Mr.T on 4/28/2016.
 */
public class TaskLoading extends AsyncTask<Void,Void,ArrayList<LatLng>> {
    MyApiEndPointInterface apiService;
    GoogleMap mMap;
    String start;
    String end;
    TextValue distance;
    TextValue duration;

    public TaskLoading(MyApiEndPointInterface apiService, GoogleMap mMap, String start, String end){
        this.apiService = apiService;
        this.mMap = mMap;
        this.start = start;
        this.end = end;
    }

    @Override
    protected ArrayList<LatLng> doInBackground(Void... params) {
        ArrayList<LatLng> routeList = new ArrayList<>();
        Call<DirectionResults> call = apiService.getJson(start,end
                , "AIzaSyCO80d2HeElbprv2Gn9HI9ER0Y8HF6wsTY");
        //Log.d("aaa", call.request().toString());
        DirectionResults directionResults = null;
        try {
            directionResults = call.execute().body();
            //Log.d("aaa", call.execute().body().toString());

            if(directionResults.getRoutes().size()>0){
                ArrayList<LatLng> decodeList;
                // route
                Route route1 = directionResults.getRoutes().get(0);
                // duration and distance
                Leg leg0 = route1.getLegs().get(0);
                distance = leg0.getDistance();
                duration = leg0.getDuration();

                if(route1.getLegs().size()>0){
                    // steps
                    ArrayList<Step> steps = leg0.getSteps();


                    Location location;
                    String polyline;
                    for(Step step:steps){
                        location =step.getStartLocation();
                        routeList.add(new LatLng(location.getLat(), location.getLng()));

                        polyline = step.getPolyline().getPoints();
                        decodeList = RouteDecode.decodePoly(polyline);
                        routeList.addAll(decodeList);

                        location =step.getEndLocation();
                        routeList.add(new LatLng(location.getLat() ,location.getLng()));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
       return routeList;
    }

    @Override
    protected void onPostExecute(ArrayList<LatLng> routeList) {
        if(routeList.size()>0){
            PolylineOptions rectLine = new PolylineOptions().width(8).color(Color.RED);
            for (int i = 0; i < routeList.size(); i++) {
                rectLine.add(routeList.get(i));
            }

            // Add a marker and move the camera
            LatLng endLocation = routeList.get(routeList.size() - 1);
            LatLng startLocation = routeList.get(0);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(startLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .title("Vị trí của bạn")
                    .snippet("Cách: " + distance.getText() + "\n"
                                        + "Thời gian: " + duration.getText()));
            SpinMarker.dropPinEffect(marker);
            CameraPosition y = new CameraPosition.Builder().target(endLocation)
                    .zoom(16f).bearing(90).tilt(30f).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(y), 3000, null);
            mMap.addPolyline(rectLine);
        }
    }
}
