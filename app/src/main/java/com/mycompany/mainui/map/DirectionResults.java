package com.mycompany.mainui.map;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Created by Mr.T on 4/28/2016.
 */
public class DirectionResults {
    @SerializedName("geocoded_waypoints")
    private  ArrayList<GeocodedWayPoint> geocodedWayPoints;
    private ArrayList<Route> routes;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //  public constructor is necessary for collections
    DirectionResults(){
        geocodedWayPoints = new ArrayList<>();
        routes = new ArrayList<>();
    }

    public ArrayList<GeocodedWayPoint> getGeocodedWayPoints() {
        return geocodedWayPoints;
    }

    public void setGeocodedWayPoints(ArrayList<GeocodedWayPoint> geocodedWayPoints) {
        this.geocodedWayPoints = geocodedWayPoints;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }
}
