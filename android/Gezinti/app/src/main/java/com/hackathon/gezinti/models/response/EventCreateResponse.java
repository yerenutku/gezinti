package com.hackathon.gezinti.models.response;

import com.google.gson.annotations.SerializedName;
import com.hackathon.gezinti.models.common.User;

public class EventCreateResponse {
    @SerializedName("title")
    private String title;
    @SerializedName("owner")
    private User mUser;
    @SerializedName("desc")
    private String desc;

    public String getTitle() {
        return title;
    }

    public User getUser() {
        return mUser;
    }

    public String getDesc() {
        return desc;
    }
}
