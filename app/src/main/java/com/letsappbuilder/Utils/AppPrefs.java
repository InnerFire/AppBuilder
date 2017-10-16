package com.letsappbuilder.Utils;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefs {

    public static final String USER_PREFS = "USER_PREFS";
    public SharedPreferences appSharedPref;
    public SharedPreferences.Editor prefEditor;
    public String ID = "ID";
    public String MAIL = "MAIL";
    public String PASS = "PASS";
    public String NAME = "NAME";
    public String REFERID = "REFERID";
    public String PROFILEPICTURE = "PROFILEPICTURE";
    public String TEMP_IMAGE = "TEMP_IMAGE";
    public String additionalposition = "additionalposition";
    public String IS_NEW_APP = "IS_NEW_APP";
    public String APP_ID = "APP_ID";
    public String CHAT_APP_ID = "CHAT_APP_ID";
    public String TOKEN = "TOKEN";

    public AppPrefs(Context context) {
        this.appSharedPref = context.getSharedPreferences(USER_PREFS, Activity.MODE_PRIVATE);
        this.prefEditor = appSharedPref.edit();
    }

    public String getUserId() {
        return appSharedPref.getString(ID, "");
    }

    public void setUserId(String _UserId) {
        this.prefEditor.putString(ID, _UserId).commit();
    }

    public String getMAIL() {
        return appSharedPref.getString(MAIL, "");
    }

    public void setMAIL(String _MAIL) {
        this.prefEditor.putString(MAIL, _MAIL).commit();
    }

    public String getPASS() {
        return appSharedPref.getString(PASS, "");
    }

    public void setPASS(String pass) {
        this.prefEditor.putString(PASS, pass).commit();
    }

    public String getNAME() {
        return appSharedPref.getString(NAME, "");
    }

    public void setNAME(String name) {
        this.prefEditor.putString(NAME, name).commit();
    }

    public String getREFERID() {
        return appSharedPref.getString(REFERID, "");
    }

    public void setREFERID(String REFERID) {
        this.prefEditor.putString(this.REFERID, REFERID).commit();
    }

    public String getPROFILEPICTURE() {
        return appSharedPref.getString(PROFILEPICTURE, "");
    }

    public void setPROFILEPICTURE(String PROFILEPICTURE) {
        this.prefEditor.putString(this.PROFILEPICTURE, PROFILEPICTURE).commit();
    }

    public String getTEMP_IMAGE() {
        return appSharedPref.getString(TEMP_IMAGE, "");
    }

    public void setTEMP_IMAGE(String TEMP_IMAGE) {
        this.prefEditor.putString(this.TEMP_IMAGE, TEMP_IMAGE).commit();
    }

    public String getAdditionalposition() {
        return appSharedPref.getString(additionalposition, "");
    }

    public void setAdditionalposition(String additionalposition) {
        this.prefEditor.putString(this.additionalposition, additionalposition).commit();
    }

    public String getIS_NEW_APP() {
        return appSharedPref.getString(IS_NEW_APP, "");
    }

    public void setIS_NEW_APP(String IS_NEW_APP) {
        this.prefEditor.putString(this.IS_NEW_APP, IS_NEW_APP).commit();
    }

    public String getAPP_ID() {
        return appSharedPref.getString(APP_ID, "");
    }

    public void setAPP_ID(String APP_ID) {
        this.prefEditor.putString(this.APP_ID, APP_ID).commit();
    }

    public String getCHAT_APP_ID() {
        return appSharedPref.getString(CHAT_APP_ID, "");
    }

    public void setCHAT_APP_ID(String CHAT_APP_ID) {
        this.prefEditor.putString(this.CHAT_APP_ID, CHAT_APP_ID).commit();
    }

    public String getTOKEN() {
        return appSharedPref.getString(TOKEN, "");
    }

    public void setTOKEN(String TOKEN) {
        this.prefEditor.putString(this.TOKEN, TOKEN).commit();
    }

}
