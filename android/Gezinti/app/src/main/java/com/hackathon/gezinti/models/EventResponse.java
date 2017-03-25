package com.hackathon.gezinti.models;

public class EventResponse {

    private String text;

    public EventResponse(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
