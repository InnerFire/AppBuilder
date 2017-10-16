package com.letsappbuilder.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Savaliya Imfotech on 19-04-2016.
 */
public class SignUpResponse implements Serializable {
    @SerializedName("result")
    public String result;
}
