package com.letsappbuilder.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Admin on 22-12-2015.
 */
public class LoginResponse implements Serializable {
    @SerializedName("uid")
    public String uid;
    @SerializedName("email")
    public String email;
    @SerializedName("password")
    public String password;
    @SerializedName("name")
    public String name;
    @SerializedName("referid")
    public String referid;
    @SerializedName("dp")
    public String dp;

}
