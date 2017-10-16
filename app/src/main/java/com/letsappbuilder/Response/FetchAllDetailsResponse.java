package com.letsappbuilder.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Admin on 22-12-2015.
 */
public class FetchAllDetailsResponse implements Serializable {
    @SerializedName("APP_ID")
    public String APP_ID;
    @SerializedName("APP_NAME")
    public String APP_NAME;
    @SerializedName("APP_ICON")
    public String APP_ICON;
    @SerializedName("SPLASH_ICON")
    public String SPLASH_ICON;
    @SerializedName("APP_CATEGORY")
    public String APP_CATEGORY;

    @SerializedName("APP_THEME")
    public String APP_THEME;
    @SerializedName("THEME_COLOR")
    public String THEME_COLOR;
    @SerializedName("TEXT_COLOR")
    public String TEXT_COLOR;

    @SerializedName("PUBLISH_ID")
    public String PUBLISH_ID;
    @SerializedName("APP_PAGE")
    public String APP_PAGE;
    @SerializedName("APP_PAGES_ID")
    public String APP_PAGES_ID;

}
