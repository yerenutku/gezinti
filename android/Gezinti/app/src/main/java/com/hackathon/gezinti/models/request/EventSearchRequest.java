package com.hackathon.gezinti.models.request;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.hackathon.gezinti.models.BaseRequest;
import com.hackathon.gezinti.models.response.EventSearchResponse;

import java.lang.reflect.Type;

/**
 * Created by yutku on 25/03/17.
 */

public class EventSearchRequest extends BaseRequest {
    @SerializedName("lat")
    private String lat;
    @SerializedName("lon")
    private String lon;
    @SerializedName("eventType")
    private int eventType;
    @SerializedName("eventTime")
    private int eventTime;

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public void setEventTime(int eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public Type getResponseType() {
        return new TypeToken<EventSearchResponse>() {
        }.getType();
    }
}
