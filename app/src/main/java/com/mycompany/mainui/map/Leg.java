package com.mycompany.mainui.map;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mr.T on 4/28/2016.
 */
public class Leg {
    private TextValue distance;
    private TextValue duration;
    @SerializedName("end_address")
    private String endAddress;
    @SerializedName("end_location")
    private Location endLocation;
    @SerializedName("start_address")
    private String startAddress;
    @SerializedName("start_location")
    private Location startLocation;
    private ArrayList<Step> steps;

    public TextValue getDistance() {
        return distance;
    }

    public void setDistance(TextValue distance) {
        this.distance = distance;
    }

    public TextValue getDuration() {
        return duration;
    }

    public void setDuration(TextValue duration) {
        this.duration = duration;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public Location getStatrLocation() {
        return startLocation;
    }

    public void setStatrLocation(Location statrLocation) {
        this.startLocation = statrLocation;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }
}
