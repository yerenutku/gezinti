package com.hackathon.gezinti.models.response;

import com.google.gson.annotations.SerializedName;
import com.hackathon.gezinti.models.common.Event;

import java.util.ArrayList;

/**
 * Created by yutku on 25/03/17.
 */

public class EventSearchResponse {
    @SerializedName("result")
    private ArrayList<Event> mEvents;

    public ArrayList<Event> getEvents() {
        return mEvents;
    }
}
