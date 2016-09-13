package com.mycompany.mainui.map;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mr.T on 4/28/2016.
 */
public class Step {
    private TextValue distance;
    private TextValue duration;
    @SerializedName("end_location")
    private Location endLocation;
    @SerializedName("html_instructions")
    private String htmlInstructions;
    private OverViewPolyLine polyline;
    @SerializedName("start_location")
    private Location startLocation;
    @SerializedName("travel_mode")
    private String travelMode;

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

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public String getHtmlInstructions() {
        return htmlInstructions;
    }

    public void setHtmlInstructions(String htmlInstructions) {
        this.htmlInstructions = htmlInstructions;
    }

    public OverViewPolyLine getPolyline() {
        return polyline;
    }

    public void setPolyline(OverViewPolyLine polyline) {
        this.polyline = polyline;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }
}
