package com.letsappbuilder.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Savaliya Imfotech on 20-09-2016.
 */
public class Common {
    public static final String DASHBOARD = "Dashboard";
    public static final String PORTFOLIO = "Portfolio";
    public static final String ABOUT = "About";
    public static final String CONTACT = "Contact";
    public static final String OUR_WORK = "Our Work";
    public static final String MAP = "Map";
    public static final String CHAT = "Chat";
    public static final String SHARE = "Share";
    public static final String FEEDBACK = "Feedback";
    public static final String QR_CODE = "QR Code";
    public static final String QUIZ = "Quiz";
    public static final String SURVEY = "Survey";
    public static final String DOCUMENT = "Document";
    public static final String VIDEO = "Video";
    public static String[] imageBackgroundURL = {"https://images7.alphacoders.com/305/305307.jpg", "http://lifetrons.net/images/projects/illustration3.jpg", "http://vignette3.wikia.nocookie.net/mixels/images/2/2f/Hyperealistic_Gaben_Chat_Background.png/revision/latest?cb=20150810185233", "http://il9.picdn.net/shutterstock/videos/11847977/thumb/1.jpg", "http://il2.picdn.net/shutterstock/videos/15505084/thumb/1.jpg", "http://www.powerpointhintergrund.com/uploads/nature-blurry-background-wallpaper-27.jpg", "http://www.graphicsfuel.com/wp-content/uploads/2013/08/blurred-texture-background03-preview.jpg", "http://www.graphicsfuel.com/wp-content/uploads/2013/08/backgrounds-preview.jpg", "http://www.okilla.com/uploadfile/1/2013/08/07/11375913405/1375919392_atmosphere-apps-backgrounds-preview-4.png", "http://il3.picdn.net/shutterstock/videos/11364851/thumb/1.jpg", "http://il3.picdn.net/shutterstock/videos/4890467/thumb/1.jpg", "http://il9.picdn.net/shutterstock/videos/11730878/thumb/1.jpg?i10c=img.resize(height:160)", "http://il8.picdn.net/shutterstock/videos/8866834/thumb/1.jpg", "http://il5.picdn.net/shutterstock/videos/16396816/thumb/1.jpg", "http://il8.picdn.net/shutterstock/videos/3257260/thumb/1.jpg", "https://c2.staticflickr.com/8/7137/8168039208_4a1db9b364_b.jpg", "http://photoblog.hk/wordpress/wp-content/uploads/2011/11/bokeh01.jpg"};
    public static String emailPattern = "[a-zA-Z]+[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+";
    public ProgressDialog mProgressDialog;
    Context activity;

    public Common(Context activity) {
        this.activity = activity;
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this.activity);
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


}
