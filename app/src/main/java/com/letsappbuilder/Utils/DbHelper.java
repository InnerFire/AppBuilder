package com.letsappbuilder.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.letsappbuilder.Adapter.AppDetailClass;
import com.letsappbuilder.MainActivity;

import java.util.ArrayList;

/**
 * Created by Admin on 21-01-2016.
 */
public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "appbuilder.db";
    public static final String TABLE_NAME = "APP_DETAIL";
    public static final int DATABASE_VERSION = 1;

    public static final String APP_ID = "APP_ID";
    public static final String APP_NAME = "APP_NAME";
    public static final String APP_ICON = "APP_ICON";
    public static final String SPLASH_ICON = "SPLASH_ICON";
    public static final String APP_CATEGORY = "APP_CATEGORY";
    public static final String APP_THEME = "APP_THEME";
    public static final String THEME_COLOR = "THEME_COLOR";
    public static final String TEXT_COLOR = "TEXT_COLOR";
    public static final String APP_PAGES = "APP_PAGES";
    public static final String APP_PAGES_ID = "APP_PAGES_ID";
    public static final String PUBLISH_ID = "PUBLISH_ID";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + APP_ID + " text NOT NULL PRIMARY KEY ," + APP_NAME + " text ," + SPLASH_ICON + " text ," + APP_ICON + " text ," + APP_CATEGORY + " text ," + THEME_COLOR + " text ," + TEXT_COLOR + " text ," + APP_THEME + " text ," + APP_PAGES + " text ," + APP_PAGES_ID + " text ," + PUBLISH_ID + " text  );";

    Context context;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        Log.e("********", "Constructor called");
    }


    public void insertSelectionPhasedata(String app_id, String app_name, String app_category, String app_theme, String position) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(APP_ID, app_id);
        contentValues.put(APP_NAME, app_name);
        contentValues.put(APP_CATEGORY, app_category);
        contentValues.put(APP_THEME, app_theme);
        contentValues.put(SPLASH_ICON, MainActivity.splash_image_set[Integer.parseInt(position)]);
        contentValues.put(APP_ICON, MainActivity.app_icon_set[Integer.parseInt(position)]);
        contentValues.put(THEME_COLOR, "-10011977");
        contentValues.put(TEXT_COLOR, "sans-serif-smallcaps");
        contentValues.put(APP_PAGES, "0");
        contentValues.put(APP_PAGES_ID, "0");
        contentValues.put(PUBLISH_ID, "NILL");

        long id = db.insert(DbHelper.TABLE_NAME, null, contentValues);
        Log.e("insertSeldata***", id + "");
    }

    public void DeleteFromMyApps(String app_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String queryUpdate = "DELETE FROM " + TABLE_NAME + " WHERE " + APP_ID + "='" + app_id + "'";
            db.execSQL(queryUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DeleteDataWhileLogout() {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String queryUpdate = "DELETE FROM " + TABLE_NAME;
            db.execSQL(queryUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateSelectionOnePhasedata(String app_id, String app_name, String app_category) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String queryUpdate = "UPDATE " + TABLE_NAME + " SET " + APP_NAME + "='" + app_name + "', " + APP_CATEGORY + "='" + app_category + "' WHERE " + APP_ID + "='" + app_id + "'";
            db.execSQL(queryUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateSelectionTwoPhasedata(String app_id, String app_theme) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String queryUpdate = "UPDATE " + TABLE_NAME + " SET " + APP_THEME + "='" + app_theme + "' WHERE " + APP_ID + "='" + app_id + "'";
            db.execSQL(queryUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateAttributeDesignOnedata(String app_id, String attribute, String value) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String queryUpdate = "UPDATE " + TABLE_NAME + " SET " + attribute + "='" + value + "' WHERE " + APP_ID + "='" + app_id + "'";
            db.execSQL(queryUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateDesignTwoPhasedata(String app_id, String app_name, String app_category, String app_icon, String splash_icon, String app_theme_color, String text_color) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String queryUpdate = "UPDATE " + TABLE_NAME + " SET " + APP_NAME + "='" + app_name + "', " + APP_CATEGORY + "='" + app_category + "', " + APP_ICON + "='" + app_icon + "', " + SPLASH_ICON + "='" + splash_icon + "', " + THEME_COLOR + "='" + app_theme_color + "', " + TEXT_COLOR + "='" + text_color + "' WHERE " + APP_ID + "='" + app_id + "'";
            db.execSQL(queryUpdate);
            Log.e("$$$$", "Updated");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateFreshDataFromServerToLocal(String APP_ID, String APP_NAME, String APP_ICON, String SPLASH_ICON, String APP_CATEGORY, String APP_THEME, String THEME_COLOR, String TEXT_COLOR, String PUBLISH_ID, String APP_PAGE, String APP_PAGES_ID) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String queryUpdate = "UPDATE " + TABLE_NAME + " SET " + DbHelper.APP_NAME + "='" + APP_NAME + "', " + DbHelper.APP_ICON + "='" + APP_ICON + "', " + DbHelper.SPLASH_ICON + "='" + SPLASH_ICON + "', " + DbHelper.APP_CATEGORY + "='" + APP_CATEGORY + "', " + DbHelper.APP_THEME + "='" + APP_THEME + "', " + DbHelper.THEME_COLOR + "='" + THEME_COLOR + "', " + DbHelper.TEXT_COLOR + "='" + TEXT_COLOR + "', " + DbHelper.PUBLISH_ID + "='" + PUBLISH_ID + "', " + DbHelper.APP_PAGES_ID + "='" + APP_PAGES_ID + "', " + DbHelper.APP_PAGES + "='" + APP_PAGE + "' WHERE " + DbHelper.APP_ID + "='" + APP_ID + "'";
            db.execSQL(queryUpdate);
            Log.e("!!!", "Updated");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void InsertFreshDataFromServerToLocal(String APP_ID, String APP_NAME, String APP_ICON, String SPLASH_ICON, String APP_CATEGORY, String APP_THEME, String THEME_COLOR, String TEXT_COLOR, String PUBLISH_ID, String APP_PAGE, String APP_PAGES_ID) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DbHelper.APP_ID, APP_ID);
            contentValues.put(DbHelper.APP_NAME, APP_NAME);
            contentValues.put(DbHelper.APP_CATEGORY, APP_CATEGORY);
            contentValues.put(DbHelper.APP_THEME, APP_THEME);
            contentValues.put(DbHelper.SPLASH_ICON, SPLASH_ICON);
            contentValues.put(DbHelper.APP_ICON, APP_ICON);
            contentValues.put(DbHelper.THEME_COLOR, THEME_COLOR);
            contentValues.put(DbHelper.TEXT_COLOR, TEXT_COLOR);
            contentValues.put(DbHelper.APP_PAGES, APP_PAGE);
            contentValues.put(DbHelper.APP_PAGES_ID, APP_PAGES_ID);

            contentValues.put(DbHelper.PUBLISH_ID, PUBLISH_ID);

            long id = db.insert(DbHelper.TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String SelectAttributeviseData(String appid, String ATTRIBUTE) {
        SQLiteDatabase db = this.getWritableDatabase();
        String result = "NULL";
        try {
            String query = "SELECT " + ATTRIBUTE + " FROM " + TABLE_NAME + " WHERE " + APP_ID + " = '" + appid + "'";
            Cursor resultset = db.rawQuery(query, null);
            while (resultset.moveToNext()) {
                result = resultset.getString(resultset.getColumnIndex(ATTRIBUTE));
            }
            resultset.close();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return result;
    }

    public boolean isNewApp(String appid) {
        boolean flag = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT " + APP_ID + " FROM " + TABLE_NAME + " WHERE " + APP_ID + " = '" + appid + "'";
            Cursor resultset = db.rawQuery(query, null);
            Log.e("in function%%", resultset.getCount() + "");
            while (resultset.moveToNext()) {
                if (resultset.getString(resultset.getColumnIndex(APP_ID)).equals(appid)) {
                    flag = true;
                }
            }
            resultset.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return flag;
    }


    public ArrayList<AppDetailClass> GetDraftAppDetails() {
        ArrayList<AppDetailClass> Draft_app_list = new ArrayList<AppDetailClass>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PUBLISH_ID + " = 'NILL'";
            Cursor resultset = db.rawQuery(query, null);
            while (resultset.moveToNext()) {
                AppDetailClass appDetailClass = new AppDetailClass();
                appDetailClass.setAPP_ID(resultset.getString(resultset.getColumnIndex(APP_ID)));
                appDetailClass.setAPP_CATEGORY(resultset.getString(resultset.getColumnIndex(APP_CATEGORY)));
                appDetailClass.setAPP_ICON(resultset.getString(resultset.getColumnIndex(APP_ICON)));
                appDetailClass.setAPP_NAME(resultset.getString(resultset.getColumnIndex(APP_NAME)));
                appDetailClass.setPUBLISH_ID(resultset.getString(resultset.getColumnIndex(PUBLISH_ID)));
                Draft_app_list.add(appDetailClass);
            }
            resultset.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return Draft_app_list;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("********", "on create called");
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("********", "on Upgrade called");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void ondelete() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.e("********", "ondelete called");
        onCreate(db);
    }
}
