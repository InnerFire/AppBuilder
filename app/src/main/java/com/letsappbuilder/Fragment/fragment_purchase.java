package com.letsappbuilder.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.letsappbuilder.MainActivity;
import com.letsappbuilder.R;
import com.letsappbuilder.Response.SignUpResponse;
import com.letsappbuilder.Utils.AppPrefs;
import com.letsappbuilder.Utils.Common;
import com.letsappbuilder.Utils.DbHelper;
import com.letsappbuilder.util.IabHelper;
import com.letsappbuilder.util.IabResult;
import com.letsappbuilder.util.Inventory;
import com.letsappbuilder.util.Purchase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class fragment_purchase extends Fragment {
    //   App billing
    private static final String TAG = "InAppBilling";
    //  static final String ITEM_SKU = "wallpaper.changer";
    public String ITEM_SKU;
    ImageView imgAppIcon;
    TextView txtAppName;
    Common common;
    DbHelper dbHelper;
    ScrollView rootPurchase;
    AppPrefs appPrefs;
    String PLAN_TYPE;
    Button btnmonth1, btnmonth3, btnmonth6, btnyear1, btnyear5, btnyear10;
    IabHelper mHelper;
    //  **************** Call Update_Details API ******************************//
    AsyncHttpClient callAppDetailsAPIRequest;
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                @Override
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    if (result.isSuccess()) {
                        if (common.isConnected()) {
                            common.showProgressDialog(getString(R.string.payment_in_progress));
                            CallAppDetailsApi(appPrefs.getUserId(), dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.APP_ID), PLAN_TYPE);
                        } else {
                            Snackbar snackbar = Snackbar.make(rootPurchase, R.string.message_turn_on_internet, Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.firstColor));
                            snackbar.show();
                        }
                    }
                }
            };
    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                Toast.makeText(getActivity(), R.string.message_please_try_later, Toast.LENGTH_SHORT).show();
            } else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                        mConsumeFinishedListener);
            }
        }
    };
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase) {
            if (result.isFailure()) {
                Toast.makeText(getActivity(), R.string.message_something_went_wrong, Toast.LENGTH_SHORT).show();
            } else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_purchase, container, false);
        MainActivity.frameToolbar.setVisibility(View.GONE);
        MainActivity.secondframeToolbar.setVisibility(View.VISIBLE);
        MainActivity.secondframeToolbar.setBackgroundColor(getResources().getColor(R.color.firstColor));
        MainActivity.animator(getActivity(), MainActivity.secondimgMainNext);
        MainActivity.animator(getActivity(), MainActivity.secondimgMainPrev);
        MainActivity.tv_main_second.setText(R.string.toolbar_upgrade_plan);
        common = new Common(this.getActivity());
        appPrefs = new AppPrefs(getActivity());
        dbHelper = new DbHelper(getActivity());
        rootPurchase = (ScrollView) v.findViewById(R.id.root_purchase_allappdetails);
        imgAppIcon = (ImageView) v.findViewById(R.id.img_app_icon);
        txtAppName = (TextView) v.findViewById(R.id.tv_app_name);
        btnmonth1 = (Button) v.findViewById(R.id.bt_month1);
        btnmonth3 = (Button) v.findViewById(R.id.bt_month3);
        btnmonth6 = (Button) v.findViewById(R.id.bt_month6);
        btnyear1 = (Button) v.findViewById(R.id.bt_year);
        btnyear5 = (Button) v.findViewById(R.id.bt_year5);
        btnyear10 = (Button) v.findViewById(R.id.bt_year10);

        String base64EncodedPublicKey =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmD5o26lr2Z8rNOicq/pH7SmcoyWJMDvnMTL70rjrUa4yoZPuDMihejBEWMKqjcsXUCMBHLU+T6DKAeaL/CRxtypM27jcCcH9vTHvjecAlTQGMtcDpfDmI9Up6I7WoeyE/6wGnbA/4ef/zwiB5kQ9COZz9kEmCAFSi5wTA80182dVhnNFwriVi1my/Ve6YYsSSyteUWpMciLmoNh/fs5fVBNZbnAJd3mZCk3DfBF8fotUT+d3Ia2yTdZpd/ITfLRZii5832mnR71W42rYs/xeR3CHaB1Sx2tEjg3MUeAMkGwKljBSEXzAWROpl57QxNjFd5EjqSMxCLmG3fCzocUYmwIDAQAB";

        mHelper = new IabHelper(getActivity(), base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.e(TAG, "In-app Billing setup failed: " +
                            result);
                } else {
                    Log.e(TAG, "In-app Billing is set up OK");
                }
            }
        });

        txtAppName.setText(getArguments().getString("APP_NAME") + " (Offline)");
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        imageLoader.displayImage(getArguments().getString("APP_ICON"), imgAppIcon, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    }

                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String s, View view, int i, int i1) {
                    }
                }
        );

        btnmonth1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PLAN_TYPE = "1M";
                ITEM_SKU = "new_month1_upgrade";
                if (mHelper != null) {
                    try {
                        mHelper.launchPurchaseFlow(getActivity(), ITEM_SKU, 10001,
                                mPurchaseFinishedListener, "");
                    } catch (IllegalStateException ex) {
                        Toast.makeText(getActivity(), "Please retry in a few seconds.", Toast.LENGTH_SHORT).show();
                        mHelper.flagEndAsync();
                    }
                }
            }
        });
        btnmonth3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PLAN_TYPE = "3M";
                ITEM_SKU = "new_month3_upgrade";

                if (mHelper != null) {
                    try {
                        mHelper.launchPurchaseFlow(getActivity(), ITEM_SKU, 10001,
                                mPurchaseFinishedListener, "");
                    } catch (IllegalStateException ex) {
                        Toast.makeText(getActivity(), "Please retry in a few seconds.", Toast.LENGTH_SHORT).show();
                        mHelper.flagEndAsync();
                    }
                }
            }
        });
        btnmonth6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PLAN_TYPE = "6M";
                ITEM_SKU = "new_month6_upgrade";

                if (mHelper != null) {
                    try {
                        mHelper.launchPurchaseFlow(getActivity(), ITEM_SKU, 10001,
                                mPurchaseFinishedListener, "");
                    } catch (IllegalStateException ex) {
                        Toast.makeText(getActivity(), "Please retry in a few seconds.", Toast.LENGTH_SHORT).show();
                        mHelper.flagEndAsync();
                    }
                }
            }
        });
        btnyear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PLAN_TYPE = "1Y";
                ITEM_SKU = "new_year1_upgrade";

                if (mHelper != null) {
                    try {
                        mHelper.launchPurchaseFlow(getActivity(), ITEM_SKU, 10001,
                                mPurchaseFinishedListener, "");
                    } catch (IllegalStateException ex) {
                        Toast.makeText(getActivity(), "Please retry in a few seconds.", Toast.LENGTH_SHORT).show();
                        mHelper.flagEndAsync();
                    }
                }
            }
        });
        btnyear5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PLAN_TYPE = "5Y";
                ITEM_SKU = "new_year5_upgrade";
                if (mHelper != null) {
                    try {
                        mHelper.launchPurchaseFlow(getActivity(), ITEM_SKU, 10001,
                                mPurchaseFinishedListener, "");
                    } catch (IllegalStateException ex) {
                        Toast.makeText(getActivity(), "Please retry in a few seconds.", Toast.LENGTH_SHORT).show();
                        mHelper.flagEndAsync();
                    }
                }
            }
        });
        btnyear10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PLAN_TYPE = "10Y";
                ITEM_SKU = "new_year10_upgrade";
                if (mHelper != null) {
                    try {
                        mHelper.launchPurchaseFlow(getActivity(), ITEM_SKU, 10001,
                                mPurchaseFinishedListener, "");
                    } catch (IllegalStateException ex) {
                        Toast.makeText(getActivity(), "Please retry in a few seconds.", Toast.LENGTH_SHORT).show();
                        mHelper.flagEndAsync();
                    }
                }
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    public void CallAppDetailsApi(String UID, String APP_ID, String PLAN_TYPE) {
        if (callAppDetailsAPIRequest != null) {
            callAppDetailsAPIRequest.cancelRequests(getActivity(), true);
        }
        callAppDetailsAPIRequest = new AsyncHttpClient();
        callAppDetailsAPIRequest.post("http://fadootutorial.com/appgenerator/updatepurchasedetails.php", RequestAppDetailsParams(UID, APP_ID, PLAN_TYPE), new All_APP_DETAILS_result());
    }

    public RequestParams RequestAppDetailsParams(String UID, String APP_ID, String PLAN_TYPE) {
        RequestParams params = new RequestParams();
        params.put("UID", UID);
        params.put("APP_ID", APP_ID);
        params.put("PLAN_TYPE", PLAN_TYPE);
        return params;
    }

    public class All_APP_DETAILS_result extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            common.hideProgressDialog();
            try {
                String str = new String(responseBody, "UTF-8");
                if (str != null) {
                    SignUpResponse response = new Gson().fromJson(str, SignUpResponse.class);
                    if (response.result.equals("appdetailsuccess")) {
                        Fragment fragment = new fragment_purchase_successful();
                        Bundle bundle = new Bundle();
                        bundle.putString("APP_NAME", dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.APP_NAME));
                        bundle.putString("PUBLISH_ID", dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.PUBLISH_ID));
                        fragment.setArguments(bundle);
                        if (fragment != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.fourthColor));
                            }
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();
                        } else {
                            //  Log.e("Home", "Error in creating fragment");
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(rootPurchase, R.string.message_payment_not_updated, Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.firstColor));
                        snackbar.show();
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            common.hideProgressDialog();
            Snackbar snackbar = Snackbar.make(rootPurchase, R.string.message_please_try_later, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.firstColor));
            snackbar.show();
        }
    }
}
