package com.hackathon.gezinti.models.common;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by yutku on 25/03/17.
 */

public class User implements Serializable{
    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("password")
    private String password;
    @SerializedName("tel")
    private String tel;

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public String getTel() {
        return tel;
    }
}
