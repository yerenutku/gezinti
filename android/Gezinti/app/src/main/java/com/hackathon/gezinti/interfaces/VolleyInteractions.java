package com.hackathon.gezinti.interfaces;

import com.hackathon.gezinti.models.EventResponse;

public interface VolleyInteractions {
    void onSuccess(EventResponse response);

    void onError(String reason, String errorMessage);

    void onBeforeRequest();

    void onAfterRequest();
}