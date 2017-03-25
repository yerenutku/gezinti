package com.hackathon.gezinti.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.hackathon.gezinti.interfaces.EventCreateListener;
import com.hackathon.gezinti.interfaces.EventSearchListener;
import com.hackathon.gezinti.interfaces.GetEventDetailListener;
import com.hackathon.gezinti.interfaces.SuccessListener;
import com.hackathon.gezinti.models.request.EventCreateRequest;
import com.hackathon.gezinti.models.request.EventSearchRequest;
import com.hackathon.gezinti.models.response.EventCreateResponse;
import com.hackathon.gezinti.models.response.EventSearchResponse;
import com.hackathon.gezinti.models.response.GetEventDetailResponse;

/**
 * Created by yutku on 25/03/17.
 */

public class EventInteractor {
    private Context mContext;

    public EventInteractor(Context context) {
        mContext = context;
    }

    public void eventCreate(EventCreateRequest request, final EventCreateListener listener) {
        listener.onBeforeRequest();
        VolleyClientRequests.getInstance(mContext).post("/api/event/register", request, new SuccessListener<EventCreateResponse>() {
                    @Override
                    public void onSuccess(EventCreateResponse response) {
                        listener.onResponse(response);
                        listener.onAfterRequest();
                        Log.d("TAG", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG_ERROR", error.toString());
                        listener.onError(error.toString());
                    }
                });
    }

    public void eventSearch(EventSearchRequest request, final EventSearchListener listener) {
        listener.onBeforeRequest();
        VolleyClientRequests.getInstance(mContext).post("/api/event/search", request, new SuccessListener<EventSearchResponse>() {
                    @Override
                    public void onSuccess(EventSearchResponse response) {
                        listener.onEventSearch(response);
                        listener.onAfterRequest();
                        Log.d("TAG", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG_ERROR", error.toString());
                        listener.onError(error.toString());
                    }
                });
    }

    public void getEventDetail(String eventId, final GetEventDetailListener listener) {
        listener.onBeforeRequest();
        VolleyClientRequests.getInstance(mContext).get("/api/event/" + eventId, new TypeToken<GetEventDetailResponse>() {
        }.getType(), new SuccessListener<GetEventDetailResponse>() {

            @Override
            public void onSuccess(GetEventDetailResponse response) {
                listener.onGetEventDetail(response);
                listener.onAfterRequest();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error.toString());
            }
        });
    }


}
