package com.hackathon.gezinti.interfaces;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public abstract class SuccessListener<T> implements Response.Listener {
    private Type type;

    public void setResponseType(Type type) {
        this.type = type;
    }

    @Override
    public void onResponse(Object response) {
        Gson gson = new GsonBuilder().create();
        if (type == null) {
            type = Object.class;
        }
        T successType = gson.fromJson(response.toString(), type);
        onSuccess(successType);
    }

    public abstract void onSuccess(T response);
}