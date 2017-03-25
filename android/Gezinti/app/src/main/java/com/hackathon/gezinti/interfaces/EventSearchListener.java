package com.hackathon.gezinti.interfaces;

import com.hackathon.gezinti.models.response.EventSearchResponse;

/**
 * Created by yutku on 25/03/17.
 */

public interface EventSearchListener extends BaseListener {
    public void onEventSearch(EventSearchResponse response);
}
