package com.hackathon.gezinti.interfaces;

/**
 * Created by yutku on 25/03/17.
 */

public interface BaseListener {
    void onError(String errorMessage);

    void onBeforeRequest();

    void onAfterRequest();
}
