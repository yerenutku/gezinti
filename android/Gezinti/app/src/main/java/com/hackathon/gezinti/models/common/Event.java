package com.hackathon.gezinti.models.common;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yutku on 25/03/17.
 */

public class Event implements Serializable{
    @SerializedName("_id")
    public String id;

    @SerializedName("title")
    public String title;

    @SerializedName("owner")
    public Owner owner;

    @SerializedName("desc")
    public String desc;

    @SerializedName("time")
    public String time;

    @SerializedName("messages")
    public ArrayList<Object> messages;

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

    public Owner getOwner() {
        return owner;
    }

    public String getDesc() {
        return desc;
    }

    public String getTime() {
        return time;
    }

    public ArrayList<Object> getMessages() {
        return messages;
    }

    public List<Coordinates> getCoordinatesList() {
        return mCoordinatesList;
    }

    public List<User> getMembers() {
        return members;
    }

    public String getCreateDate() {
        return createDate;
    }
}

