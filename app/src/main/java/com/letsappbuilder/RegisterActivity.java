package com.letsappbuilder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.plus.PlusOneButton;
import com.google.gson.Gson;
import com.letsappbuilder.Response.SignUpResponse;
import com.letsappbuilder.Utils.Common;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class RegisterActivity extends AppCompatActivity {

    private static final int PLUS_ONE_REQUEST_CODE = 0;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.cv_add)
    CardView cvAdd;
    EditText edtEmail, edtPwd, edtrePwd, edtFullName;
    Button btnNext;
    Common common;
    PlusOneButton mPlusOneButton;
    String APP_URL = "https://play.google.com/store/apps/details?id=com.letsappbuilder";
    // ******************************    Enter edtEmail, edtPwd, edtRepeatPwd, edtUname, edtAge, edtContactno, edtCity, edtAddress;
    AsyncHttpClient callSignUpAPIRequest;
    // ###################  Check First time Registration is done API  ######################## //
    AsyncHttpClient callCheckFirstSignUpAPIRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        btnNext = (Button) findViewById(R.id.bt_next);

        mPlusOneButton = (PlusOneButton) findViewById(R.id.plus_one_button);
        //  mPlusOneButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink));
        common = new Common(RegisterActivity.this);
        edtEmail = (EditText) findViewById(R.id.et_email);
        edtPwd = (EditText) findViewById(R.id.et_password);
        edtrePwd = (EditText) findViewById(R.id.et_repeatpassword);
        edtFullName = (EditText) findViewById(R.id.et_fullname);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
        clickHandler();

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

    @Override
    public void onResume() {
        super.onResume();
        mPlusOneButton.initialize(APP_URL, PLUS_ONE_REQUEST_CODE);
    }

    private void clickHandler() {
        // TODO Auto-generated method stub

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtEmail.getText().toString().trim().equals("")) {
                    edtEmail.setError(getString(R.string.email_empty));
                } else if (!edtEmail.getText().toString().matches(Common.emailPattern)) {
                    edtEmail.setError(getString(R.string.email_valid));
                } else if (edtPwd.getText().toString().trim().equals("")) {
                    edtPwd.setError(getString(R.string.password_valid));
                } else if (edtrePwd.getText().toString().trim().equals("")) {
                    edtrePwd.setError(getString(R.string.retypepassword_valid));
                } else if (!edtrePwd.getText().toString().trim().equals(edtPwd.getText().toString())) {
                    edtrePwd.setError(getString(R.string.password_not_match));
                } else if (edtFullName.getText().toString().trim().equals("")) {
                    edtFullName.setError(getString(R.string.name_valid));
                } else {
                    if (common.isConnected()) {
                        common.showProgressDialog(getString(R.string.progrss_registering));
                        CallCheckFirstSignUpApi(edtEmail.getText().toString());
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.message_turn_on_internet, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }

    public void CallSignUpApi(String email, String pass, String name, String refer) {
        if (callSignUpAPIRequest != null) {
            callSignUpAPIRequest.cancelRequests(getApplicationContext(), true);
        }
        callSignUpAPIRequest = new AsyncHttpClient();
        callSignUpAPIRequest.post("http://fadootutorial.com/appgenerator/register.php", RequestSign(email, pass, name, refer), new SignUp_result());
    }

    public RequestParams RequestSign(String email, String pass, String name, String refer) {
        RequestParams params = new RequestParams();

        params.put("email", email);
        params.put("password", pass);
        params.put("name", name);
        params.put("refer", refer);

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), fab.getWidth() / 2);
            mAnimator.setDuration(500);
            mAnimator.setInterpolator(new AccelerateInterpolator());
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    cvAdd.setVisibility(View.INVISIBLE);
                    super.onAnimationEnd(animation);
                    fab.setImageResource(R.drawable.plus);
                    RegisterActivity.super.onBackPressed();
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }
            });
            mAnimator.start();

        } else {
            fab.setImageResource(R.drawable.plus);
            RegisterActivity.super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    public class SignUp_result extends AsyncHttpResponseHandler {


        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
            //  Log.e("$$$", "On Success");
            common.hideProgressDialog();
            try {
                String str = new String(responseBody, "UTF-8");
                //  Log.e("***********", "response is" + str);

                if (str != null) {
                    SignUpResponse response = new Gson().fromJson(str, SignUpResponse.class);
                    //  Log.e("****SignUp*****", "" + response.result);

                    if (response.result.equals("success")) {
                        Toast.makeText(getApplicationContext(), R.string.register_done, Toast.LENGTH_SHORT).show();
                        animateRevealClose();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.message_please_try_later, Toast.LENGTH_SHORT).show();
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
                //   Log.e("***********", "response is" + str);

                if (str != null) {
                    SignUpResponse response = new Gson().fromJson(str, SignUpResponse.class);
                    //  Log.e("****SignUp*****", "" + response.result);

                    if (response.result.trim().equals("error")) {
                        if (common.isConnected()) {
                            common.showProgressDialog(getString(R.string.progrss_registering));
                            CallSignUpApi(edtEmail.getText().toString(), edtPwd.getText().toString(), edtFullName.getText().toString(), getSaltString());
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.message_turn_on_internet, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.message_email_already_registered, Toast.LENGTH_SHORT).show();
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
