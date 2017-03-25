package com.hackathon.gezinti.interfaces;

import com.hackathon.gezinti.models.response.EventCreateResponse;

/**
 * Created by yutku on 25/03/17.
 */

public interface EventCreateListener extends BaseListener {
    void onResponse(EventCreateResponse response);
}
