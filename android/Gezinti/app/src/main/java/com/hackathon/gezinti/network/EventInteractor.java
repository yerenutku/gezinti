package com.hackathon.gezinti.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.hackathon.gezinti.R;
import com.hackathon.gezinti.interfaces.EventCreateListener;
import com.hackathon.gezinti.interfaces.EventSearchListener;
import com.hackathon.gezinti.interfaces.GeneralSuccessListener;
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
                        listener.onError(userFriendlyError(error));
                        listener.onAfterRequest();
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
                        listener.onError(userFriendlyError(error));
                        listener.onAfterRequest();
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
                Log.d("TAG_ERROR", error.toString());
                listener.onError(userFriendlyError(error));
                listener.onAfterRequest();
            }
        });
    }

    public void leaveEvent(String eventId, String userId, final GeneralSuccessListener listener) {
        listener.onBeforeRequest();
        VolleyClientRequests.getInstance(mContext).get("/api/event/" + eventId + "/" + userId + "/remove", null, new SuccessListener() {
            @Override
            public void onSuccess(Object response) {
                listener.onSuccess();
                listener.onAfterRequest();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG_ERROR", error.toString());
                listener.onError(userFriendlyError(error));
                listener.onAfterRequest();
            }
        });
    }

    public void joinEvent(String eventId, String userId, final GeneralSuccessListener listener) {
        listener.onBeforeRequest();
        VolleyClientRequests.getInstance(mContext).get("/api/event/" + eventId + "/" + userId + "/join", null, new SuccessListener() {
            @Override
            public void onSuccess(Object response) {
                listener.onSuccess();
                listener.onAfterRequest();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG_ERROR", error.toString());
                listener.onError(userFriendlyError(error));
                listener.onAfterRequest();
            }
        });
    }

    public void deleteEvent(String eventId, final GeneralSuccessListener listener){
        listener.onBeforeRequest();
        VolleyClientRequests.getInstance(mContext).get("/api/event/" + eventId + "/remove", null, new SuccessListener() {
            @Override
            public void onSuccess(Object response) {
                listener.onSuccess();
                listener.onAfterRequest();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG_ERROR", error.toString());
                listener.onError(userFriendlyError(error));
                listener.onAfterRequest();
            }
        });
    }

    private String userFriendlyError(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            return mContext.getString(R.string.error_msg_timeout) + " : " + mContext.getString(R.string.error_msg_reason) + error.getMessage();
        } else if (error instanceof AuthFailureError) {
            return mContext.getString(R.string.error_msg_auth_failure) + " : " + mContext.getString(R.string.error_msg_reason) + error.getMessage();
        } else if (error instanceof ServerError) {
            return mContext.getString(R.string.error_msg_server) + " : " + mContext.getString(R.string.error_msg_reason) + error.getMessage();
        } else if (error instanceof NetworkError) {
            return mContext.getString(R.string.error_msg_network) + " : " + mContext.getString(R.string.error_msg_reason) + error.getMessage();
        } else if (error instanceof ParseError) {
            return mContext.getString(R.string.error_msg_parse) + " : " + mContext.getString(R.string.error_msg_reason) + error.getMessage();
        } else {
            return mContext.getString(R.string.error_msg_general_title) + " : " + mContext.getString(R.string.error_msg_reason) + error.getMessage();
        }
    }


}
