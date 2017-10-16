package com.letsappbuilder.FCM_Package;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.letsappbuilder.Response.SignUpResponse;
import com.letsappbuilder.Utils.AppPrefs;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;


/**
 * Created by Belal on 03/11/16.
 */


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "My####Firebase";
    //  ####################   Token Register Api   ###################
    AsyncHttpClient callTokenRegisterAPIRequest;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //  Log.e(TAG, "Refreshed token: " + refreshedToken);
        storeToken(refreshedToken);
    }

    private void storeToken(String token) {
        //saving the token on shared preferences
        AppPrefs appPrefs = new AppPrefs(getApplicationContext());
        appPrefs.setTOKEN(token);
        //   if(!appPrefs.getAPP_ID().isEmpty())
        //  CallTokenRegisterApi(appPrefs.getCHAT_APP_ID(),appPrefs.getUserId(),appPrefs.getMAIL(),token);
    }

    public void CallTokenRegisterApi(String appid, String userid, String email, String token) {
        if (callTokenRegisterAPIRequest != null) {
            callTokenRegisterAPIRequest.cancelRequests(getApplicationContext(), true);
        }
        callTokenRegisterAPIRequest = new AsyncHttpClient();
        callTokenRegisterAPIRequest.post("http://fadootutorial.com/FcmExample/UpdateTokenDevice.php", RequestTokenRegister(appid, userid, email, token), new RequestToken_result());
    }

    public RequestParams RequestTokenRegister(String appid, String userid, String email, String token) {
        RequestParams params = new RequestParams();

        params.put("app_id", appid);
        params.put("user_id", userid);
        params.put("email", email);
        params.put("token", token);

        return params;
    }

    public class RequestToken_result extends AsyncHttpResponseHandler {


        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
            //  Log.e("$$$", "On Success");
            try {
                String str = new String(responseBody, "UTF-8");
                //  Log.e("***********", "response is" + str);

                if (str != null) {
                    SignUpResponse response = new Gson().fromJson(str, SignUpResponse.class);
                    //  Log.e("****SignUp*****", "" + response.result);

                    if (response.result.equals("success")) {
                        // Log.e("###", "Device Token Updated succesfully");

                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
            //  Log.e("###", "Something went wrong");
        }


    }

}