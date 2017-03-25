package com.hackathon.gezinti.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hackathon.gezinti.BuildConfig;
import com.hackathon.gezinti.interfaces.SuccessListener;
import com.hackathon.gezinti.models.BaseRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class VolleyClientRequests {

    private static VolleyClientRequests instance;
    private Context mContext;
    private RequestQueue requestQueue;
    private Gson mGson;

    private String mTAG = "VOLLEY_RESPONSE";

    private VolleyClientRequests(Context context) {
        mContext = context;
        mGson = new Gson();
        requestQueue = Volley.newRequestQueue(mContext);
    }

    public static VolleyClientRequests getInstance(Context context) {
        if (instance == null)
            instance = new VolleyClientRequests(context);
        return instance;
    }

    public void post(String url, BaseRequest request, SuccessListener successListener, Response.ErrorListener errorListener) {
        try {
            successListener.setResponseType(request.getResponseType());
            String tempJson = mGson.toJson(request);
            JSONObject jsonObject = new JSONObject(tempJson);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BuildConfig.BASE_URL + url, jsonObject, successListener, errorListener);
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void get(String url, Type type, SuccessListener successListener, Response.ErrorListener errorListener) {
        successListener.setResponseType(type);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BuildConfig.BASE_URL + url, null, successListener, errorListener);
        requestQueue.add(jsonObjectRequest);
    }


}
