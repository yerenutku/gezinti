package com.hackathon.gezinti.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hackathon.gezinti.interfaces.EventCreateListener;
import com.hackathon.gezinti.interfaces.SuccessListener;
import com.hackathon.gezinti.models.request.EventCreateRequest;
import com.hackathon.gezinti.models.response.EventCreateResponse;

/**
 * Created by yutku on 25/03/17.
 */

public class EventInteractor {
    private Context mContext;

    public EventInteractor(Context context) {
        mContext = context;
    }

    public void eventCreate(EventCreateRequest request, final EventCreateListener eventCreateListener) {
        eventCreateListener.onBeforeRequest();
        VolleyClientRequests.getInstance(mContext).postRequest("/api/event/register", request, new SuccessListener<EventCreateResponse>() {
                    @Override
                    public void onSuccess(EventCreateResponse response) {
                        eventCreateListener.onResponse(response);
                        eventCreateListener.onAfterRequest();
                        Log.d("TAG", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG_ERROR", error.toString());
                        eventCreateListener.onError(error.toString());
                    }
                });
    }


}
