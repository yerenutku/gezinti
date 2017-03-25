package com.hackathon.gezinti.models.request;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.hackathon.gezinti.models.BaseRequest;
import com.hackathon.gezinti.models.common.Coordinates;
import com.hackathon.gezinti.models.response.EventCreateResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class EventCreateRequest extends BaseRequest {

    @SerializedName("title")
    private String title;

    @SerializedName("owner")
    private String owner;

    @SerializedName("desc")
    private String desc;

    @SerializedName("time")
    private String time;

    @SerializedName("location")
    private ArrayList<Coordinates> mCoordinates;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCoordinates(ArrayList<Coordinates> coordinates) {
        mCoordinates = coordinates;
    }

    @Override
    public Type getResponseType() {
        return new TypeToken<EventCreateResponse>() {
        }.getType();
    }
}
