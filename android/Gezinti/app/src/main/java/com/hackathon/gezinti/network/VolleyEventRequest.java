package com.hackathon.gezinti.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hackathon.gezinti.BuildConfig;
import com.hackathon.gezinti.R;
import com.hackathon.gezinti.interfaces.VolleyInteractions;
import com.hackathon.gezinti.models.EventRequest;
import com.hackathon.gezinti.models.EventResponse;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class VolleyEventRequest {

    private static VolleyEventRequest instance;
    private Context mContext;
    private RequestQueue requestQueue;
    private Gson mGson;

    private String mTAG = "VOLLEY_RESPONSE";

    private VolleyEventRequest(Context context){
        this.mContext = context;
        this.mGson = new Gson();
    }

    public static VolleyEventRequest getInstance(Context context){
        if(instance == null)
            instance = new VolleyEventRequest(context);
        return instance;
    }

    public void postRequest(final EventRequest eventRequest, final VolleyInteractions volleyInteractions) {
        volleyInteractions.onBeforeRequest();

        try {
            String tempJson = mGson.toJson(eventRequest);
            JSONObject jsonObject = new JSONObject(tempJson);
            @SuppressWarnings("unchecked")
            GsonPostRequest gsonPostRequest = new GsonPostRequest(
                    BuildConfig.BASE_URL,
                    jsonObject.toString(),
                    eventRequest.getResponseType(),
                    mGson,
                    new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            Log.d(TAG, "onResponse: " + response.toString());
                            EventResponse eventResponse = (EventResponse) response;
                            if (eventResponse.getCode() == 0) {
                                volleyInteractions.onSuccess(eventResponse);
                            } else {
                                volleyInteractions.onError(mContext.getString(R.string.error_msg_general_title), eventResponse.getMsg());
                            }
                            volleyInteractions.onAfterRequest();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse: " + error.getMessage());
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                volleyInteractions.onError(mContext.getString(R.string.error_msg_timeout), mContext.getString(R.string.error_msg_reason) + error.getMessage());
                            } else if (error instanceof AuthFailureError) {
                                volleyInteractions.onError(mContext.getString(R.string.error_msg_auth_failure), mContext.getString(R.string.error_msg_reason) + error.getMessage());
                            } else if (error instanceof ServerError) {
                                volleyInteractions.onError(mContext.getString(R.string.error_msg_server), mContext.getString(R.string.error_msg_reason) + error.getMessage());
                            } else if (error instanceof NetworkError) {
                                volleyInteractions.onError(mContext.getString(R.string.error_msg_network), mContext.getString(R.string.error_msg_reason) + error.getMessage());
                            } else if (error instanceof ParseError) {
                                volleyInteractions.onError(mContext.getString(R.string.error_msg_parse), mContext.getString(R.string.error_msg_reason) + error.getMessage());
                            } else {
                                volleyInteractions.onError(mContext.getString(R.string.error_msg_general_title), mContext.getString(R.string.error_msg_reason) + error.getMessage());
                            }
                            volleyInteractions.onAfterRequest();

                        }
                    }
            );
            requestQueue.add(gsonPostRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
