package com.hackathon.gezinti.models.common;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by yutku on 25/03/17.
 */

public class Coordinates implements Serializable{
    @SerializedName("lat")
    String lat;

    @SerializedName("lon")
    String lon;

    public Coordinates(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
