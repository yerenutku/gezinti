package com.hackathon.gezinti.models;

public class EventResponse {

    private String text;
    private double longitude, latitude;

    public EventResponse(String text, double latitude, double longitude) {
        this.text = text;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
