package com.letsappbuilder;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.letsappbuilder.Response.LoginResponse;
import com.letsappbuilder.Response.SignUpResponse;
import com.letsappbuilder.Utils.AppPrefs;
import com.letsappbuilder.Utils.Common;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // Google Login
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    public GoogleApiClient mGoogleApiClient;
    public GoogleSignInOptions gso;
    @InjectView(R.id.et_email)
    EditText etUsername;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.bt_go)
    Button btGo;
    @InjectView(R.id.cv)
    CardView cv;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    AppPrefs appPrefs;
    Dialog dialog;
    Common common;
    // Facebook Login
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    ProfileTracker profileTracker;
    String EMAIL, NAME, PASSWORD;
    AsyncHttpClient callForgotPasswordAPIRequest;
    //  **************** Call Login API ******************************
    AsyncHttpClient callLoginAPIRequest;
    AsyncHttpClient callSignUpAPIRequest;
    AsyncHttpClient callSocialLoginAPIRequest;
    // ###################  Check First time Registration is done API  ######################## //
    AsyncHttpClient callCheckFirstSignUpAPIRequest;
    private EditText et_username, et_password;
    private Button btn_go;
    private TextView tvForgotPassword;
    private RelativeLayout root_snackbar;
    private ImageView imgGoogleLogin, imgFacebookLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        dialog = new Dialog(LoginActivity.this);
        common = new Common(getApplicationContext());
        // Google Login //
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        imgGoogleLogin = (ImageView) findViewById(R.id.google_login);
        imgFacebookLogin = (ImageView) findViewById(R.id.fb_login);
        imgGoogleLogin.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.color_item));
        imgFacebookLogin.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.color_item));
        root_snackbar = (RelativeLayout) findViewById(R.id.login_root_snackbar);
        ButterKnife.inject(this);
        common = new Common(LoginActivity.this);
        appPrefs = new AppPrefs(getApplicationContext());
        if (!appPrefs.getUserId().isEmpty()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        et_username = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        tvForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);
        btn_go = (Button) findViewById(R.id.bt_go);
        /*   Facebook Integration ********** */
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    com.facebook.Profile oldProfile,
                    com.facebook.Profile currentProfile) {

                // App code
            }
        };
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                GraphRequest req = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response.getError() != null) {
                            //       Log.e("Error++", " " + response.getError());
                        } else {
                            PASSWORD = getSaltString();
                            EMAIL = object.optString("email");
                            NAME = object.optString("name");
                            if (common.isConnected()) {
                                common.showProgressDialog(getString(R.string.progress_signin));
                                CallCheckFirstSignUpApi(EMAIL);
                            } else {
                                Snackbar.make(root_snackbar, R.string.message_turn_on_internet, Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                Bundle para = new Bundle();
                para.putString("fields", "name,first_name,last_name,email");
                req.setParameters(para);
                req.executeAsync();

            }

            @Override
            public void onCancel() {
                //  Log.e("*****onCancel", "");

            }

            @Override
            public void onError(FacebookException e) {
                //        Log.e("*****onError", "");
            }

        });

        clickHandler();

    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            //  Log.e("IP Address", ex.toString());
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        //    Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //  GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null) {
                PASSWORD = getSaltString();
                EMAIL = acct.getEmail();
                NAME = acct.getDisplayName();
                Log.e("$$$", PASSWORD + "" + EMAIL + NAME);
            }

            if (common.isConnected()) {
                common.showProgressDialog(getString(R.string.progress_signin));
                CallCheckFirstSignUpApi(EMAIL);
            } else {
                Snackbar.make(root_snackbar, R.string.message_turn_on_internet, Snackbar.LENGTH_LONG).show();
            }


        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
        //  mGoogleApiClient.disconnect();

    }

    private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();

    }

