package com.hackathon.gezinti.interfaces;

import com.hackathon.gezinti.models.response.GetEventDetailResponse;

/**
 * Created by yutku on 25/03/17.
 */

public interface GetEventDetailListener extends BaseListener {
    public void onGetEventDetail(GetEventDetailResponse response);
}
