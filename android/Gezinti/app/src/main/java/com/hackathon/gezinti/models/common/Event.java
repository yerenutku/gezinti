package com.hackathon.gezinti.models.common;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yutku on 25/03/17.
 */

public class Event implements Serializable {
    @SerializedName("_id")
    public String id;

    @SerializedName("title")
    public String title;

    @SerializedName("owner")
    public User owner;

    @SerializedName("desc")
    public String desc;

    @SerializedName("eventType")
    public String eventType;

    @SerializedName("eventTime")
    public String eventTime;

    @SerializedName("location")
    public List<Coordinates> mCoordinatesList;

    @SerializedName("members")
    public List<User> members = null;

    @SerializedName("createDate")
    public String createDate;


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getOwner() {
        return owner;
    }

    public String getDesc() {
        return desc;
    }

    public List<Coordinates> getCoordinatesList() {
        return mCoordinatesList;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventTime() {
        return eventTime;
    }

    public List<User> getMembers() {
        return members;
    }

    public String getCreateDate() {
        return createDate;
    }
}