/*
    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toString().getBytes());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }
*/
/* **************  Call Forgot Password API   *************************  */

    @Override
    public void onDestroy() {
        super.onDestroy();

        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick({R.id.bt_go, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setExitTransition(null);
                    getWindow().setEnterTransition(null);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
           /* case R.id.bt_go:
                Explode explode = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    explode = new Explode();
                    explode.setDuration(500);
                    getWindow().setExitTransition(explode);
                    getWindow().setEnterTransition(explode);
                    ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
                    Intent i2 = new Intent(this, MainActivity.class);
                    startActivity(i2, oc2.toBundle());
                } else {
                    startActivity(new Intent(this, MainActivity.class));
                }
                break;
*/
        }
    }

    private void clickHandler() {
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_username.getText().toString().trim().equals("")) {
                    et_username.setError(getString(R.string.email_empty));
                } else if (!et_username.getText().toString().matches(Common.emailPattern)) {
                    et_username.setError(getString(R.string.email_valid));
                } else if (et_password.getText().toString().trim().equals("")) {
                    et_password.setError(getString(R.string.password_valid));
                } else {
                    if (common.isConnected()) {
                        common.showProgressDialog(getString(R.string.progress_signin));
                        CallLoginApi(et_username.getText().toString(), et_password.getText().toString());
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.message_turn_on_internet, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.setContentView(R.layout.forgot_password_dialog);

                dialog.setCancelable(true);
                final EditText edtForgotEmail = (EditText) dialog.findViewById(R.id.edt_ForogtPassword);
                Button btn = (Button) dialog.findViewById(R.id.bt_go_forgot_password);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtForgotEmail.getText().toString().trim().equals("")) {
                            edtForgotEmail.setError(getString(R.string.email_empty));
                        } else if (!edtForgotEmail.getText().toString().matches(Common.emailPattern)) {
                            edtForgotEmail.setError(getString(R.string.email_valid));
                        } else {
                            // ************************* Calling Forgot Password API   *****************
                            if (common.isConnected()) {
                                common.showProgressDialog(getString(R.string.progress_loading));
                                CallForgotPasswordAPI(edtForgotEmail.getText().toString());
                            } else {
                                Snackbar.make(root_snackbar, R.string.message_turn_on_internet, Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                dialog.show();
            }
        });
        imgGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (common.isConnected()) {
                    signIn();
                } else {
                    Snackbar.make(root_snackbar, R.string.message_turn_on_internet, Snackbar.LENGTH_LONG).show();
                }
            }
        });
        imgFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (common.isConnected()) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
                } else {
                    Snackbar.make(root_snackbar, R.string.message_turn_on_internet, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void CallForgotPasswordAPI(String email) {
        if (callForgotPasswordAPIRequest != null) {
            callForgotPasswordAPIRequest.cancelRequests(getApplicationContext(), true);
        }

        callForgotPasswordAPIRequest = new AsyncHttpClient();
        callForgotPasswordAPIRequest.post("http://fadootutorial.com/appgenerator/forgotpassword.php", RequestForgotPassword(email), new Forgot_Password_Result());
    }

    public RequestParams RequestForgotPassword(String email) {
        RequestParams params = new RequestParams();
        params.put("email", email);
        return params;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void CallLoginApi(String email, String pass) {
        if (callLoginAPIRequest != null) {
            callLoginAPIRequest.cancelRequests(getApplicationContext(), true);
        }
        callLoginAPIRequest = new AsyncHttpClient();
        callLoginAPIRequest.post("http://fadootutorial.com/appgenerator/login.php", RequestLogin(email, pass), new Login_result());
    }

    // ##################  Call Register API  ##################### //

    public RequestParams RequestLogin(String email, String pass) {
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", pass);
        return params;
    }

    public void CallSocialSignUpApi(String email, String pass, String name, String refer) {
        if (callSignUpAPIRequest != null) {
            callSignUpAPIRequest.cancelRequests(getApplicationContext(), true);
        }
        callSignUpAPIRequest = new AsyncHttpClient();
        callSignUpAPIRequest.post("http://fadootutorial.com/appgenerator/socialregister.php", RequestSign(email, pass, name, refer), new SignUp_result());
    }

    public RequestParams RequestSign(String email, String pass, String name, String refer) {
        RequestParams params = new RequestParams();

        params.put("email", email);
        params.put("password", pass);
        params.put("name", name);
        params.put("refer", refer);

        return params;
    }

    public void CallSocialLoginApi(String email) {
        if (callSocialLoginAPIRequest != null) {
            callSocialLoginAPIRequest.cancelRequests(getApplicationContext(), true);
        }
        callSocialLoginAPIRequest = new AsyncHttpClient();
        callSocialLoginAPIRequest.post("http://fadootutorial.com/appgenerator/sociallogin.php", RequestSocialLogin(email), new SocialLogin_result());
    }
    // ##################  Call Social Login API  ##################### //

    public RequestParams RequestSocialLogin(String email) {
        RequestParams params = new RequestParams();
        params.put("email", email);
        return params;
    }

    public void CallCheckFirstSignUpApi(String email) {
        if (callCheckFirstSignUpAPIRequest != null) {
            callCheckFirstSignUpAPIRequest.cancelRequests(getApplicationContext(), true);
        }
        callCheckFirstSignUpAPIRequest = new AsyncHttpClient();
        callCheckFirstSignUpAPIRequest.post("http://fadootutorial.com/appgenerator/checkfirsttime.php", RequestCheckFirstSign(email), new CheckFirstSignUp_result());
    }

    public RequestParams RequestCheckFirstSign(String email) {
        RequestParams params = new RequestParams();
        params.put("email", email);
        return params;
    }

    public class Forgot_Password_Result extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            common.hideProgressDialog();
            try {
                String str = new String(responseBody, "UTF-8");
                //  Log.e("***********", "response is" + str);

                if (str != null) {
                    SignUpResponse response = new Gson().fromJson(str, SignUpResponse.class);
                    if (response.result.equals("success")) {
                        Snackbar.make(root_snackbar, R.string.message_emal_send_success, Snackbar.LENGTH_LONG).show();
                        dialog.cancel();
                    } else {
                        Snackbar.make(root_snackbar, R.string.message_email_not_exist, Snackbar.LENGTH_LONG).show();
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            common.hideProgressDialog();
            Snackbar.make(root_snackbar, R.string.message_something_went_wrong, Snackbar.LENGTH_LONG).show();
        }
    }

    public class Login_result extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            //    Log.e("$$$", "On Success");
            common.hideProgressDialog();
            try {
                String str = new String(responseBody, "UTF-8");
                //      Log.e("***********", "response is" + str);

                if (str != null) {
                    LoginResponse response = new Gson().fromJson(str, LoginResponse.class);
                    if (response.uid.equals("ERROR")) {
                        Snackbar.make(root_snackbar, R.string.invalid_uname_password, Snackbar.LENGTH_LONG).show();
                    } else {
                        appPrefs.setUserId(response.uid);
                        appPrefs.setMAIL(response.email);
                        appPrefs.setPASS(response.password);
                        appPrefs.setNAME(response.name);
                        appPrefs.setREFERID(response.referid);
                        appPrefs.setPROFILEPICTURE(response.dp);

                      /*  Explode explode = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            explode = new Explode();
                            explode.setDuration(500);
                            getWindow().setExitTransition(explode);
                            getWindow().setEnterTransition(explode);
                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                            Intent i2 = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i2, oc2.toBundle());
                            finish();
                        } else {
                      */
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        // }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            common.hideProgressDialog();
            Toast.makeText(getApplicationContext(), R.string.message_something_went_wrong, Toast.LENGTH_SHORT).show();
        }
    }

    public class SignUp_result extends AsyncHttpResponseHandler {


        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
            //  Log.e("$$$", "On Success");
            common.hideProgressDialog();
            try {
                String str = new String(responseBody, "UTF-8");
                //      Log.e("***********", "response is" + str);

                if (str != null) {
                    LoginResponse response = new Gson().fromJson(str, LoginResponse.class);
                    if (response.uid.equals("ERROR")) {
                        Snackbar.make(root_snackbar, R.string.message_request_not_complete, Snackbar.LENGTH_LONG).show();
                    } else {
                        appPrefs.setUserId(response.uid);
                        appPrefs.setMAIL(response.email);
                        appPrefs.setPASS(response.password);
                        appPrefs.setNAME(response.name);
                        appPrefs.setREFERID(response.referid);
                        appPrefs.setPROFILEPICTURE(response.dp);

                      /*  Explode explode = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            explode = new Explode();
                            explode.setDuration(500);
                            getWindow().setExitTransition(explode);
                            getWindow().setEnterTransition(explode);
                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                            Intent i2 = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i2, oc2.toBundle());
                            finish();
                        } else {
                         */
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        //   }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
            common.hideProgressDialog();
            Toast.makeText(getApplicationContext(), R.string.message_something_went_wrong, Toast.LENGTH_SHORT).show();
        }

    }

    public class SocialLogin_result extends AsyncHttpResponseHandler {


        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
            //  Log.e("$$$", "On Success");
            common.hideProgressDialog();
            try {
                String str = new String(responseBody, "UTF-8");
                //      Log.e("***********", "response is" + str);

                if (str != null) {
                    LoginResponse response = new Gson().fromJson(str, LoginResponse.class);
                    if (response.uid.equals("ERROR")) {
                        Snackbar.make(root_snackbar, R.string.message_request_not_complete, Snackbar.LENGTH_LONG).show();
                    } else {
                        appPrefs.setUserId(response.uid);
                        appPrefs.setMAIL(response.email);
                        appPrefs.setPASS(response.password);
                        appPrefs.setNAME(response.name);
                        appPrefs.setREFERID(response.referid);
                        appPrefs.setPROFILEPICTURE(response.dp);

                        /*Explode explode = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            explode = new Explode();
                            explode.setDuration(500);
                            getWindow().setExitTransition(explode);
                            getWindow().setEnterTransition(explode);
                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                            Intent i2 = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i2, oc2.toBundle());
                            finish();
                        } else {*/
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        // }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
            common.hideProgressDialog();
            Toast.makeText(getApplicationContext(), R.string.message_something_went_wrong, Toast.LENGTH_SHORT).show();
        }

    }

    public class CheckFirstSignUp_result extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
            //  Log.e("$$$", "On Success");
            common.hideProgressDialog();
            try {
                String str = new String(responseBody, "UTF-8");
                //  Log.e("***********", "response is" + str);

                if (str != null) {
                    SignUpResponse response = new Gson().fromJson(str, SignUpResponse.class);

                    if (response.result.trim().equals("error")) {
                        if (common.isConnected()) {
                            common.showProgressDialog(getString(R.string.progress_signin));
                            CallSocialSignUpApi(EMAIL, PASSWORD, NAME, PASSWORD);
                        } else {
                            Snackbar.make(root_snackbar, R.string.message_turn_on_internet, Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        if (common.isConnected()) {
                            common.showProgressDialog(getString(R.string.progress_signin));
                            CallSocialLoginApi(EMAIL);
                        } else {
                            Snackbar.make(root_snackbar, R.string.message_turn_on_internet, Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
            common.hideProgressDialog();
            Toast.makeText(getApplicationContext(), R.string.message_something_went_wrong, Toast.LENGTH_SHORT).show();
        }
    }

}
