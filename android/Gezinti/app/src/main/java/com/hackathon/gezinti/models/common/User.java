package com.hackathon.gezinti.models.common;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by yutku on 25/03/17.
 */

public class User implements Serializable{
    @SerializedName("name")
    private String name;
    @SerializedName("password")
    private String password;

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
