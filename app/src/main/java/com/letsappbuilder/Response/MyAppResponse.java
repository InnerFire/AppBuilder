package com.letsappbuilder.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 22-12-2015.
 */
public class MyAppResponse implements Serializable {
    @SerializedName("MY_APP_LIST")
    public ArrayList<MyAppList> myAppLists = new ArrayList<MyAppList>();

    public class MyAppList {
        @SerializedName("APP_ID")
        public String APP_ID;
        @SerializedName("APP_NAME")
        public String APP_NAME;
        @SerializedName("APP_ICON")
        public String APP_ICON;
        @SerializedName("APP_CATEGORY")
        public String APP_CATEGORY;
        @SerializedName("SENTANT")
        public String SENTANT;
        @SerializedName("PUBLISH_ID")
        public String PUBLISH_ID;

    }
}
