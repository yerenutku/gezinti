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

}
