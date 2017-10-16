package com.letsappbuilder.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.letsappbuilder.Adapter.DocumentAdapter;
import com.letsappbuilder.Adapter.ListItemSelectionOne;
import com.letsappbuilder.Adapter.PortfolioSpinner3AAdapter;
import com.letsappbuilder.Adapter.ProductDetailsAdapter;
import com.letsappbuilder.Adapter.UserLocation;
import com.letsappbuilder.MainActivity;
import com.letsappbuilder.R;
import com.letsappbuilder.Response.SignUpResponse;
import com.letsappbuilder.Utils.AppPrefs;
import com.letsappbuilder.Utils.Common;
import com.letsappbuilder.Utils.DbHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

import static com.letsappbuilder.R.id.design_one_frame_llout;

/**
 * Created by Savaliya Imfotech on 24-05-2016.
 */

public class fragment_design_one extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    FrameLayout design_one_framellout;
    StringBuilder splitString;
    AppPrefs appPrefs;
    Common common;
    int savePosition = 0;
    Boolean isOnImagePressedCheck = false;
    String WhoseAdapter;
    Bitmap UniversalBitmap;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final int REQUEST_PLACE_PICKER = 22;
    ImageLoader imageLoader;
    LinearLayout rootDesignOne;

    public RecyclerView recyclerViewRight;
    public RecyclerView.LayoutManager layoutManagerright;
    public RecyclerView.Adapter adapterright;
    CardPortfolioAdapter MaincardPortfolioAdapter;
    List<String> AttributeCheckList;

    // Dashboard identifier
    List<String> imageDashboardArray, imagePortfolioArray;
    List<ProductDetailsAdapter> Adapter_Dashboard_Deals_of_the_day;
    List<ProductDetailsAdapter> Adapter_Dashboard_Descounts_for_you;
    List<ProductDetailsAdapter> Adapter_Dashboard_ColorfulCards;
    CardDashBoardAdapter MaincardDashboardAdapter;
    CardDashBoard_Deals_of_Adapter MaincardDashboard_Dealofday_Adapter;
    CardDashBoard_Discount_for_you_Adapter MaincardDashboard_Discountforyou_Adapter;

    String edttitle0B, edttitle0C, edttitle0D;
    String spinnertitle0B, spinnertitle0C, spinnertitle0D;
    int[] orientationtheme = {R.drawable.spinner_portfolio_theme3, R.drawable.spinner_portfolio_theme2, R.drawable.spinner_portfolio_theme1};

    // Document identifier
    List<DocumentAdapter> Adapter_Documents;
    String spinnertitle12A;

    //  About us identifier //
    String edtAbout1A, edtAbout1B, edtAbout1C, edtAbout1D, edtAbout1E, edtAbout1F, edtAbout1G, edtAbout1H, edtAbout1I, edtAbout1J, edtAbout1K, edtAbout1L;
    //  Contact us identifier //
    String edtContact2A, edtContact2B, edtContact2C, edtContact2D, edtContact2E, edtContact2F, edtContact2G;
    //  Map identifier //
    UserLocation userLocation;

    // Our Works identifier
    String edtOurWorks4A;

    // Chat identifier //
    Boolean IsChatFeatureON = false;

    // Share identifier //
    String edtShare7A;

    // VIDEO identifier //
    String edtVideo13A;

    // Quiz identifier
    String edtQuiz10A;

    // Survey identifier
    String edtSurvey11A;
    // QR Code identifier //
    String edtQRCode9A = "https://play.google.com/store/apps/details?id=bhojpuri.hitsongs";

    // Feedback identifier //
    Boolean IsFeedbackFeatureON = false;

    // Portfolio identifier //
    String edttitle3A;
    int[] imageSpinnerthemelist = {R.drawable.spinner_portfolio_theme1, R.drawable.spinner_portfolio_theme2, R.drawable.spinner_portfolio_theme3};

    public static final int DASHBOARD = 0, ABOUT = 1, CONTACT = 2, PORTPOLIO = 3, OUR_WORK = 4, MAP = 5, CHAT = 6, SHARE = 7, FEEDBACK = 8, QR_CODE = 9, QUIZ = 10, SURVEY = 11, DOCUMENT = 12, VIDEO = 13;
    public String[] name;
    public int[] image = {R.drawable.about_white, R.drawable.contactus_white, R.drawable.portfolio_white, R.drawable.our_works_white, R.drawable.location, R.drawable.chat_filled, R.drawable.menu_share, R.drawable.menu_send, R.drawable.qr_code, R.drawable.quiz, R.drawable.survey, R.drawable.document, R.drawable.video_list};
    public int[] color = {R.color.firstColor, R.color.secondColor, R.color.thirdColor, R.color.fourthColor, R.color.fifthColor, R.color.sixthColor, R.color.firstColor, R.color.secondColor, R.color.thirdColor, R.color.fourthColor, R.color.fifthColor};
    int[] viewType = {DASHBOARD, ABOUT, CONTACT, PORTPOLIO, OUR_WORK, MAP, CHAT, SHARE};

    public static ArrayList<Integer> list_viewType;
    DbHelper dbHelper;
    String APP_ID, APP_NAME, APP_ICON, SPLASH_ICON, APP_CATEGORY, APP_THEME, THEME_COLOR, TEXT_COLOR, PUBLISH_ID, APP_PAGE, APP_PAGES_ID;

    public JSONObject getDASHBOARD_0AString() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < imageDashboardArray.size(); i++) {
                JSONObject jObject = new JSONObject();
                jObject.put("x", imageDashboardArray.get(i));
                jsonArray.put(jObject);
            }
            jsonObject.put("0A", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getDASHBOARD_0B_String() {
        JSONObject jsonObject = new JSONObject();

        JSONArray jsonArray1 = new JSONArray();
        try {
            for (int k = 0; k < Adapter_Dashboard_Deals_of_the_day.size(); k++) {
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("P_IMAGE", Adapter_Dashboard_Deals_of_the_day.get(k).getImagename());
                jsonObject2.put("P_NAME", Adapter_Dashboard_Deals_of_the_day.get(k).getProduct_Name());
                jsonObject2.put("P_SELL", Adapter_Dashboard_Deals_of_the_day.get(k).getProduct_Selling_Price());
                jsonArray1.put(jsonObject2);
            }
            jsonObject.put("0B", jsonArray1);
            if (edttitle0B != null)
                jsonObject.put("0B1", edttitle0B);
            else
                jsonObject.put("0B1", "No Title");
            jsonObject.put("0B2", spinnertitle0B);

            Log.e("@@@0B", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getDASHBOARD_0C_String() {
        JSONObject jsonObject = new JSONObject();

        JSONArray jsonArray1 = new JSONArray();
        try {

            for (int k = 0; k < Adapter_Dashboard_Descounts_for_you.size(); k++) {
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("P_IMAGE", Adapter_Dashboard_Descounts_for_you.get(k).getImagename());
                jsonObject2.put("P_NAME", Adapter_Dashboard_Descounts_for_you.get(k).getProduct_Name());
                jsonObject2.put("P_SELL", Adapter_Dashboard_Descounts_for_you.get(k).getProduct_Selling_Price());
                jsonArray1.put(jsonObject2);
            }
            jsonObject.put("0C", jsonArray1);
            if (edttitle0C != null)
                jsonObject.put("0C1", edttitle0C);
            else
                jsonObject.put("0C1", "No Title");
            jsonObject.put("0C2", spinnertitle0C);

            Log.e("@@@0C", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getDASHBOARD_0D_String() {
        JSONObject jsonObject = new JSONObject();

        JSONArray jsonArray1 = new JSONArray();
        try {

            for (int k = 0; k < Adapter_Dashboard_ColorfulCards.size(); k++) {
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("P_NAME", Adapter_Dashboard_ColorfulCards.get(k).getProduct_Name());
                jsonObject2.put("P_SELL", Adapter_Dashboard_ColorfulCards.get(k).getProduct_Selling_Price());
                jsonArray1.put(jsonObject2);
            }
            jsonObject.put("0D", jsonArray1);
            if (edttitle0D != null)
                jsonObject.put("0D1", edttitle0D);
            else
                jsonObject.put("0D1", "No Title");
            jsonObject.put("0D2", spinnertitle0D);

            Log.e("@@@0D", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getPORTFOLIO_3AString() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < imagePortfolioArray.size(); i++) {
                JSONObject jObject = new JSONObject();
                jObject.put("x", imagePortfolioArray.get(i));
                jsonArray.put(jObject);
            }
            jsonObject.put("3A", jsonArray);
            if (edttitle3A != null)
                jsonObject.put("3A1", edttitle3A);
            else
                jsonObject.put("3A1", "0");

            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    // ##################### Get About Us Details  ##########################
    public JSONObject getABOUT_1AString() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtAbout1A != null) {
                    jObject.put("x", edtAbout1A);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("1A", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getABOUT_1B_String() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtAbout1B != null) {
                    jObject.put("x", edtAbout1B);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("1B", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getABOUT_1C_String() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtAbout1C != null) {
                    jObject.put("x", edtAbout1C);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("1C", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getABOUT_1D_String() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtAbout1D != null) {
                    jObject.put("x", edtAbout1D);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("1D", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getABOUT_1E_String() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtAbout1E != null) {
                    jObject.put("x", edtAbout1E);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("1E", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getABOUT_1F_String() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtAbout1F != null) {
                    jObject.put("x", edtAbout1F);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("1F", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getABOUT_1G_String() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtAbout1G != null) {
                    jObject.put("x", edtAbout1G);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("1G", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getABOUT_1H_String() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtAbout1H != null) {
                    jObject.put("x", edtAbout1H);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("1H", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getABOUT_1I_String() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtAbout1I != null) {
                    jObject.put("x", edtAbout1I);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("1I", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getABOUT_1J_String() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtAbout1J != null) {
                    jObject.put("x", edtAbout1J);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("1J", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getABOUT_1K_String() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtAbout1K != null) {
                    jObject.put("x", edtAbout1K);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("1K", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getABOUT_1L_String() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtAbout1L != null) {
                    jObject.put("x", edtAbout1L);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("1L", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    //  Get Contact Details
    public JSONObject getCONTACT_2AString() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtContact2A != null) {
                    jObject.put("x", edtContact2A);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("2A", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getCONTACT_2B_String() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtContact2B != null) {
                    jObject.put("x", edtContact2B);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("2B", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getCONTACT_2C_String() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtContact2C != null) {
                    jObject.put("x", edtContact2C);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("2C", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getCONTACT_2D_String() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtContact2D != null) {
                    jObject.put("x", edtContact2D);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("2D", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getCONTACT_2E_String() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtContact2E != null) {
                    jObject.put("x", edtContact2E);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("2E", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getCONTACT_2F_String() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtContact2F != null) {
                    jObject.put("x", edtContact2F);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("2F", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getCONTACT_2G_String() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtContact2G != null) {
                    jObject.put("x", edtContact2G);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("2G", jsonArray);
            Log.e("###", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getOURWORKS_4AString() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = new JSONObject();
                if (edtOurWorks4A != null) {
                    jObject.put("x", edtOurWorks4A);
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("4A", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getMAP_5_ALL_String() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < 4; i++) {
                JSONObject jObject = new JSONObject();
                if (userLocation != null) {
                    switch (i) {
                        case 0:
                            jObject.put("x", userLocation.getName());
                            break;
                        case 1:
                            jObject.put("x", userLocation.getAddress());
                            break;
                        case 2:
                            jObject.put("x", userLocation.getLatitude());
                            break;
                        case 3:
                            jObject.put("x", userLocation.getLongitude());
                            break;
                    }
                } else {
                    jObject.put("x", "No Details added by client.");
                }
                jsonArray.put(jObject);
            }
            jsonObject.put("5A", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public JSONObject get_SHARE_7AString() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jObject = new JSONObject();
            if (edtShare7A != null) {
                jObject.put("x", edtShare7A);
            } else {
                jObject.put("x", "Create your own app, No coding needed. Let's App is a tool that create app for you. Coming soon...");
            }
            jsonArray.put(jObject);
            jsonObject.put("7A", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject get_FEEDBACK_8AString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("8A", appPrefs.getMAIL());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject get_QRCODE_9AString() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jObject = new JSONObject();
            if (edtQRCode9A != null) {
                jObject.put("x", edtQRCode9A);
            } else {
                jObject.put("x", "https://play.google.com/store/apps/details?id=bhojpuri.hitsongs");
            }
            jsonArray.put(jObject);
            jsonObject.put("9A", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject get_QUIZ_10AString() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jObject = new JSONObject();
            if (edtQuiz10A != null) {
                jObject.put("x", edtQuiz10A);
            } else {
                jObject.put("x", "NULL");
            }
            jsonArray.put(jObject);
            jsonObject.put("10A", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject get_SURVEY_11AString() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jObject = new JSONObject();
            if (edtSurvey11A != null) {
                jObject.put("x", edtSurvey11A);
            } else {
                jObject.put("x", "NULL");
            }
            jsonArray.put(jObject);
            jsonObject.put("11A", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getDOCUMENT_12A_String() {
        JSONObject jsonObject = new JSONObject();

        JSONArray jsonArray1 = new JSONArray();
        try {

            for (int k = 0; k < Adapter_Documents.size(); k++) {
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("D_NAME", Adapter_Documents.get(k).getName());
                jsonObject2.put("D_LINK", Adapter_Documents.get(k).getLink());
                jsonArray1.put(jsonObject2);
            }
            jsonObject.put("12A", jsonArray1);
            jsonObject.put("12A1", spinnertitle12A);

            Log.e("@@@12A", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject get_VIDEO_13AString() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jObject = new JSONObject();
            if (edtVideo13A != null) {
                jObject.put("x", edtVideo13A);
            } else {
                jObject.put("x", "NULL");
            }
            jsonArray.put(jObject);
            jsonObject.put("13A", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_design_one, container, false);
        name = new String[]{getString(R.string.About), getString(R.string.Contact), getString(R.string.Portfolio), getString(R.string.OurWork), getString(R.string.Map), getString(R.string.Chat), getString(R.string.Share), getString(R.string.Feedback), getString(R.string.QRCode), getString(R.string.Quiz), getString(R.string.Survey), getString(R.string.Document), getString(R.string.Video)};

        MainActivity.frameToolbar.setVisibility(View.VISIBLE);
        MainActivity.secondframeToolbar.setVisibility(View.GONE);
        rootDesignOne = (LinearLayout) v.findViewById(R.id.root_fragment_design_one);
        appPrefs = new AppPrefs(getActivity());
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        common = new Common(getContext());
        dbHelper = new DbHelper(getContext());
        MainActivity.frameToolbar.setBackgroundColor(getResources().getColor(R.color.thirdColor));
        AttributeCheckList = new ArrayList<String>();
        MainActivity.animator(getActivity(), MainActivity.imgMainNext);
        MainActivity.animator(getActivity(), MainActivity.imgMainPrev);
        MainActivity.tv_main.setText(R.string.toolbar_design1);

        list_viewType = new ArrayList<Integer>();
        imageDashboardArray = new ArrayList<String>();
        imagePortfolioArray = new ArrayList<String>();
        Adapter_Dashboard_Deals_of_the_day = new ArrayList<ProductDetailsAdapter>();
        Adapter_Dashboard_Descounts_for_you = new ArrayList<ProductDetailsAdapter>();
        Adapter_Dashboard_ColorfulCards = new ArrayList<ProductDetailsAdapter>();
        Adapter_Documents = new ArrayList<DocumentAdapter>();
        design_one_framellout = (FrameLayout) v.findViewById(design_one_frame_llout);
        recyclerViewRight = (RecyclerView) v.findViewById(R.id.recyclerview_right_main);
        recyclerViewRight.setHasFixedSize(true);

        layoutManagerright = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        // layoutManagerright = new LinearLayoutManager(getActivity());

        recyclerViewRight.setLayoutManager(layoutManagerright);

        adapterright = new CardAdapterRight(name, image, color);
        recyclerViewRight.setAdapter(adapterright);
        for (int i = 0; i < 1; i++) {
            imageDashboardArray.add("banner_auto.jpg");
            imagePortfolioArray.add("banner_auto.jpg");
            ProductDetailsAdapter productDetailsAdapter = new ProductDetailsAdapter();
            productDetailsAdapter.setImagename("banner_auto.jpg");
            // productDetailsAdapter.setProduct_MRP_Price("2999");
            productDetailsAdapter.setProduct_Selling_Price(getString(R.string.subtitle_name));
            productDetailsAdapter.setProduct_Name(getString(R.string.title_name));
            //   productDetailsAdapter.setProduct_Specification("Type:Flat\nWashable\nFlat (L*W): 235cm*225cm");
            Adapter_Dashboard_Deals_of_the_day.add(productDetailsAdapter);
            Adapter_Dashboard_Descounts_for_you.add(productDetailsAdapter);
        }
        ProductDetailsAdapter productDetailsAdapter1 = new ProductDetailsAdapter();
        productDetailsAdapter1.setProduct_Selling_Price(getString(R.string.subtitle_name));
        productDetailsAdapter1.setProduct_Name(getString(R.string.title_name));
        Adapter_Dashboard_ColorfulCards.add(productDetailsAdapter1);

        DocumentAdapter documentAdapter = new DocumentAdapter();
        documentAdapter.setName(getString(R.string.attendence_example));
        documentAdapter.setLink("https://docs.google.com/spreadsheets/d/1Bpt1BGHn4thXhanNJ3G0wQsBtQuwfBT33eug3NFE0pM/edit?usp=sharing");
        Adapter_Documents.add(documentAdapter);

        for (int i = 0; i < viewType.length; i++) {
            list_viewType.add(viewType[i]);
        }

        if (dbHelper.isNewApp(appPrefs.getAPP_ID())) {
            JSONObject jsonobject = null;
            try {
                String[] separated = dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.APP_PAGES_ID).split(":");
                jsonobject = new JSONObject(dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.APP_PAGES));
                JSONObject jsonmidObject = jsonobject.getJSONObject("APP_PAGES");
                list_viewType.clear();
                AttributeCheckList.clear();
                list_viewType.add(DASHBOARD);
                for (int i = 0; i < separated.length; i++) {
                    JSONObject jsoninObject;
                    JSONArray jarray;
                    AttributeCheckList.add(separated[i]);
                    switch (separated[i]) {
                        case "0A":
                            list_viewType.add(DASHBOARD);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            imageDashboardArray.clear();
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                imageDashboardArray.add(jb.getString("x"));
                            }
                            break;
                        case "0B":
                            list_viewType.add(DASHBOARD);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            edttitle0B = jsoninObject.getString("0B1");
                            spinnertitle0B = jsoninObject.getString("0B2");
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            Adapter_Dashboard_Deals_of_the_day.clear();
                            for (int m = 0; m < jarray.length(); m++) {
                                JSONObject jsonObject = (JSONObject) jarray.get(m);
                                ProductDetailsAdapter productDetailsAdapter = new ProductDetailsAdapter();
                                productDetailsAdapter.setImagename(jsonObject.getString("P_IMAGE"));
                                productDetailsAdapter.setProduct_Name(jsonObject.getString("P_NAME"));
                                productDetailsAdapter.setProduct_Selling_Price(jsonObject.getString("P_SELL"));
                                Adapter_Dashboard_Deals_of_the_day.add(productDetailsAdapter);
                            }
                            break;
                        case "0C":
                            list_viewType.add(DASHBOARD);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            edttitle0C = jsoninObject.getString("0C1");
                            spinnertitle0C = jsoninObject.getString("0C2");
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            Adapter_Dashboard_Descounts_for_you.clear();
                            for (int m = 0; m < jarray.length(); m++) {
                                JSONObject jsonObject = (JSONObject) jarray.get(m);
                                ProductDetailsAdapter productDetailsAdapter = new ProductDetailsAdapter();
                                productDetailsAdapter.setImagename(jsonObject.getString("P_IMAGE"));
                                productDetailsAdapter.setProduct_Name(jsonObject.getString("P_NAME"));
                                productDetailsAdapter.setProduct_Selling_Price(jsonObject.getString("P_SELL"));
                                Adapter_Dashboard_Descounts_for_you.add(productDetailsAdapter);
                            }
                            break;
                        case "0D":
                            list_viewType.add(DASHBOARD);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            edttitle0D = jsoninObject.getString("0D1");
                            spinnertitle0D = jsoninObject.getString("0D2");
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            Adapter_Dashboard_ColorfulCards.clear();
                            for (int m = 0; m < jarray.length(); m++) {
                                JSONObject jsonObject = (JSONObject) jarray.get(m);
                                ProductDetailsAdapter productDetailsAdapter = new ProductDetailsAdapter();
                                productDetailsAdapter.setProduct_Name(jsonObject.getString("P_NAME"));
                                productDetailsAdapter.setProduct_Selling_Price(jsonObject.getString("P_SELL"));
                                Adapter_Dashboard_ColorfulCards.add(productDetailsAdapter);
                            }
                            break;
                        case "1A":
                            list_viewType.add(ABOUT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtAbout1A = jb.getString("x");
                            }
                            break;
                        case "1B":
                            list_viewType.add(ABOUT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtAbout1B = jb.getString("x");
                            }
                            break;
                        case "1C":
                            list_viewType.add(ABOUT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtAbout1C = jb.getString("x");
                            }
                            break;
                        case "1D":
                            list_viewType.add(ABOUT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtAbout1D = jb.getString("x");
                            }
                            break;
                        case "1E":
                            list_viewType.add(ABOUT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtAbout1E = jb.getString("x");
                            }
                            break;
                        case "1F":
                            list_viewType.add(ABOUT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtAbout1F = jb.getString("x");
                            }
                            break;
                        case "1G":
                            list_viewType.add(ABOUT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtAbout1G = jb.getString("x");
                            }
                            break;
                        case "1H":
                            list_viewType.add(ABOUT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtAbout1H = jb.getString("x");
                            }
                            break;
                        case "1I":
                            list_viewType.add(ABOUT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtAbout1I = jb.getString("x");
                            }
                            break;
                        case "1J":
                            list_viewType.add(ABOUT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtAbout1J = jb.getString("x");
                            }
                            break;
                        case "1K":
                            list_viewType.add(ABOUT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtAbout1K = jb.getString("x");
                            }
                            break;
                        case "1L":
                            list_viewType.add(ABOUT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtAbout1L = jb.getString("x");
                            }
                            break;

                        case "2A":
                            list_viewType.add(CONTACT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtContact2A = jb.getString("x");
                            }
                            break;
                        case "2B":
                            list_viewType.add(CONTACT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtContact2B = jb.getString("x");
                            }
                            break;
                        case "2C":
                            list_viewType.add(CONTACT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtContact2C = jb.getString("x");
                            }
                            break;
                        case "2D":
                            list_viewType.add(CONTACT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtContact2D = jb.getString("x");
                            }
                            break;
                        case "2E":
                            list_viewType.add(CONTACT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtContact2E = jb.getString("x");
                            }
                            break;
                        case "2F":
                            list_viewType.add(CONTACT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtContact2F = jb.getString("x");
                            }
                            break;
                        case "2G":
                            list_viewType.add(CONTACT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtContact2G = jb.getString("x");
                            }
                            break;
                        case "3A":
                            list_viewType.add(PORTPOLIO);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            edttitle3A = jsoninObject.getString("3A1");
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            imagePortfolioArray.clear();
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                imagePortfolioArray.add(jb.getString("x"));
                            }
                            break;
                        case "4A":
                            list_viewType.add(OUR_WORK);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtOurWorks4A = jb.getString("x");
                            }
                            break;
                        case "5A":
                            list_viewType.add(MAP);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            userLocation = new UserLocation();
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                switch (k) {
                                    case 0:
                                        userLocation.setName(jb.getString("x"));
                                        break;
                                    case 1:
                                        userLocation.setAddress(jb.getString("x"));
                                        break;
                                    case 2:
                                        try {
                                            userLocation.setLatitude(Double.parseDouble(jb.getString("x")));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case 3:
                                        try {
                                            userLocation.setLongitude(Double.parseDouble(jb.getString("x")));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                }
                            }
                            break;
                        case "6A":
                            list_viewType.add(CHAT);
                            IsChatFeatureON = true;
                            break;
                        case "7A":
                            list_viewType.add(SHARE);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtShare7A = jb.getString("x");
                            }
                            break;
                        case "8A":
                            list_viewType.add(FEEDBACK);
                            IsFeedbackFeatureON = true;
                            break;
                        case "9A":
                            list_viewType.add(QR_CODE);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtQRCode9A = jb.getString("x");
                            }
                            break;
                        case "10A":
                            list_viewType.add(QUIZ);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtQuiz10A = jb.getString("x");
                            }
                            break;
                        case "11A":
                            list_viewType.add(SURVEY);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtSurvey11A = jb.getString("x");
                            }
                            break;
                        case "12A":
                            list_viewType.add(DOCUMENT);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            spinnertitle12A = jsoninObject.getString("12A1");
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            Adapter_Documents.clear();
                            for (int m = 0; m < jarray.length(); m++) {
                                JSONObject jsonObject = (JSONObject) jarray.get(m);
                                DocumentAdapter documentAdapter1 = new DocumentAdapter();
                                documentAdapter1.setName(jsonObject.getString("D_NAME"));
                                documentAdapter1.setLink(jsonObject.getString("D_LINK"));
                                Adapter_Documents.add(documentAdapter1);
                            }
                            break;
                        case "13A":
                            list_viewType.add(VIDEO);
                            jsoninObject = jsonmidObject.getJSONObject(separated[i]);
                            jarray = jsoninObject.getJSONArray(separated[i]);
                            for (int k = 0; k < jarray.length(); k++) {
                                JSONObject jb = (JSONObject) jarray.get(k);
                                edtVideo13A = jb.getString("x");
                            }
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Set<Integer> without_duplicate_viewtype = new LinkedHashSet<Integer>(list_viewType);
        list_viewType.clear();
        list_viewType.addAll(without_duplicate_viewtype);
        Collections.sort(list_viewType);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CardAdapter(list_viewType);
        recyclerView.setAdapter(adapter);

        design_one_framellout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerView.getVisibility() == View.GONE) {
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerViewRight.setVisibility(View.GONE);
                }
            }
        });

        MainActivity.imgMainNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                splitString = new StringBuilder();
                if (AttributeCheckList.size() > 0) {
                    try {
                        JSONObject jsonMainObject = new JSONObject();
                        JSONObject jsoninsideObject = new JSONObject();
                        Set<String> without_duplicate = new LinkedHashSet<String>(AttributeCheckList);
                        AttributeCheckList.clear();
                        AttributeCheckList.addAll(without_duplicate);
                        for (int i = 0; i < AttributeCheckList.size(); i++) {
                            splitString.append(":" + AttributeCheckList.get(i));
                            switch (AttributeCheckList.get(i)) {
                                case "0A":
                                    jsoninsideObject.put("0A", getDASHBOARD_0AString());
                                    break;
                                case "0B":
                                    jsoninsideObject.put("0B", getDASHBOARD_0B_String());
                                    break;
                                case "0C":
                                    jsoninsideObject.put("0C", getDASHBOARD_0C_String());
                                    break;
                                case "0D":
                                    jsoninsideObject.put("0D", getDASHBOARD_0D_String());
                                    break;
                                case "1A":
                                    jsoninsideObject.put("1A", getABOUT_1AString());
                                    break;
                                case "1B":
                                    jsoninsideObject.put("1B", getABOUT_1B_String());
                                    break;
                                case "1C":
                                    jsoninsideObject.put("1C", getABOUT_1C_String());
                                    break;
                                case "1D":
                                    jsoninsideObject.put("1D", getABOUT_1D_String());
                                    break;
                                case "1E":
                                    jsoninsideObject.put("1E", getABOUT_1E_String());
                                    break;
                                case "1F":
                                    jsoninsideObject.put("1F", getABOUT_1F_String());
                                    break;
                                case "1G":
                                    jsoninsideObject.put("1G", getABOUT_1G_String());
                                    break;
                                case "1H":
                                    jsoninsideObject.put("1H", getABOUT_1H_String());
                                    break;
                                case "1I":
                                    jsoninsideObject.put("1I", getABOUT_1I_String());
                                    break;
                                case "1J":
                                    jsoninsideObject.put("1J", getABOUT_1J_String());
                                    break;
                                case "1K":
                                    jsoninsideObject.put("1K", getABOUT_1K_String());
                                    break;
                                case "1L":
                                    jsoninsideObject.put("1L", getABOUT_1L_String());
                                    break;
                                case "2A":
                                    jsoninsideObject.put("2A", getCONTACT_2AString());
                                    break;
                                case "2B":
                                    jsoninsideObject.put("2B", getCONTACT_2B_String());
                                    break;
                                case "2C":
                                    jsoninsideObject.put("2C", getCONTACT_2C_String());
                                    break;
                                case "2D":
                                    jsoninsideObject.put("2D", getCONTACT_2D_String());
                                    break;
                                case "2E":
                                    jsoninsideObject.put("2E", getCONTACT_2E_String());
                                    break;
                                case "2F":
                                    jsoninsideObject.put("2F", getCONTACT_2F_String());
                                    break;
                                case "2G":
                                    jsoninsideObject.put("2G", getCONTACT_2G_String());
                                    break;
                                case "3A":
                                    jsoninsideObject.put("3A", getPORTFOLIO_3AString());
                                    break;
                                case "4A":
                                    jsoninsideObject.put("4A", getOURWORKS_4AString());
                                    break;
                                case "5A":
                                    jsoninsideObject.put("5A", getMAP_5_ALL_String());
                                    break;
                                case "7A":
                                    jsoninsideObject.put("7A", get_SHARE_7AString());
                                    break;
                                case "8A":
                                    jsoninsideObject.put("8A", get_FEEDBACK_8AString());
                                    break;
                                case "9A":
                                    jsoninsideObject.put("9A", get_QRCODE_9AString());
                                    break;
                                case "10A":
                                    jsoninsideObject.put("10A", get_QUIZ_10AString());
                                    break;
                                case "11A":
                                    jsoninsideObject.put("11A", get_SURVEY_11AString());
                                    break;
                                case "12A":
                                    jsoninsideObject.put("12A", getDOCUMENT_12A_String());
                                    break;
                                case "13A":
                                    jsoninsideObject.put("13A", get_VIDEO_13AString());
                                    break;
                            }
                        }
                        jsonMainObject.put("APP_PAGES", jsoninsideObject);

                        dbHelper.UpdateAttributeDesignOnedata(appPrefs.getAPP_ID(), DbHelper.APP_PAGES, jsonMainObject.toString());
                        dbHelper.UpdateAttributeDesignOnedata(appPrefs.getAPP_ID(), DbHelper.APP_PAGES_ID, splitString.substring(1, splitString.length()));

                        if (appPrefs.getIS_NEW_APP().equals("false")) {
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            try {
                                String query = "SELECT * FROM " + DbHelper.TABLE_NAME + " WHERE " + DbHelper.APP_ID + " = '" + appPrefs.getAPP_ID() + "'";
                                Cursor resultset = db.rawQuery(query, null);

                                if (resultset != null) {
                                    while (resultset.moveToNext()) {
                                        APP_ID = resultset.getString(resultset.getColumnIndex(DbHelper.APP_ID));
                                        APP_NAME = resultset.getString(resultset.getColumnIndex(DbHelper.APP_NAME));
                                        APP_ICON = resultset.getString(resultset.getColumnIndex(DbHelper.APP_ICON));
                                        SPLASH_ICON = resultset.getString(resultset.getColumnIndex(DbHelper.SPLASH_ICON));
                                        APP_CATEGORY = resultset.getString(resultset.getColumnIndex(DbHelper.APP_CATEGORY));
                                        APP_THEME = resultset.getString(resultset.getColumnIndex(DbHelper.APP_THEME));
                                        THEME_COLOR = resultset.getString(resultset.getColumnIndex(DbHelper.THEME_COLOR));
                                        TEXT_COLOR = resultset.getString(resultset.getColumnIndex(DbHelper.TEXT_COLOR));
                                        PUBLISH_ID = resultset.getString(resultset.getColumnIndex(DbHelper.PUBLISH_ID));
                                        APP_PAGE = resultset.getString(resultset.getColumnIndex(DbHelper.APP_PAGES));
                                        APP_PAGES_ID = resultset.getString(resultset.getColumnIndex(DbHelper.APP_PAGES_ID));
                                        resultset.close();
                                    }
                                }

                            } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                            if (common.isConnected()) {
                                common.showProgressDialog(getString(R.string.progress_updating));
                                CallEachPhaseAppDetailsApi(appPrefs.getUserId(), APP_ID, APP_NAME, APP_ICON, SPLASH_ICON, APP_CATEGORY, APP_THEME, THEME_COLOR, TEXT_COLOR, PUBLISH_ID, APP_PAGE, APP_PAGES_ID);
                            } else {
                                Snackbar snackbar = Snackbar.make(rootDesignOne, R.string.message_turn_on_internet, Snackbar.LENGTH_SHORT);
                                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                                snackbar.show();
                            }
                        } else {
                            Fragment fragment = new fragment_design_two();
                            if (fragment != null) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                                    // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                                    // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.thirdColor));
                                    // }
                                }

                                MainActivity.frameToolbar.setBackgroundColor(getResources().getColor(R.color.thirdColor));
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();
                                Snackbar snackbar = Snackbar.make(rootDesignOne, R.string.message_data_save_success, Snackbar.LENGTH_SHORT);
                                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                                snackbar.show();
                            } else {
                              //  Log.e("Home", "Error in creating fragment");
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Snackbar snackbar = Snackbar.make(rootDesignOne, R.string.message_minimum_requirement, Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                    snackbar.show();
                }

            }
        });

        MainActivity.imgMainPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  MainActivity.bottomNavigationView.selectTab(1);
                Fragment fragment = new fragment_selection_two();
                if (fragment != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.secondColor));
                    }

                    MainActivity.frameToolbar.setBackgroundColor(getResources().getColor(R.color.secondColor));

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();
                } else {
                  //  Log.e("Home", "Error in creating fragment");
                }

            }
        });

        return v;
    }

    private void sendLocation() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            startActivityForResult(builder.build(getActivity()), REQUEST_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
          //  Log.e("Google Play", e.toString(), e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_PLACE_PICKER) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                userLocation = new UserLocation(place);
            } else if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void selectImage(final CardPortfolioAdapter cardPortfolioAdapter) {
        final CharSequence[] items = {getString(R.string.camera_dialog_take_photo), getString(R.string.camera_dialog_choose_library),
                getString(R.string.camera_dialog_cancel)};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.camera_dialog_title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals(getString(R.string.camera_dialog_take_photo))) {
                    // userChoosenTask = "Take Photo";
                    if (result)
                        MaincardPortfolioAdapter = cardPortfolioAdapter;
                    MaincardPortfolioAdapter = cardPortfolioAdapter;
                    cameraIntent();
                } else if (items[item].equals(getString(R.string.camera_dialog_choose_library))) {
                    //  userChoosenTask = "Choose from Library";
                    if (result)
                        MaincardPortfolioAdapter = cardPortfolioAdapter;
                    MaincardPortfolioAdapter = cardPortfolioAdapter;
                    galleryIntent();

                } else if (items[item].equals(getString(R.string.camera_dialog_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectDashboardImage(final CardDashBoardAdapter cardDashBoardAdapter) {
        final CharSequence[] items = {getString(R.string.camera_dialog_take_photo), getString(R.string.camera_dialog_choose_library),
                getString(R.string.camera_dialog_cancel)};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.camera_dialog_title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals(getString(R.string.camera_dialog_take_photo))) {
                    if (result)
                        MaincardDashboardAdapter = cardDashBoardAdapter;
                    MaincardDashboardAdapter = cardDashBoardAdapter;
                    cameraIntent();


                } else if (items[item].equals(getString(R.string.camera_dialog_choose_library))) {
                    if (result)
                        MaincardDashboardAdapter = cardDashBoardAdapter;
                    MaincardDashboardAdapter = cardDashBoardAdapter;
                    galleryIntent();

                } else if (items[item].equals(getString(R.string.camera_dialog_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectDashboard_Dealoftheday_Image(final CardDashBoard_Deals_of_Adapter cardDashBoard_deals_of_adapter) {
        final CharSequence[] items = {getString(R.string.camera_dialog_take_photo), getString(R.string.camera_dialog_choose_library),
                getString(R.string.camera_dialog_cancel)};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.camera_dialog_title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals(getString(R.string.camera_dialog_take_photo))) {
                    if (result)
                        MaincardDashboard_Dealofday_Adapter = cardDashBoard_deals_of_adapter;
                    MaincardDashboard_Dealofday_Adapter = cardDashBoard_deals_of_adapter;
                    cameraIntent();


                } else if (items[item].equals(getString(R.string.camera_dialog_choose_library))) {
                    if (result)
                        MaincardDashboard_Dealofday_Adapter = cardDashBoard_deals_of_adapter;
                    MaincardDashboard_Dealofday_Adapter = cardDashBoard_deals_of_adapter;
                    galleryIntent();

                } else if (items[item].equals(getString(R.string.camera_dialog_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectDashboard_Discountforyou_Image(final CardDashBoard_Discount_for_you_Adapter cardDashBoard_deals_of_adapter) {
        final CharSequence[] items = {getString(R.string.camera_dialog_take_photo), getString(R.string.camera_dialog_choose_library),
                getString(R.string.camera_dialog_cancel)};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.camera_dialog_title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());
                if (items[item].equals(getString(R.string.camera_dialog_take_photo))) {
                    if (result)
                        MaincardDashboard_Discountforyou_Adapter = cardDashBoard_deals_of_adapter;
                    MaincardDashboard_Discountforyou_Adapter = cardDashBoard_deals_of_adapter;
                    cameraIntent();
                } else if (items[item].equals(getString(R.string.camera_dialog_choose_library))) {
                    if (result)
                        MaincardDashboard_Discountforyou_Adapter = cardDashBoard_deals_of_adapter;
                    MaincardDashboard_Discountforyou_Adapter = cardDashBoard_deals_of_adapter;
                    galleryIntent();
                } else if (items[item].equals(getString(R.string.camera_dialog_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, getString(R.string.gallery_select_file)), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 9) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        UniversalBitmap = thumbnail;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if (thumbnail != null) {
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        }
        uploadImage();
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadImage() {
        final CharSequence[] items = {getString(R.string.upload_dialog_photo), getString(R.string.upload_dialog_cancel)};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this.getContext());
        builder.setTitle(R.string.upload_dialog_title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity().getApplicationContext());

                if (items[item].equals(getString(R.string.upload_dialog_photo))) {
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("uploaded_file", getStringImage(UniversalBitmap));
                    params.put("uid", appPrefs.getUserId());
                    params.put("refer_id", appPrefs.getREFERID());
                    if (WhoseAdapter.equals("DASHBOARD")) {
                        params.put("image_type", "DASHBOARD");
                    } else if (WhoseAdapter.equals("PORTFOLIO")) {
                        params.put("image_type", "PORTFOLIO");
                    } else if (WhoseAdapter.equals("DASHBOARD_DEALSOFTHEDAY")) {
                        params.put("image_type", "DASHBOARD_DEALSOFTHEDAY");
                    } else if (WhoseAdapter.equals("DASHBOARD_DISCOUNTFORYOU")) {
                        params.put("image_type", "DASHBOARD_DISCOUNTFORYOU");
                    }
                    final String saltString = getSaltString();
                    params.put("random_id", saltString);

                  //  Log.e("!!!", params.toString());
                    client.post("http://fadootutorial.com/appgenerator/imgupload.php", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            common.showProgressDialog(getString(R.string.progress_uploading));
                          //  Log.e("$$$", "start");
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            common.hideProgressDialog();
                            String str = null;
                            try {
                                str = new String(responseBody, "UTF-8");
                                // Log.e("***********", "response is" + str);
                                SignUpResponse response = new Gson().fromJson(str, SignUpResponse.class);
                               // Log.e("****SignUp*****", "" + response.result);

                                if (response.result.equals("success")) {
                                    if (WhoseAdapter.equals("PORTFOLIO")) {
                                        //  bitmapArray.add(savePosition, UniversalBitmap);
                                        if (isOnImagePressedCheck) {
                                            imagePortfolioArray.set(savePosition, saltString + ".png");
                                            MaincardPortfolioAdapter.notifyItemChanged(savePosition);
                                        } else {
                                            imagePortfolioArray.add(savePosition, saltString + ".png");
                                            MaincardPortfolioAdapter.notifyItemInserted(savePosition);
                                            MaincardPortfolioAdapter.notifyDataSetChanged();
                                        }
                                    } else if (WhoseAdapter.equals("DASHBOARD")) {
                                        //  bitmapDashboardArray.add(savePosition, UniversalBitmap);
                                        if (isOnImagePressedCheck) {
                                            imageDashboardArray.set(savePosition, saltString + ".png");
                                         //   Log.e("###", "Pressed check");
                                            MaincardDashboardAdapter.notifyItemChanged(savePosition);
                                        } else {
                                            imageDashboardArray.add(savePosition, saltString + ".png");
                                           // Log.e("###", "Not Pressed");
                                            MaincardDashboardAdapter.notifyItemInserted(savePosition);
                                            MaincardDashboardAdapter.notifyDataSetChanged();
                                        }

                                    } else if (WhoseAdapter.equals("DASHBOARD_DEALSOFTHEDAY")) {
                                        ProductDetailsAdapter productDetailsAdapter = new ProductDetailsAdapter();
                                        if (isOnImagePressedCheck) {
                                            Adapter_Dashboard_Deals_of_the_day.get(savePosition).setImagename(saltString + ".png");
                                            MaincardDashboard_Dealofday_Adapter.notifyItemChanged(savePosition);
                                        } else {
                                            productDetailsAdapter.setImagename(saltString + ".png");
                                            Adapter_Dashboard_Deals_of_the_day.add(savePosition, productDetailsAdapter);
                                            MaincardDashboard_Dealofday_Adapter.notifyItemInserted(savePosition);
                                            MaincardDashboard_Dealofday_Adapter.notifyDataSetChanged();
                                        }
                                    } else if (WhoseAdapter.equals("DASHBOARD_DISCOUNTFORYOU")) {
                                        ProductDetailsAdapter productDetailsAdapter = new ProductDetailsAdapter();
                                        if (isOnImagePressedCheck) {
                                            Adapter_Dashboard_Descounts_for_you.get(savePosition).setImagename(saltString + ".png");
                                            MaincardDashboard_Discountforyou_Adapter.notifyItemChanged(savePosition);
                                        } else {
                                            productDetailsAdapter.setImagename(saltString + ".png");
                                            Adapter_Dashboard_Descounts_for_you.add(savePosition, productDetailsAdapter);
                                            MaincardDashboard_Discountforyou_Adapter.notifyItemInserted(savePosition);
                                            MaincardDashboard_Discountforyou_Adapter.notifyDataSetChanged();
                                        }
                                    }
                                    Toast.makeText(getActivity(), R.string.message_image_upload_success, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), R.string.message_image_upload_error, Toast.LENGTH_SHORT).show();
                                }

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            common.hideProgressDialog();
                            Toast.makeText(getActivity(), R.string.message_please_try_later, Toast.LENGTH_LONG).show();

                        }
                    });

                } else if (items[item].equals(getString(R.string.upload_dialog_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                UniversalBitmap = bm;
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    class CardAdapterRight extends RecyclerView.Adapter<CardAdapterRight.ViewHolder> {

        List<ListItemSelectionOne> items;

        public CardAdapterRight(String[] names, int[] image, int[] color) {
            super();
            items = new ArrayList<ListItemSelectionOne>();
            for (int i = 0; i < names.length; i++) {
                ListItemSelectionOne item = new ListItemSelectionOne();
                item.setName(names[i]);
                item.setImagePath(image[i]);
                item.setColor(color[i % 6]);
                items.add(item);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_card_main_right_popup, parent, false);
           // Log.e("$$$", "" + viewType);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final ListItemSelectionOne list = items.get(position);

            // holder.textViewName.setTag(image[position]);
            holder.textViewName.setText(list.getName());
            // holder.cardView.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_up));
            holder.imgList.setImageResource(image[position]);
            //  holder.imgList.startAnimation(AnimationUtils.loadAnimation(, R.anim.slide_up));
            holder.relativeLayout.setBackgroundResource(color[position % 6]);
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appPrefs.setAdditionalposition(position + "");
                    showLocationDialog();
                }
            });
            holder.imgRightAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    appPrefs.setAdditionalposition(position + "");
                    showLocationDialog();
                }
            });

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textViewName;
            ImageView imgList;
            RelativeLayout relativeLayout;
            ImageView imgRightAdd;

            public ViewHolder(View itemView) {
                super(itemView);
                imgList = (ImageView) itemView.findViewById(R.id.img_list_card);
                textViewName = (TextView) itemView.findViewById(R.id.textViewName);
                relativeLayout = (RelativeLayout) itemView.findViewById(R.id.selection_rrout);
                imgRightAdd = (ImageView) itemView.findViewById(R.id.img_add_right_popup);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void showLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.category_dialog_title);
        String s = name[Integer.parseInt(appPrefs.getAdditionalposition())];
        builder.setMessage(s + getString(R.string.category_dialog_message));

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        Boolean flag = false;
                        for (int i = 0; i < list_viewType.size(); i++) {
                            if (list_viewType.get(i) == (Integer.parseInt(appPrefs.getAdditionalposition()) + 1)) {
                                flag = true;
                            }
                        }
                        if (!flag) {
                            list_viewType.add(savePosition, Integer.parseInt(appPrefs.getAdditionalposition()) + 1);
                            adapter.notifyItemInserted(savePosition);
                            recyclerView.scrollToPosition(savePosition);
                            recyclerViewRight.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        } else {
                            Snackbar snackbar = Snackbar.make(rootDesignOne, R.string.message_page_already_available, Snackbar.LENGTH_SHORT);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                            snackbar.show();

                        }
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }


    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

        public CardAdapter(ArrayList<Integer> viewTypeCard) {
            super();
            list_viewType = viewTypeCard;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            if (viewType == DASHBOARD) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_card_design_dashboard, parent, false);
                return new ViewDashboardHolder(v);
            }
            if (viewType == DOCUMENT) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_card_design_documents, parent, false);
                return new ViewDocumentHolder(v);
            } else if (viewType == MAP) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_card_design_map, parent, false);
                return new ViewMapHolder(v);
            } else if (viewType == CHAT) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_card_design_chat, parent, false);
                return new ViewChatHolder(v);
            } else if (viewType == FEEDBACK) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_card_design_feedback, parent, false);
                return new ViewFeedbackHolder(v);
            } else if (viewType == CONTACT) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_card_design_contactus, parent, false);
                return new ViewContactUsHolder(v);
            } else if (viewType == OUR_WORK) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_card_design_ourworks, parent, false);
                return new ViewOurWorksHolder(v);
            } else if (viewType == SHARE) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_card_design_share, parent, false);
                return new ViewShareHolder(v);
            } else if (viewType == QUIZ) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_card_design_share, parent, false);
                return new ViewShareHolder(v);
            } else if (viewType == VIDEO) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_card_design_share, parent, false);
                return new ViewShareHolder(v);
            } else if (viewType == SURVEY) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_card_design_share, parent, false);
                return new ViewShareHolder(v);
            } else if (viewType == QR_CODE) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_card_design_qrcode, parent, false);
                return new ViewQRCodeHolder(v);
            } else if (viewType == FEEDBACK) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_card_design_feedback, parent, false);
                return new ViewShareHolder(v);
            } else if (viewType == PORTPOLIO) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_card_design_portfolio, parent, false);
                return new ViewPortfolioHolder(v);
            } else /*if (viewType == ABOUT)*/ {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_card_design_about, parent, false);
                return new ViewAboutHolder(v);
            }

        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
          /*  Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.color_item);
            animation.setInterpolator(new AccelerateInterpolator());
            viewHolder.itemView.setAnimation(animation);
          */ // viewHolder.itemView.setVisibility(View.VISIBLE);
            // animation.start();

            if (viewHolder.getItemViewType() == ABOUT) {
                final ViewAboutHolder holder = (ViewAboutHolder) viewHolder;
                holder.about_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.about_llout_visibility.getVisibility() == View.VISIBLE) {
                            holder.about_llout_visibility.setVisibility(View.GONE);
                        } else if (holder.about_llout_visibility.getVisibility() == View.GONE) {
                            holder.about_llout_visibility.setVisibility(View.VISIBLE);
                        }
                    }
                });

                holder.frameBackgroung.setBackgroundColor(ContextCompat.getColor(getActivity(), color[position % 6]));
                // holder.textViewName.setText("About");
                // ##################### FOR 1A ################## //
                if (edtAbout1A != null) {
                    holder.switch_1A.setChecked(true);
                    holder.switch_1A.setHint("");
                    holder.edt1A.setText(edtAbout1A);
                    holder.edt1A.setVisibility(View.VISIBLE);
                }
                holder.switch_1A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_1A.setHint("");
                            holder.edt1A.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("1A");
                        } else {
                            holder.switch_1A.setHint(R.string.switch1a_hint);
                            holder.edt1A.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("1A"));
                        }
                    }
                });

                holder.edt1A.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtAbout1A = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtAbout1A = editable.toString();
                    }
                });

                // ##################### FOR 1B ################## //
                if (edtAbout1B != null) {
                    holder.switch_1B.setChecked(true);
                    holder.switch_1B.setHint("");
                    holder.edt1B.setText(edtAbout1B);
                    holder.edt1B.setVisibility(View.VISIBLE);
                }

                holder.switch_1B.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_1B.setHint("");
                            holder.edt1B.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("1B");
                        } else {
                            holder.switch_1B.setHint(R.string.switch1b_hint);
                            holder.edt1B.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("1B"));
                        }
                    }
                });

                holder.edt1B.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtAbout1B = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtAbout1B = editable.toString();
                    }
                });

                // ##################### FOR 1C ################## //
                if (edtAbout1C != null) {
                    holder.switch_1C.setChecked(true);
                    holder.switch_1C.setHint("");
                    holder.edt1C.setText(edtAbout1C);
                    holder.edt1C.setVisibility(View.VISIBLE);
                }

                holder.switch_1C.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_1C.setHint("");
                            holder.edt1C.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("1C");
                        } else {
                            holder.switch_1C.setHint(R.string.switch1c_hint);
                            holder.edt1C.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("1C"));
                        }
                    }
                });

                holder.edt1C.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtAbout1C = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtAbout1C = editable.toString();
                    }
                });

                // ##################### FOR 1D ################## //
                if (edtAbout1D != null) {
                    holder.switch_1D.setChecked(true);
                    holder.switch_1D.setHint("");
                    holder.edt1D.setText(edtAbout1D);
                    holder.edt1D.setVisibility(View.VISIBLE);
                }
                holder.switch_1D.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_1D.setHint("");
                            holder.edt1D.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("1D");
                        } else {
                            holder.edt1D.setVisibility(View.GONE);
                            holder.switch_1D.setHint(R.string.switch1d_hint);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("1D"));
                        }
                    }
                });

                holder.edt1D.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtAbout1D = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtAbout1D = editable.toString();
                    }
                });
                // ##################### FOR 1E ################## //
                if (edtAbout1E != null) {
                    holder.switch_1E.setChecked(true);
                    holder.switch_1E.setHint("");
                    holder.edt1E.setText(edtAbout1E);
                    holder.edt1E.setVisibility(View.VISIBLE);
                }

                holder.switch_1E.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_1E.setHint("");
                            holder.edt1E.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("1E");
                        } else {
                            holder.switch_1E.setHint(R.string.switch1e_hint);
                            holder.edt1E.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("1E"));
                        }
                    }
                });

                holder.edt1E.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtAbout1E = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtAbout1E = editable.toString();
                    }
                });
                // ##################### FOR 1F ################## //
                if (edtAbout1F != null) {
                    holder.switch_1F.setChecked(true);
                    holder.switch_1F.setHint("");
                    holder.edt1F.setText(edtAbout1F);
                    holder.edt1F.setVisibility(View.VISIBLE);
                }

                holder.switch_1F.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_1F.setHint("");
                            holder.edt1F.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("1F");
                        } else {
                            holder.switch_1F.setHint(R.string.switch1f_hint);
                            holder.edt1F.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("1F"));
                        }
                    }
                });

                holder.edt1F.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtAbout1F = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtAbout1F = editable.toString();
                    }
                });
                // ##################### FOR 1G ################## //
                if (edtAbout1G != null) {
                    holder.switch_1G.setChecked(true);
                    holder.switch_1G.setHint("");
                    holder.edt1G.setText(edtAbout1G);
                    holder.edt1G.setVisibility(View.VISIBLE);
                }

                holder.switch_1G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_1G.setHint("");
                            holder.edt1G.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("1G");
                        } else {
                            holder.edt1G.setVisibility(View.GONE);
                            holder.switch_1G.setHint(R.string.switch1g_hint);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("1G"));
                        }
                    }
                });

                holder.edt1G.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtAbout1G = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtAbout1G = editable.toString();
                    }
                });
                // ##################### FOR 1H ################## //
                if (edtAbout1H != null) {
                    holder.switch_1H.setChecked(true);
                    holder.switch_1H.setHint("");
                    holder.edt1H.setText(edtAbout1H);
                    holder.edt1H.setVisibility(View.VISIBLE);
                }

                holder.switch_1H.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_1H.setHint("");
                            holder.edt1H.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("1H");
                        } else {
                            holder.switch_1H.setHint(R.string.switch1h_hint);
                            holder.edt1H.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("1H"));
                        }
                    }
                });

                holder.edt1H.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtAbout1H = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtAbout1H = editable.toString();
                    }
                });
                // ##################### FOR 1I ################## //
                if (edtAbout1I != null) {
                    holder.switch_1I.setChecked(true);
                    holder.switch_1I.setHint("");
                    holder.edt1I.setText(edtAbout1I);
                    holder.edt1I.setVisibility(View.VISIBLE);
                }

                holder.switch_1I.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_1I.setHint("");
                            holder.edt1I.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("1I");
                        } else {
                            holder.switch_1I.setHint(R.string.switch1i_hint);
                            holder.edt1I.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("1I"));
                        }
                    }
                });

                holder.edt1I.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtAbout1I = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtAbout1I = editable.toString();
                    }
                });
                // ##################### FOR 1J ################## //
                if (edtAbout1J != null) {
                    holder.switch_1J.setChecked(true);
                    holder.switch_1J.setHint("");
                    holder.edt1J.setText(edtAbout1J);
                    holder.edt1J.setVisibility(View.VISIBLE);
                }

                holder.switch_1J.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_1J.setHint("");
                            holder.edt1J.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("1J");
                        } else {
                            holder.switch_1J.setHint(R.string.switch1j_hint);
                            holder.edt1J.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("1J"));
                        }
                    }
                });

                holder.edt1J.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtAbout1J = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtAbout1J = editable.toString();
                    }
                });
                // ##################### FOR 1K ################## //
                if (edtAbout1K != null) {
                    holder.switch_1K.setChecked(true);
                    holder.switch_1K.setHint("");
                    holder.edt1K.setText(edtAbout1K);
                    holder.edt1K.setVisibility(View.VISIBLE);
                }

                holder.switch_1K.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.edt1K.setVisibility(View.VISIBLE);
                            holder.switch_1K.setHint("");
                            AttributeCheckList.add("1K");
                        } else {
                            holder.switch_1K.setHint(R.string.switch1k_hint);
                            holder.edt1K.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("1K"));
                        }
                    }
                });

                holder.edt1K.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtAbout1K = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtAbout1K = editable.toString();
                    }
                });
                // ##################### FOR 1L ################## //
                if (edtAbout1L != null) {
                    holder.switch_1L.setChecked(true);
                    holder.switch_1L.setHint("");
                    holder.edt1L.setText(edtAbout1L);
                    holder.edt1L.setVisibility(View.VISIBLE);
                }

                holder.switch_1L.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_1L.setHint("");
                            holder.edt1L.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("1L");
                        } else {
                            holder.switch_1L.setHint(R.string.switch1l_hint);
                            holder.edt1L.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("1L"));
                        }
                    }
                });

                holder.edt1L.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtAbout1L = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtAbout1L = editable.toString();
                    }
                });


                holder.imgadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerViewRight.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        savePosition = position;
                    }
                });

                holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list_viewType.remove(list_viewType.indexOf(ABOUT));
                        notifyItemRemoved(list_viewType.indexOf(ABOUT));
                    }
                });

            } else if (viewHolder.getItemViewType() == CONTACT) {
                final ViewContactUsHolder holder = (ViewContactUsHolder) viewHolder;
                holder.contact_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.contact_llout_visibility.getVisibility() == View.VISIBLE) {
                            holder.contact_llout_visibility.setVisibility(View.GONE);
                        } else if (holder.contact_llout_visibility.getVisibility() == View.GONE) {
                            holder.contact_llout_visibility.setVisibility(View.VISIBLE);
                        }
                    }
                });

                holder.frameBackgroung.setBackgroundColor(ContextCompat.getColor(getActivity(), color[position % 6]));
                // ######################  FOR EDT CONTACT 2A ##################### //
                if (edtContact2A != null) {
                    holder.switch_2A.setChecked(true);
                    holder.switch_2A.setHint("");
                    holder.edt2A.setText(edtContact2A);
                    holder.edt2A.setVisibility(View.VISIBLE);
                }

                holder.edt2A.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtContact2A = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtContact2A = editable.toString();
                    }
                });
                holder.switch_2A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_2A.setHint("");
                            holder.edt2A.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("2A");
                        } else {
                            holder.switch_2A.setHint(R.string.switch2a_hint);
                            holder.edt2A.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("2A"));
                        }
                    }
                });
                // ######################  FOR EDT CONTACT 2b ##################### //
                if (edtContact2B != null) {
                    holder.switch_2B.setHint("");
                    holder.switch_2B.setChecked(true);
                    holder.edt2B.setText(edtContact2B);
                    holder.edt2B.setVisibility(View.VISIBLE);
                }

                holder.edt2B.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtContact2B = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtContact2B = editable.toString();
                    }
                });
                holder.switch_2B.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_2B.setHint("");
                            holder.edt2B.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("2B");
                        } else {
                            holder.switch_2B.setHint(R.string.switch2b_hint);
                            holder.edt2B.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("2B"));
                        }
                    }
                });
                // ######################  FOR EDT CONTACT 2C ##################### //
                if (edtContact2C != null) {
                    holder.switch_2C.setHint("");
                    holder.switch_2C.setChecked(true);
                    holder.edt2C.setText(edtContact2C);
                    holder.edt2C.setVisibility(View.VISIBLE);
                }

                holder.edt2C.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtContact2C = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtContact2C = editable.toString();
                    }
                });
                holder.switch_2C.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_2C.setHint("");
                            holder.edt2C.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("2C");
                        } else {
                            holder.switch_2C.setHint(R.string.switch2c_hint);
                            holder.edt2C.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("2C"));
                        }
                    }
                });
                // ######################  FOR EDT CONTACT 2D  ##################### //
                if (edtContact2D != null) {
                    holder.switch_2D.setHint("");
                    holder.switch_2D.setChecked(true);
                    holder.edt2D.setText(edtContact2D);
                    holder.edt2D.setVisibility(View.VISIBLE);
                }

                holder.edt2D.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtContact2D = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtContact2D = editable.toString();
                    }
                });
                holder.switch_2D.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_2D.setHint("");
                            holder.edt2D.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("2D");
                        } else {
                            holder.switch_2D.setHint(R.string.switch2d_hint);
                            holder.edt2D.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("2D"));
                        }
                    }
                });
                // ######################  FOR EDT CONTACT 2E ##################### //
                if (edtContact2E != null) {
                    holder.switch_2E.setHint("");
                    holder.switch_2E.setChecked(true);
                    holder.edt2E.setText(edtContact2E);
                    holder.edt2E.setVisibility(View.VISIBLE);
                }

                holder.edt2E.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtContact2E = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtContact2E = editable.toString();
                    }
                });
                holder.switch_2E.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_2E.setHint("");
                            holder.edt2E.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("2E");
                        } else {
                            holder.switch_2E.setHint(R.string.switch2e_hint);
                            holder.edt2E.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("2E"));
                        }
                    }
                });
                // ######################  FOR EDT CONTACT 2F ##################### //
                if (edtContact2F != null) {
                    holder.switch_2F.setHint("");
                    holder.switch_2F.setChecked(true);
                    holder.edt2F.setText(edtContact2F);
                    holder.edt2F.setVisibility(View.VISIBLE);
                }

                holder.edt2F.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtContact2F = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtContact2F = editable.toString();
                    }
                });
                holder.switch_2F.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_2F.setHint("");
                            holder.edt2F.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("2F");
                        } else {
                            holder.switch_2F.setHint(R.string.switch2f_hint);
                            holder.edt2F.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("2F"));
                        }
                    }
                });
                // ######################  FOR EDT CONTACT 2G ##################### //
                if (edtContact2G != null) {
                    holder.switch_2G.setHint("");
                    holder.switch_2G.setChecked(true);
                    holder.edt2G.setText(edtContact2G);
                    holder.edt2G.setVisibility(View.VISIBLE);
                }

                holder.edt2G.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtContact2G = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtContact2G = editable.toString();
                    }
                });
                holder.switch_2G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_2G.setHint("");
                            holder.edt2G.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("2G");
                        } else {
                            holder.switch_2G.setHint(R.string.switch2g_hint);
                            holder.edt2G.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("2G"));
                        }
                    }
                });

                holder.imgadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerViewRight.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        savePosition = position;
                    }
                });

                holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list_viewType.remove(list_viewType.indexOf(CONTACT));
                        notifyItemRemoved(list_viewType.indexOf(CONTACT));
                    }
                });

            } else if (viewHolder.getItemViewType() == OUR_WORK) {
                final ViewOurWorksHolder holder = (ViewOurWorksHolder) viewHolder;
                holder.ourworks_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.ourwork_llout_visibility.getVisibility() == View.VISIBLE) {
                            holder.ourwork_llout_visibility.setVisibility(View.GONE);
                        } else if (holder.ourwork_llout_visibility.getVisibility() == View.GONE) {
                            holder.ourwork_llout_visibility.setVisibility(View.VISIBLE);
                        }
                    }
                });

                holder.frameBackgroung.setBackgroundColor(ContextCompat.getColor(getActivity(), color[position % 6]));
                if (edtOurWorks4A != null) {
                    holder.switch_4A.setChecked(true);
                    holder.edt4A.setText(edtOurWorks4A);
                    holder.edt4A.setVisibility(View.VISIBLE);
                }
                holder.edt4A.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtOurWorks4A = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtOurWorks4A = editable.toString();
                    }
                });

                holder.switch_4A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.edt4A.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("4A");
                        } else {
                            holder.edt4A.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("4A"));
                        }
                    }
                });
                holder.imgadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerViewRight.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        savePosition = position;
                    }
                });
                holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list_viewType.remove(list_viewType.indexOf(OUR_WORK));
                        notifyItemRemoved(list_viewType.indexOf(OUR_WORK));
                    }
                });
            } else if (viewHolder.getItemViewType() == SHARE) {
                final ViewShareHolder holder = (ViewShareHolder) viewHolder;
                holder.share_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.share_llout_visibility.getVisibility() == View.VISIBLE) {
                            holder.share_llout_visibility.setVisibility(View.GONE);
                        } else if (holder.share_llout_visibility.getVisibility() == View.GONE) {
                            holder.share_llout_visibility.setVisibility(View.VISIBLE);
                        }
                    }
                });
                holder.frameBackgroung.setBackgroundColor(ContextCompat.getColor(getActivity(), color[position % 6]));
                if (edtShare7A != null) {
                    holder.switch_7A.setChecked(true);
                    holder.edt7A.setText(edtShare7A);
                    holder.edt7A.setVisibility(View.VISIBLE);
                }
                holder.edt7A.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtShare7A = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtShare7A = editable.toString();
                    }
                });

                holder.switch_7A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.edt7A.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("7A");
                        } else {
                            holder.edt7A.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("7A"));
                        }
                    }
                });
                holder.imgadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerViewRight.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        savePosition = position;
                    }
                });

                holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list_viewType.remove(list_viewType.indexOf(SHARE));
                        notifyItemRemoved(list_viewType.indexOf(SHARE));
                    }
                });

            } else if (viewHolder.getItemViewType() == QUIZ) {
                final ViewShareHolder holder = (ViewShareHolder) viewHolder;
                holder.share_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.share_llout_visibility.getVisibility() == View.VISIBLE) {
                            holder.share_llout_visibility.setVisibility(View.GONE);
                        } else if (holder.share_llout_visibility.getVisibility() == View.GONE) {
                            holder.share_llout_visibility.setVisibility(View.VISIBLE);
                        }
                    }
                });
                holder.frameBackgroung.setBackgroundColor(ContextCompat.getColor(getActivity(), color[position % 6]));
                holder.textViewName.setText(R.string.title_quiz);
                holder.edt7A.setHint(R.string.quiz_hint);
                if (edtQuiz10A != null) {
                    holder.switch_7A.setChecked(true);
                    holder.edt7A.setText(edtQuiz10A);
                    holder.edt7A.setVisibility(View.VISIBLE);
                }
                holder.edt7A.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtQuiz10A = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtQuiz10A = editable.toString();
                    }
                });

                holder.switch_7A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.edt7A.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("10A");
                        } else {
                            holder.edt7A.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("10A"));
                        }
                    }
                });
                holder.imgadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerViewRight.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        savePosition = position;
                    }
                });

                holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list_viewType.remove(list_viewType.indexOf(QUIZ));
                        notifyItemRemoved(list_viewType.indexOf(QUIZ));
                    }
                });

            } else if (viewHolder.getItemViewType() == VIDEO) {
                final ViewShareHolder holder = (ViewShareHolder) viewHolder;
                holder.share_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.share_llout_visibility.getVisibility() == View.VISIBLE) {
                            holder.share_llout_visibility.setVisibility(View.GONE);
                        } else if (holder.share_llout_visibility.getVisibility() == View.GONE) {
                            holder.share_llout_visibility.setVisibility(View.VISIBLE);
                        }
                    }
                });
                holder.frameBackgroung.setBackgroundColor(ContextCompat.getColor(getActivity(), color[position % 6]));
                holder.textViewName.setText(R.string.title_video);
                holder.edt7A.setHint(R.string.video_hint);
                if (edtVideo13A != null) {
                    holder.switch_7A.setChecked(true);
                    holder.edt7A.setText(edtVideo13A);
                    holder.edt7A.setVisibility(View.VISIBLE);
                }
                holder.edt7A.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtVideo13A = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtVideo13A = editable.toString();
                    }
                });

                holder.switch_7A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.edt7A.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("13A");
                        } else {
                            holder.edt7A.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("13A"));
                        }
                    }
                });
                holder.imgadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerViewRight.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        savePosition = position;
                    }
                });

                holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list_viewType.remove(list_viewType.indexOf(VIDEO));
                        notifyItemRemoved(list_viewType.indexOf(VIDEO));
                    }
                });

            } else if (viewHolder.getItemViewType() == SURVEY) {
                final ViewShareHolder holder = (ViewShareHolder) viewHolder;
                holder.share_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.share_llout_visibility.getVisibility() == View.VISIBLE) {
                            holder.share_llout_visibility.setVisibility(View.GONE);
                        } else if (holder.share_llout_visibility.getVisibility() == View.GONE) {
                            holder.share_llout_visibility.setVisibility(View.VISIBLE);
                        }
                    }
                });
                holder.frameBackgroung.setBackgroundColor(ContextCompat.getColor(getActivity(), color[position % 6]));
                holder.edt7A.setHint(R.string.survey_hint);
                holder.textViewName.setText(R.string.title_survey);
                if (edtSurvey11A != null) {
                    holder.switch_7A.setChecked(true);
                    holder.edt7A.setText(edtSurvey11A);
                    holder.edt7A.setVisibility(View.VISIBLE);
                }
                holder.edt7A.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtSurvey11A = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtSurvey11A = editable.toString();
                    }
                });

                holder.switch_7A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.edt7A.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("11A");
                        } else {
                            holder.edt7A.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("11A"));
                        }
                    }
                });
                holder.imgadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerViewRight.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        savePosition = position;
                    }
                });

                holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list_viewType.remove(list_viewType.indexOf(SURVEY));
                        notifyItemRemoved(list_viewType.indexOf(SURVEY));
                    }
                });

            } else if (viewHolder.getItemViewType() == QR_CODE) {
                final ViewQRCodeHolder holder = (ViewQRCodeHolder) viewHolder;
                holder.qrcode_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.qrcode_llout_visibility.getVisibility() == View.VISIBLE) {
                            holder.qrcode_llout_visibility.setVisibility(View.GONE);
                        } else if (holder.qrcode_llout_visibility.getVisibility() == View.GONE) {
                            holder.qrcode_llout_visibility.setVisibility(View.VISIBLE);
                        }
                    }
                });
                if (AttributeCheckList.indexOf("9A") != -1) {
                    holder.switch_9A.setChecked(true);
                    holder.edt9A.setText(edtQRCode9A);
                    holder.edt9A.setVisibility(View.VISIBLE);
                }

                QRCodeWriter writer = new QRCodeWriter();
                try {
                    BitMatrix bitMatrix = writer.encode(edtQRCode9A, BarcodeFormat.QR_CODE, 512, 512);
                    int width = bitMatrix.getWidth();
                    int height = bitMatrix.getHeight();
                    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    holder.imgqrcode.setImageBitmap(bmp);

                } catch (WriterException e) {
                    e.printStackTrace();
                }

                holder.imgqrcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QRCodeWriter writer = new QRCodeWriter();
                        try {
                            BitMatrix bitMatrix = writer.encode(holder.edt9A.getText().toString(), BarcodeFormat.QR_CODE, 512, 512);
                            int width = bitMatrix.getWidth();
                            int height = bitMatrix.getHeight();
                            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                            for (int x = 0; x < width; x++) {
                                for (int y = 0; y < height; y++) {
                                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                                }
                            }
                            holder.imgqrcode.setImageBitmap(bmp);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                        if (Utility.checkWritePermission(getActivity())) {
                            File filepath = Environment.getExternalStorageDirectory();

                            // Create a new folder in SD Card
                            File dir = new File(filepath.getAbsolutePath()
                                    + "/QR_CODE/");
                            dir.mkdirs();

                            // Create a name for the saved image
                            File file = new File(dir, "qrcode_" + getSaltString() + ".png");

                            // Show a toast message on successful save
                            Toast.makeText(getActivity().getApplicationContext(), R.string.qrcode_savw_notice,
                                    Toast.LENGTH_SHORT).show();
                            try {
                                OutputStream output = null;
                                output = new FileOutputStream(file);
                                Bitmap bitmapmyLogo = ((BitmapDrawable) holder.imgqrcode.getDrawable()).getBitmap();
                                bitmapmyLogo.compress(Bitmap.CompressFormat.PNG, 100, output);
                                output.flush();
                                output.close();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                });

                holder.frameBackgroung.setBackgroundColor(ContextCompat.getColor(getActivity(), color[position % 6]));
                holder.edt9A.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edtQRCode9A = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edtQRCode9A = editable.toString();
                    }
                });

                holder.switch_9A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.edt9A.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("9A");
                        } else {
                            holder.edt9A.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("9A"));
                        }
                    }
                });
                holder.imgadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerViewRight.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        savePosition = position;
                    }
                });

                holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list_viewType.remove(list_viewType.indexOf(QR_CODE));
                        notifyItemRemoved(list_viewType.indexOf(QR_CODE));
                    }
                });

            } else if (viewHolder.getItemViewType() == MAP) {
                final ViewMapHolder holder = (ViewMapHolder) viewHolder;
                holder.map_frameBackgroung.setBackgroundColor(ContextCompat.getColor(getActivity(), color[position % 6]));
                holder.map_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.frame_map_llout.getVisibility() == View.VISIBLE) {
                            holder.frame_map_llout.setVisibility(View.GONE);
                        } else if (holder.frame_map_llout.getVisibility() == View.GONE) {
                            holder.frame_map_llout.setVisibility(View.VISIBLE);
                        }
                    }
                });
                if (userLocation != null) {
                    holder.switch_5A.setChecked(true);
                    holder.imgMap.setVisibility(View.VISIBLE);
                  //  Log.e("###", userLocation.getLatitude() + "&" + userLocation.getLongitude());
                }

                holder.imgMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendLocation();
                    }
                });
                holder.switch_5A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.imgMap.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("5A");
                        } else {
                            holder.imgMap.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("5A"));
                        }
                    }
                });
                holder.imgadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerViewRight.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        savePosition = position;
                    }
                });

                holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list_viewType.remove(list_viewType.indexOf(MAP));
                        notifyItemRemoved(list_viewType.indexOf(MAP));
                    }
                });

            } else if (viewHolder.getItemViewType() == FEEDBACK) {
                final ViewFeedbackHolder holder = (ViewFeedbackHolder) viewHolder;
                holder.feedback_frameBackgroung.setBackgroundColor(ContextCompat.getColor(getActivity(), color[position % 6]));
                holder.feedback_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.frame_feedback_llout.getVisibility() == View.VISIBLE) {
                            holder.frame_feedback_llout.setVisibility(View.GONE);
                        } else if (holder.frame_feedback_llout.getVisibility() == View.GONE) {
                            holder.frame_feedback_llout.setVisibility(View.VISIBLE);
                        }
                    }
                });
                if (IsFeedbackFeatureON) {
                    holder.switch_8A.setChecked(true);
                    holder.imgFeedback.setVisibility(View.VISIBLE);
                }

                holder.imgFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar snackbar = Snackbar.make(rootDesignOne, R.string.feedback_notice, Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                        snackbar.show();

                    }
                });
                holder.switch_8A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.imgFeedback.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("8A");
                        } else {
                            holder.imgFeedback.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("8A"));
                        }
                    }
                });
                holder.imgadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerViewRight.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        savePosition = position;
                    }
                });

                holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list_viewType.remove(list_viewType.indexOf(FEEDBACK));
                        notifyItemRemoved(list_viewType.indexOf(FEEDBACK));
                    }
                });

            } else if (viewHolder.getItemViewType() == CHAT) {
                final ViewChatHolder holder = (ViewChatHolder) viewHolder;
                holder.chat_frameBackgroung.setBackgroundColor(ContextCompat.getColor(getActivity(), color[position % 6]));
                holder.chat_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.frame_chat_llout.getVisibility() == View.VISIBLE) {
                            holder.frame_chat_llout.setVisibility(View.GONE);
                        } else if (holder.frame_chat_llout.getVisibility() == View.GONE) {
                            holder.frame_chat_llout.setVisibility(View.VISIBLE);
                        }
                    }
                });
                if (IsChatFeatureON) {
                    holder.imgChat.setVisibility(View.VISIBLE);
                    holder.switch_6A.setChecked(true);
                }
                holder.imgChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar snackbar = Snackbar.make(rootDesignOne, R.string.chat_feature_on, Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                        snackbar.show();

                    }
                });
                holder.switch_6A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            Snackbar snackbar = Snackbar.make(rootDesignOne, R.string.chat_feature_on, Snackbar.LENGTH_SHORT);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                            snackbar.show();

                            holder.imgChat.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("6A");
                        } else {
                            Snackbar snackbar = Snackbar.make(rootDesignOne, R.string.chat_feature_off, Snackbar.LENGTH_SHORT);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                            snackbar.show();

                            holder.imgChat.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("6A"));
                        }
                    }
                });
                holder.imgadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerViewRight.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        savePosition = position;
                    }
                });

                holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list_viewType.remove(list_viewType.indexOf(CHAT));
                        notifyItemRemoved(list_viewType.indexOf(CHAT));
                    }
                });

            } else if (viewHolder.getItemViewType() == DASHBOARD) {
                final ViewDashboardHolder holder = (ViewDashboardHolder) viewHolder;
                holder.dashboard_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.dashboard_llout_visibility.getVisibility() == View.VISIBLE) {
                            holder.dashboard_llout_visibility.setVisibility(View.GONE);
                        } else if (holder.dashboard_llout_visibility.getVisibility() == View.GONE) {
                            holder.dashboard_llout_visibility.setVisibility(View.VISIBLE);
                        }
                    }
                });
                //  Condition checked for all 0A,0B,0C...........  //
                for (int i = 0; i < AttributeCheckList.size(); i++) {
                    if (AttributeCheckList.get(i).equals("0A")) {
                        holder.switch_0A.setChecked(true);
                        holder.switch_0A.setHint("");
                        holder.llout_recyclerDashboard.setVisibility(View.VISIBLE);
                    }
                    if (AttributeCheckList.get(i).equals("0B")) {
                        holder.switch_0B.setChecked(true);
                        holder.switch_0B.setHint("");
                        holder.llout_recyclerDashboard_deals_ofthe_day.setVisibility(View.VISIBLE);
                    }

                    if (AttributeCheckList.get(i).equals("0C")) {
                        holder.switch_0C.setChecked(true);
                        holder.switch_0C.setHint("");
                        holder.llout_recyclerDashboard_discount_for_you.setVisibility(View.VISIBLE);
                    }
                    if (AttributeCheckList.get(i).equals("0D")) {
                        holder.switch_0D.setChecked(true);
                        holder.switch_0D.setHint("");
                        holder.llout_recyclerDashboard_colorful_card.setVisibility(View.VISIBLE);
                    }
                }
                //  ############### 0A  ################### //
                holder.recyclerDashboard.setHasFixedSize(true);
                holder.framellout_dashboard.setBackgroundColor(ContextCompat.getColor(getActivity(), color[position % 6]));
                holder.layoutDashboard = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                holder.recyclerDashboard.setLayoutManager(holder.layoutDashboard);
                holder.adapterDashboard = new CardDashBoardAdapter();
                holder.recyclerDashboard.setAdapter(holder.adapterDashboard);

                holder.switch_0A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_0A.setHint("");
                            holder.llout_recyclerDashboard.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("0A");

                        } else {
                            holder.switch_0A.setHint(R.string.switch0a_hint);
                            holder.llout_recyclerDashboard.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("0A"));
                        }
                    }
                });

                //  ############### 0B ################### //
                if (edttitle0B != null) {
                    holder.edt_dashboard_deals_ofthe_day.setText(edttitle0B);
                }
                if (spinnertitle0B != null)
                    holder.spinner_switch0B.setSelection(Integer.parseInt(spinnertitle0B));

                holder.recyclerDashboard_dealsofthe_day.setHasFixedSize(true);
                holder.layoutDashboard_deals_ofthe_day = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                holder.recyclerDashboard_dealsofthe_day.setLayoutManager(holder.layoutDashboard_deals_ofthe_day);
                holder.adapterDashboard_deals_ofthe_day = new CardDashBoard_Deals_of_Adapter();
                holder.recyclerDashboard_dealsofthe_day.setAdapter(holder.adapterDashboard_deals_ofthe_day);
                PortfolioSpinner3AAdapter customAdapter = new PortfolioSpinner3AAdapter(getActivity(), orientationtheme);
                holder.spinner_switch0B.setAdapter(customAdapter);
                if (spinnertitle0B != null)
                    holder.spinner_switch0B.setSelection(Integer.parseInt(spinnertitle0B));

                holder.spinner_switch0B.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spinnertitle0B = String.valueOf(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                holder.switch_0B.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_0B.setHint("");
                            holder.llout_recyclerDashboard_deals_ofthe_day.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("0B");

                        } else {
                            holder.switch_0B.setHint(R.string.switch0b_hint);
                            holder.llout_recyclerDashboard_deals_ofthe_day.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("0B"));
                        }
                    }
                });

                holder.edt_dashboard_deals_ofthe_day.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edttitle0B = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edttitle0B = editable.toString();
                    }
                });

                //  ############### 0C ################### //
                if (edttitle0C != null) {
                    holder.edt_dashboard_discount_for_you.setText(edttitle0C);
                }

                holder.recyclerDashboard_discount_for_you.setHasFixedSize(true);
                holder.layoutDashboard_discount_for_you = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                holder.recyclerDashboard_discount_for_you.setLayoutManager(holder.layoutDashboard_discount_for_you);
                holder.adapterDashboard_discount_for_you = new CardDashBoard_Discount_for_you_Adapter();
                holder.recyclerDashboard_discount_for_you.setAdapter(holder.adapterDashboard_discount_for_you);
                PortfolioSpinner3AAdapter custom0CAdapter = new PortfolioSpinner3AAdapter(getActivity(), orientationtheme);
                holder.spinner_switch0C.setAdapter(custom0CAdapter);
                if (spinnertitle0C != null)
                    holder.spinner_switch0C.setSelection(Integer.parseInt(spinnertitle0C));

                holder.spinner_switch0C.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spinnertitle0C = String.valueOf(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                holder.switch_0C.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_0C.setHint("");
                            holder.llout_recyclerDashboard_discount_for_you.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("0C");

                        } else {
                            holder.switch_0C.setHint(R.string.switch0b_hint);
                            holder.llout_recyclerDashboard_discount_for_you.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("0C"));
                        }
                    }
                });

                holder.edt_dashboard_discount_for_you.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edttitle0C = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edttitle0C = editable.toString();
                    }
                });

                //  ############### 0D ################### //

                if (edttitle0D != null) {
                    holder.edt_dashboard_colorful_card.setText(edttitle0D);
                }

                holder.recyclerDashboard_colorful_card.setHasFixedSize(true);
                holder.layoutDashboard_colorful_card = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                holder.recyclerDashboard_colorful_card.setLayoutManager(holder.layoutDashboard_colorful_card);
                holder.adapterDashboard_colorful_card = new CardDashBoard_Colorful_Card_Adapter();
                holder.recyclerDashboard_colorful_card.setAdapter(holder.adapterDashboard_colorful_card);

                PortfolioSpinner3AAdapter custom0DAdapter = new PortfolioSpinner3AAdapter(getActivity(), orientationtheme);
                holder.spinner_switch0D.setAdapter(custom0DAdapter);
                if (spinnertitle0D != null)
                    holder.spinner_switch0D.setSelection(Integer.parseInt(spinnertitle0D));

                holder.spinner_switch0D.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spinnertitle0D = String.valueOf(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                holder.switch_0D.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.switch_0D.setHint("");
                            holder.llout_recyclerDashboard_colorful_card.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("0D");

                        } else {
                            holder.switch_0D.setHint(R.string.switch0d_hint);
                            holder.llout_recyclerDashboard_colorful_card.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("0D"));
                        }
                    }
                });

                holder.edt_dashboard_colorful_card.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        edttitle0D = charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        edttitle0D = editable.toString();
                    }
                });


                //  +++++++++++++ Dashboard page Add functionality  ++++++++++++   ///
                holder.imgadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerViewRight.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        savePosition = position + 1;
                    }
                });


            } else if (viewHolder.getItemViewType() == DOCUMENT) {
                final ViewDocumentHolder holder = (ViewDocumentHolder) viewHolder;
                holder.framellout_document.setBackgroundColor(ContextCompat.getColor(getActivity(), color[position % 6]));
                holder.imggoogledrive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com")));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Snackbar snackbar = Snackbar.make(rootDesignOne, R.string.document_url_notice, Snackbar.LENGTH_SHORT);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                            snackbar.show();

                        }
                    }
                });
                holder.document_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.document_llout_visibility.getVisibility() == View.VISIBLE) {
                            holder.document_llout_visibility.setVisibility(View.GONE);
                        } else if (holder.document_llout_visibility.getVisibility() == View.GONE) {
                            holder.document_llout_visibility.setVisibility(View.VISIBLE);
                        }
                    }
                });
                for (int i = 0; i < AttributeCheckList.size(); i++) {
                    if (AttributeCheckList.get(i).equals("12A")) {
                        holder.switch_12A.setChecked(true);
                        holder.llout_recycler_document.setVisibility(View.VISIBLE);
                    }
                }

                //  ############### 12A ################### //

                holder.recycler_Document.setHasFixedSize(true);
                holder.layout_Document = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                holder.recycler_Document.setLayoutManager(holder.layout_Document);
                holder.adapter_Document = new CardDocument_Adapter();
                holder.recycler_Document.setAdapter(holder.adapter_Document);

                PortfolioSpinner3AAdapter custom12AAdapter = new PortfolioSpinner3AAdapter(getActivity(), orientationtheme);
                holder.spinner_switch12A.setAdapter(custom12AAdapter);
                if (spinnertitle12A != null)
                    holder.spinner_switch12A.setSelection(Integer.parseInt(spinnertitle12A));

                holder.spinner_switch12A.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spinnertitle12A = String.valueOf(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                holder.switch_12A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.llout_recycler_document.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("12A");

                        } else {
                            holder.llout_recycler_document.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("12A"));
                        }
                    }
                });


                //  +++++++++++++ Document page Add/Delete functionality  ++++++++++++   ///

                holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list_viewType.remove(list_viewType.indexOf(DOCUMENT));
                        notifyItemRemoved(list_viewType.indexOf(DOCUMENT));
                    }
                });
                holder.imgadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerViewRight.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        savePosition = position;
                    }
                });


            } else if (viewHolder.getItemViewType() == PORTPOLIO) {
                final ViewPortfolioHolder holder = (ViewPortfolioHolder) viewHolder;
                holder.portfolio_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.portfolio_llout_visibility.getVisibility() == View.VISIBLE) {
                            holder.portfolio_llout_visibility.setVisibility(View.GONE);
                        } else if (holder.portfolio_llout_visibility.getVisibility() == View.GONE) {
                            holder.portfolio_llout_visibility.setVisibility(View.VISIBLE);
                        }
                    }
                });

                PortfolioSpinner3AAdapter customAdapter = new PortfolioSpinner3AAdapter(getActivity(), imageSpinnerthemelist);
                holder.spinnerPortfolioLayoutSelection.setAdapter(customAdapter);

                holder.recyclerPortfolio.setHasFixedSize(true);
                holder.layoutPortfolio = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

                holder.recyclerPortfolio.setLayoutManager(holder.layoutPortfolio);
                holder.framellout_portpolio.setBackgroundColor(ContextCompat.getColor(getActivity(), color[position % 6]));
                holder.adapterPortfolio = new CardPortfolioAdapter();
                holder.recyclerPortfolio.setAdapter(holder.adapterPortfolio);

                Boolean flag = false;
                for (int i = 0; i < AttributeCheckList.size(); i++) {
                    if (AttributeCheckList.get(i).equals("3A")) {
                        flag = true;
                    }
                }
                if (flag) {
                    holder.switch_3A.setChecked(true);
                    holder.llout_recycler_portfolio_visibility.setVisibility(View.VISIBLE);
                 //   Log.e("@@@@", edttitle3A + "");
                    if (edttitle3A != null) {
                        holder.spinnerPortfolioLayoutSelection.setSelection(Integer.parseInt(edttitle3A));
                    }
                }
                holder.switch_3A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            holder.llout_recycler_portfolio_visibility.setVisibility(View.VISIBLE);
                            AttributeCheckList.add("3A");
                        } else {
                            holder.llout_recycler_portfolio_visibility.setVisibility(View.GONE);
                            AttributeCheckList.remove(AttributeCheckList.indexOf("3A"));
                        }
                    }
                });
                holder.spinnerPortfolioLayoutSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        edttitle3A = String.valueOf(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list_viewType.remove(list_viewType.indexOf(PORTPOLIO));
                        notifyItemRemoved(list_viewType.indexOf(PORTPOLIO));
                    }
                });
                holder.imgadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerViewRight.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        savePosition = position;
                    }
                });


            }
        }

        @Override
        public int getItemCount() {
            return list_viewType.size();
        }

        @Override
        public int getItemViewType(int position) {
            return list_viewType.get(position);
        }

        class ViewAboutHolder extends ViewHolder {
            public TextView textViewName;
            ImageView imgadd, imgdelete;
            LinearLayout frameBackgroung, about_llout_visibility;
            CardView about_cardview;
            EditText edt1A, edt1B, edt1C, edt1D, edt1E, edt1F, edt1G, edt1H, edt1I, edt1J, edt1K, edt1L;
            Switch switch_1A, switch_1B, switch_1C, switch_1D, switch_1E, switch_1F, switch_1G, switch_1H, switch_1I, switch_1J, switch_1K, switch_1L;

            public ViewAboutHolder(View itemView) {
                super(itemView);
                textViewName = (TextView) itemView.findViewById(R.id.textViewName);
                imgadd = (ImageView) itemView.findViewById(R.id.img_add_design);
                frameBackgroung = (LinearLayout) itemView.findViewById(R.id.frame_backgroung_design);
                imgdelete = (ImageView) itemView.findViewById(R.id.img_delete_design);
                edt1A = (EditText) itemView.findViewById(R.id.edt1A);
                edt1B = (EditText) itemView.findViewById(R.id.edt1B);
                edt1C = (EditText) itemView.findViewById(R.id.edt1C);
                edt1D = (EditText) itemView.findViewById(R.id.edt1D);
                edt1E = (EditText) itemView.findViewById(R.id.edt1E);
                edt1F = (EditText) itemView.findViewById(R.id.edt1F);
                edt1G = (EditText) itemView.findViewById(R.id.edt1G);
                edt1H = (EditText) itemView.findViewById(R.id.edt1H);
                edt1I = (EditText) itemView.findViewById(R.id.edt1I);
                edt1J = (EditText) itemView.findViewById(R.id.edt1J);
                edt1K = (EditText) itemView.findViewById(R.id.edt1K);
                edt1L = (EditText) itemView.findViewById(R.id.edt1L);
                switch_1A = (Switch) itemView.findViewById(R.id.switch_1A);
                switch_1B = (Switch) itemView.findViewById(R.id.switch_1B);
                switch_1C = (Switch) itemView.findViewById(R.id.switch_1C);
                switch_1D = (Switch) itemView.findViewById(R.id.switch_1D);
                switch_1E = (Switch) itemView.findViewById(R.id.switch_1E);
                switch_1F = (Switch) itemView.findViewById(R.id.switch_1F);
                switch_1G = (Switch) itemView.findViewById(R.id.switch_1G);
                switch_1H = (Switch) itemView.findViewById(R.id.switch_1H);
                switch_1I = (Switch) itemView.findViewById(R.id.switch_1I);
                switch_1J = (Switch) itemView.findViewById(R.id.switch_1J);
                switch_1K = (Switch) itemView.findViewById(R.id.switch_1K);
                switch_1L = (Switch) itemView.findViewById(R.id.switch_1L);
                about_cardview = (CardView) itemView.findViewById(R.id.about_cardview);
                about_llout_visibility = (LinearLayout) itemView.findViewById(R.id.about_llout_visibility);
            }
        }

        class ViewContactUsHolder extends ViewHolder {
            public TextView textViewName;
            ImageView imgadd, imgdelete;
            LinearLayout contact_llout_visibility, frameBackgroung;
            CardView contact_cardview;
            EditText edt2A, edt2B, edt2C, edt2D, edt2E, edt2F, edt2G;
            Switch switch_2A, switch_2B, switch_2C, switch_2D, switch_2E, switch_2F, switch_2G;

            public ViewContactUsHolder(View itemView) {
                super(itemView);
                textViewName = (TextView) itemView.findViewById(R.id.textViewName);
                imgadd = (ImageView) itemView.findViewById(R.id.img_add_design);
                frameBackgroung = (LinearLayout) itemView.findViewById(R.id.frame_backgroung_design);
                contact_cardview = (CardView) itemView.findViewById(R.id.contact_cardview);
                contact_llout_visibility = (LinearLayout) itemView.findViewById(R.id.contactus_llout_visibility);
                imgdelete = (ImageView) itemView.findViewById(R.id.img_delete_design);
                edt2A = (EditText) itemView.findViewById(R.id.edt2A);
                edt2B = (EditText) itemView.findViewById(R.id.edt2B);
                edt2C = (EditText) itemView.findViewById(R.id.edt2C);
                edt2D = (EditText) itemView.findViewById(R.id.edt2D);
                edt2E = (EditText) itemView.findViewById(R.id.edt2E);
                edt2F = (EditText) itemView.findViewById(R.id.edt2F);
                edt2G = (EditText) itemView.findViewById(R.id.edt2G);
                switch_2A = (Switch) itemView.findViewById(R.id.switch_2A);
                switch_2B = (Switch) itemView.findViewById(R.id.switch_2B);
                switch_2C = (Switch) itemView.findViewById(R.id.switch_2C);
                switch_2D = (Switch) itemView.findViewById(R.id.switch_2D);
                switch_2E = (Switch) itemView.findViewById(R.id.switch_2E);
                switch_2F = (Switch) itemView.findViewById(R.id.switch_2F);
                switch_2G = (Switch) itemView.findViewById(R.id.switch_2G);

            }
        }

        class ViewOurWorksHolder extends ViewHolder {
            public TextView textViewName;
            ImageView imgadd, imgdelete;
            LinearLayout frameBackgroung, ourwork_llout_visibility;
            CardView ourworks_cardview;
            EditText edt4A;
            Switch switch_4A;

            public ViewOurWorksHolder(View itemView) {
                super(itemView);
                textViewName = (TextView) itemView.findViewById(R.id.textViewName);
                imgadd = (ImageView) itemView.findViewById(R.id.img_add_design);
                frameBackgroung = (LinearLayout) itemView.findViewById(R.id.frame_backgroung_design);
                imgdelete = (ImageView) itemView.findViewById(R.id.img_delete_design);
                edt4A = (EditText) itemView.findViewById(R.id.edt4A);
                switch_4A = (Switch) itemView.findViewById(R.id.switch_4A);
                ourwork_llout_visibility = (LinearLayout) itemView.findViewById(R.id.ourworks_llout_visibility);
                ourworks_cardview = (CardView) itemView.findViewById(R.id.ourworks_cardview);
            }
        }


        class ViewShareHolder extends ViewHolder {
            public TextView textViewName;
            ImageView imgadd, imgdelete;
            LinearLayout frameBackgroung, share_llout_visibility;
            CardView share_cardview;
            EditText edt7A;
            Switch switch_7A;

            public ViewShareHolder(View itemView) {
                super(itemView);
                textViewName = (TextView) itemView.findViewById(R.id.textViewName);
                imgadd = (ImageView) itemView.findViewById(R.id.img_add_design);
                frameBackgroung = (LinearLayout) itemView.findViewById(R.id.frame_backgroung_design);
                imgdelete = (ImageView) itemView.findViewById(R.id.img_delete_design);
                edt7A = (EditText) itemView.findViewById(R.id.edt7A);
                switch_7A = (Switch) itemView.findViewById(R.id.switch_7A);
                share_llout_visibility = (LinearLayout) itemView.findViewById(R.id.share_llout_visibility);
                share_cardview = (CardView) itemView.findViewById(R.id.share_cardview);
            }
        }

        class ViewQRCodeHolder extends ViewHolder {
            public TextView textViewName;
            ImageView imgadd, imgdelete, imgqrcode;
            LinearLayout frameBackgroung, qrcode_llout_visibility;
            CardView qrcode_cardview;
            EditText edt9A;
            Switch switch_9A;

            public ViewQRCodeHolder(View itemView) {
                super(itemView);
                textViewName = (TextView) itemView.findViewById(R.id.textViewName);
                imgadd = (ImageView) itemView.findViewById(R.id.img_add_design);
                imgqrcode = (ImageView) itemView.findViewById(R.id.img_qrcode);
                frameBackgroung = (LinearLayout) itemView.findViewById(R.id.frame_backgroung_design);
                imgdelete = (ImageView) itemView.findViewById(R.id.img_delete_design);
                edt9A = (EditText) itemView.findViewById(R.id.edt9A);
                switch_9A = (Switch) itemView.findViewById(R.id.switch_9A);
                qrcode_llout_visibility = (LinearLayout) itemView.findViewById(R.id.qrcode_llout_visibility);
                qrcode_cardview = (CardView) itemView.findViewById(R.id.qrcode_cardview);
            }
        }

        class ViewFeedbackHolder extends ViewHolder {
            ImageView imgFeedback;
            ImageView imgadd, imgdelete;
            LinearLayout feedback_frameBackgroung;
            private Switch switch_8A;
            CardView feedback_cardview;
            FrameLayout frame_feedback_llout;

            public ViewFeedbackHolder(View itemView) {
                super(itemView);
                imgadd = (ImageView) itemView.findViewById(R.id.img_add_design);
                imgFeedback = (ImageView) itemView.findViewById(R.id.img_feedback_card);
                feedback_cardview = (CardView) itemView.findViewById(R.id.feedback_list_cardview);
                imgdelete = (ImageView) itemView.findViewById(R.id.img_delete_design);
                feedback_frameBackgroung = (LinearLayout) itemView.findViewById(R.id.feedback_frame_backgroung_design);
                switch_8A = (Switch) itemView.findViewById(R.id.switch_8A);
                frame_feedback_llout = (FrameLayout) itemView.findViewById(R.id.feedback_frame_llout);
            }
        }


        class ViewDashboardHolder extends ViewHolder {
            // For Dashboard -OA   //
            private RecyclerView recyclerDashboard;
            private RecyclerView.LayoutManager layoutDashboard;
            private RecyclerView.Adapter adapterDashboard;
            private LinearLayout llout_recyclerDashboard;
            private Switch switch_0A;
            // For Dashboard 0B  //
            private RecyclerView recyclerDashboard_dealsofthe_day;
            private RecyclerView.LayoutManager layoutDashboard_deals_ofthe_day;
            private RecyclerView.Adapter adapterDashboard_deals_ofthe_day;
            private LinearLayout llout_recyclerDashboard_deals_ofthe_day;
            private Switch switch_0B;
            private EditText edt_dashboard_deals_ofthe_day;
            private Spinner spinner_switch0B;

            // For Dashboard 0C //
            private RecyclerView recyclerDashboard_discount_for_you;
            private RecyclerView.LayoutManager layoutDashboard_discount_for_you;
            private RecyclerView.Adapter adapterDashboard_discount_for_you;
            private LinearLayout llout_recyclerDashboard_discount_for_you;
            private Switch switch_0C;
            EditText edt_dashboard_discount_for_you;
            private Spinner spinner_switch0C;

            // For Dashboard 0D //
            private RecyclerView recyclerDashboard_colorful_card;
            private RecyclerView.LayoutManager layoutDashboard_colorful_card;
            private RecyclerView.Adapter adapterDashboard_colorful_card;
            private LinearLayout llout_recyclerDashboard_colorful_card;
            private Switch switch_0D;
            EditText edt_dashboard_colorful_card;
            private Spinner spinner_switch0D;

            // Dashboard General variables
            private ImageView imgadd;
            private LinearLayout framellout_dashboard, dashboard_llout_visibility;
            CardView dashboard_cardview;

            public ViewDashboardHolder(View itemView) {
                super(itemView);
                //  For 0D
                recyclerDashboard_colorful_card = (RecyclerView) itemView.findViewById(R.id.recycler_colorful_card);
                llout_recyclerDashboard_colorful_card = (LinearLayout) itemView.findViewById(R.id.llout_recycler_colorful_card);
                switch_0D = (Switch) itemView.findViewById(R.id.switch_0D);
                edt_dashboard_colorful_card = (EditText) itemView.findViewById(R.id.edt_colorful_card_title);
                spinner_switch0D = (Spinner) itemView.findViewById(R.id.spinner_switch0D);

                //  For 0C
                recyclerDashboard_discount_for_you = (RecyclerView) itemView.findViewById(R.id.recycler_discount_for_you);
                llout_recyclerDashboard_discount_for_you = (LinearLayout) itemView.findViewById(R.id.llout_recycler_discount_for_you);
                switch_0C = (Switch) itemView.findViewById(R.id.switch_0C);
                edt_dashboard_discount_for_you = (EditText) itemView.findViewById(R.id.edt_discount_for_you_title);
                spinner_switch0C = (Spinner) itemView.findViewById(R.id.spinner_switch0C);

                //  For 0B
                recyclerDashboard_dealsofthe_day = (RecyclerView) itemView.findViewById(R.id.recycler_deals_of_the_day);
                llout_recyclerDashboard_deals_ofthe_day = (LinearLayout) itemView.findViewById(R.id.llout_recycler_deals_of_the_day);
                switch_0B = (Switch) itemView.findViewById(R.id.switch_0B);
                edt_dashboard_deals_ofthe_day = (EditText) itemView.findViewById(R.id.edt_deals_of_the_day_title);
                spinner_switch0B = (Spinner) itemView.findViewById(R.id.spinner_switch0B);

                //  For OA
                recyclerDashboard = (RecyclerView) itemView.findViewById(R.id.recyclerHorizontal);
                llout_recyclerDashboard = (LinearLayout) itemView.findViewById(R.id.llout_recyclerHorizontal);
                switch_0A = (Switch) itemView.findViewById(R.id.switch_0A);
                imgadd = (ImageView) itemView.findViewById(R.id.img_add_design);
                framellout_dashboard = (LinearLayout) itemView.findViewById(R.id.framellout_dashboard);
                dashboard_llout_visibility = (LinearLayout) itemView.findViewById(R.id.dashboard_llout_visibility);
                dashboard_cardview = (CardView) itemView.findViewById(R.id.dashboard_cardview);
            }
        }

        class ViewDocumentHolder extends ViewHolder {

            // For Document 12A //
            private RecyclerView recycler_Document;
            private RecyclerView.LayoutManager layout_Document;
            private RecyclerView.Adapter adapter_Document;
            private LinearLayout llout_recycler_document;
            private Switch switch_12A;
            private Spinner spinner_switch12A;

            // Dashboard General variables
            private ImageView imgadd, imgdelete, imggoogledrive;
            private LinearLayout framellout_document, document_llout_visibility;
            CardView document_cardview;

            public ViewDocumentHolder(View itemView) {
                super(itemView);
                //  For 12A
                recycler_Document = (RecyclerView) itemView.findViewById(R.id.recycler_document);
                llout_recycler_document = (LinearLayout) itemView.findViewById(R.id.llout_recycler_document);
                switch_12A = (Switch) itemView.findViewById(R.id.switch_12A);
                spinner_switch12A = (Spinner) itemView.findViewById(R.id.spinner_switch12A);
                imgadd = (ImageView) itemView.findViewById(R.id.img_add_design);
                imggoogledrive = (ImageView) itemView.findViewById(R.id.img_google_drive);
                imgdelete = (ImageView) itemView.findViewById(R.id.img_delete_design);
                framellout_document = (LinearLayout) itemView.findViewById(R.id.framellout_document);
                document_llout_visibility = (LinearLayout) itemView.findViewById(R.id.document_llout_visibility);
                document_cardview = (CardView) itemView.findViewById(R.id.document_cardview);
            }
        }


        class ViewPortfolioHolder extends ViewHolder {
            public RecyclerView recyclerPortfolio;
            private RecyclerView.LayoutManager layoutPortfolio;
            private RecyclerView.Adapter adapterPortfolio;
            private ImageView imgadd, imgdelete;
            private Switch switch_3A;
            private LinearLayout framellout_portpolio, portfolio_llout_visibility, llout_recycler_portfolio_visibility;
            CardView portfolio_cardview;
            Spinner spinnerPortfolioLayoutSelection;

            public ViewPortfolioHolder(View itemView) {
                super(itemView);
                recyclerPortfolio = (RecyclerView) itemView.findViewById(R.id.recyclerHorizontal);
                imgadd = (ImageView) itemView.findViewById(R.id.img_add_design);
                imgdelete = (ImageView) itemView.findViewById(R.id.img_delete_design);
                switch_3A = (Switch) itemView.findViewById(R.id.switch_3A);
                framellout_portpolio = (LinearLayout) itemView.findViewById(R.id.framellout_portpolio);
                llout_recycler_portfolio_visibility = (LinearLayout) itemView.findViewById(R.id.llout_recycler_portfolio_card);
                portfolio_llout_visibility = (LinearLayout) itemView.findViewById(R.id.portfolio_llout_visibility);
                portfolio_cardview = (CardView) itemView.findViewById(R.id.portfolio_cardview);
                spinnerPortfolioLayoutSelection = (Spinner) itemView.findViewById(R.id.portfolio_spinner_card);
            }
        }

        class ViewMapHolder extends ViewHolder {
            ImageView imgMap;
            ImageView imgadd, imgdelete;
            LinearLayout map_frameBackgroung;
            private Switch switch_5A;
            CardView map_cardview;
            FrameLayout frame_map_llout;

            public ViewMapHolder(View itemView) {
                super(itemView);
                imgadd = (ImageView) itemView.findViewById(R.id.img_add_design);
                imgMap = (ImageView) itemView.findViewById(R.id.img_map_card);
                map_cardview = (CardView) itemView.findViewById(R.id.map_list_cardview);
                imgdelete = (ImageView) itemView.findViewById(R.id.img_delete_design);
                map_frameBackgroung = (LinearLayout) itemView.findViewById(R.id.map_frame_backgroung_design);
                switch_5A = (Switch) itemView.findViewById(R.id.switch_5A);
                frame_map_llout = (FrameLayout) itemView.findViewById(R.id.map_frame_llout);
            }
        }

        class ViewChatHolder extends ViewHolder {
            ImageView imgChat;
            ImageView imgadd, imgdelete;
            LinearLayout chat_frameBackgroung;
            private Switch switch_6A;
            CardView chat_cardview;
            FrameLayout frame_chat_llout;

            public ViewChatHolder(View itemView) {
                super(itemView);
                imgadd = (ImageView) itemView.findViewById(R.id.img_add_design);
                imgChat = (ImageView) itemView.findViewById(R.id.img_chat_card);
                chat_cardview = (CardView) itemView.findViewById(R.id.chat_list_cardview);
                imgdelete = (ImageView) itemView.findViewById(R.id.img_delete_design);
                chat_frameBackgroung = (LinearLayout) itemView.findViewById(R.id.chat_frame_backgroung_design);
                switch_6A = (Switch) itemView.findViewById(R.id.switch_6A);
                frame_chat_llout = (FrameLayout) itemView.findViewById(R.id.chat_frame_llout);
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View v) {
                super(v);
            }
        }
    }

    /* ######################################### Second Adapter  ############################################### */
    public class CardDashBoardAdapter extends RecyclerView.Adapter<CardDashBoardAdapter.ViewHolder> {

        public CardDashBoardAdapter() {
            super();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_card_design_one_dashboard_promotional_banner, parent, false);
            return new ViewOtherHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            final ViewOtherHolder holder = (ViewOtherHolder) viewHolder;

            holder.imgList.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up));
            //  holder.imgList.setImageBitmap(bitmapDashboardArray.get(position));
            holder.imgList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savePosition = position;
                    isOnImagePressedCheck = true;
                    WhoseAdapter = "DASHBOARD";
                    selectDashboardImage(CardDashBoardAdapter.this);
                }
            });
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.mipmap.ic_launcher)
                    .showImageOnFail(R.mipmap.ic_launcher)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .showImageOnLoading(R.mipmap.ic_launcher).build();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

            imageLoader.displayImage(Utility.DIR_IMAGE_DOWNLOAD_PATH + imageDashboardArray.get(position), holder.imgList, options, new SimpleImageLoadingListener() {
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
                            // progress.setProgress(Math.round(100.0f * i / i1));
                        }
                    }
            );


            holder.imgadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isOnImagePressedCheck = false;
                    savePosition = position + 1;
                    WhoseAdapter = "DASHBOARD";
                    selectDashboardImage(CardDashBoardAdapter.this);
                }
            });

            holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  int currPosition = items.indexOf();
                    if (imageDashboardArray.size() > 1) {
                        imageDashboardArray.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    } else {
                        Snackbar snackbar = Snackbar.make(rootDesignOne, R.string.imagedashboardarray_minimu_notice, Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                        snackbar.show();
                    }
                }
            });

        }


        @Override
        public int getItemCount() {
            return imageDashboardArray.size();
        }


        class ViewOtherHolder extends ViewHolder {
            ImageView imgList, imgadd, imgdelete;

            public ViewOtherHolder(View itemView) {
                super(itemView);
                imgList = (ImageView) itemView.findViewById(R.id.img_list_card);
                imgadd = (ImageView) itemView.findViewById(R.id.img_add_design);
                imgdelete = (ImageView) itemView.findViewById(R.id.img_delete_design);
            }
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View v) {
                super(v);
            }
        }

    }

    ////////   DashBoard - Deals of the days   ////////////

    /* ######################################### Second Adapter  ############################################### */
    public class CardDashBoard_Deals_of_Adapter extends RecyclerView.Adapter<CardDashBoard_Deals_of_Adapter.ViewHolder> {

        public CardDashBoard_Deals_of_Adapter() {
            super();
        }

        @Override
        public ViewOtherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_card_design_one_dashboard_dealsoftheday, parent, false);
            return new ViewOtherHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            final ViewOtherHolder holder = (ViewOtherHolder) viewHolder;

            holder.imgList.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up));
            holder.imgList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isOnImagePressedCheck = true;
                    savePosition = position;
                    WhoseAdapter = "DASHBOARD_DEALSOFTHEDAY";
                    selectDashboard_Dealoftheday_Image(CardDashBoard_Deals_of_Adapter.this);
                }
            });
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.mipmap.ic_launcher)
                    .showImageOnFail(R.mipmap.ic_launcher)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .showImageOnLoading(R.mipmap.ic_launcher).build();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

            imageLoader.displayImage(Utility.DIR_IMAGE_DOWNLOAD_PATH + Adapter_Dashboard_Deals_of_the_day.get(position).getImagename(), holder.imgList, options, new SimpleImageLoadingListener() {
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
                            // progress.setProgress(Math.round(100.0f * i / i1));
                        }
                    }
            );
            if (Adapter_Dashboard_Deals_of_the_day.get(position).getProduct_Name() != null) {
                holder.edtProductname.setText(Adapter_Dashboard_Deals_of_the_day.get(position).getProduct_Name());
            }

            if (Adapter_Dashboard_Deals_of_the_day.get(position).getProduct_Selling_Price() != null) {
                holder.edtSellingPrice.setText(Adapter_Dashboard_Deals_of_the_day.get(position).getProduct_Selling_Price());
            }

            holder.btnSaveDealoftheday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.edtProductname.getText().toString().isEmpty()) {
                        holder.edtProductname.setError(getString(R.string.empty_productname_notice));
                    } else if (holder.edtSellingPrice.getText().toString().isEmpty()) {
                        holder.edtSellingPrice.setError(getString(R.string.empty_sellingprice_notice));
                    } else {
                        Adapter_Dashboard_Deals_of_the_day.get(position).setProduct_Name(holder.edtProductname.getText().toString());
                        Adapter_Dashboard_Deals_of_the_day.get(position).setProduct_Selling_Price(holder.edtSellingPrice.getText().toString());
                        notifyItemChanged(position);
                    }
                }
            });

            holder.imgadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isOnImagePressedCheck = false;
                    savePosition = position + 1;
                    WhoseAdapter = "DASHBOARD_DEALSOFTHEDAY";
                    selectDashboard_Dealoftheday_Image(CardDashBoard_Deals_of_Adapter.this);
                }
            });

            holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  int currPosition = items.indexOf();
                    if (Adapter_Dashboard_Deals_of_the_day.size() > 1) {
                        Adapter_Dashboard_Deals_of_the_day.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    } else {
                        Snackbar snackbar = Snackbar.make(rootDesignOne, R.string.imagedashboardarray_minimu_notice, Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                        snackbar.show();

                    }
                }
            });

        }


        @Override
        public int getItemCount() {
            return Adapter_Dashboard_Deals_of_the_day.size();
        }


        class ViewOtherHolder extends ViewHolder {
            ImageView imgList, imgadd, imgdelete;
            EditText edtProductname, edtSellingPrice;
            Button btnSaveDealoftheday;

            public ViewOtherHolder(View itemView) {
                super(itemView);
                imgList = (ImageView) itemView.findViewById(R.id.img_dealsoftheday_card);
                imgadd = (ImageView) itemView.findViewById(R.id.img_add_design);
                imgdelete = (ImageView) itemView.findViewById(R.id.img_delete_design);
                edtProductname = (EditText) itemView.findViewById(R.id.edt_product_name_dealsofthe_day);
                edtSellingPrice = (EditText) itemView.findViewById(R.id.edt_product_selling_price_dealsofthe_day);
                btnSaveDealoftheday = (Button) itemView.findViewById(R.id.btn_save_dealsoftheday);
            }
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View v) {
                super(v);
            }
        }

    }

    ////////   DashBoard - Discounts for your Dashboard 0C ////////////

    /* ######################################### Second Adapter  ############################################### */
    public class CardDashBoard_Discount_for_you_Adapter extends RecyclerView.Adapter<CardDashBoard_Discount_for_you_Adapter.ViewHolder> {

        public CardDashBoard_Discount_for_you_Adapter() {
            super();
        }

        @Override
        public ViewOtherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_card_design_one_dashboard_discountforyou, parent, false);
            return new ViewOtherHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            final ViewOtherHolder holder = (ViewOtherHolder) viewHolder;

            holder.imgList.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up));
            holder.imgList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isOnImagePressedCheck = true;
                    savePosition = position;
                    WhoseAdapter = "DASHBOARD_DISCOUNTFORYOU";
                    selectDashboard_Discountforyou_Image(CardDashBoard_Discount_for_you_Adapter.this);
                }
            });
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.mipmap.ic_launcher)
                    .showImageOnFail(R.mipmap.ic_launcher)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .showImageOnLoading(R.mipmap.ic_launcher).build();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

            imageLoader.displayImage(Utility.DIR_IMAGE_DOWNLOAD_PATH + Adapter_Dashboard_Descounts_for_you.get(position).getImagename(), holder.imgList, options, new SimpleImageLoadingListener() {
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
                            // progress.setProgress(Math.round(100.0f * i / i1));
                        }
                    }
            );
            if (Adapter_Dashboard_Descounts_for_you.get(position).getProduct_Name() != null) {
                holder.edtProductname.setText(Adapter_Dashboard_Descounts_for_you.get(position).getProduct_Name());
            }

            if (Adapter_Dashboard_Descounts_for_you.get(position).getProduct_Selling_Price() != null) {
                holder.edtSellingPrice.setText(Adapter_Dashboard_Descounts_for_you.get(position).getProduct_Selling_Price());
            }

            holder.btnSaveDealoftheday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.edtProductname.getText().toString().isEmpty()) {
                        holder.edtProductname.setError(getString(R.string.empty_productname_notice));
                    } else if (holder.edtSellingPrice.getText().toString().isEmpty()) {
                        holder.edtSellingPrice.setError(getString(R.string.empty_sellingprice_notice));
                    } else {
                        Adapter_Dashboard_Descounts_for_you.get(position).setProduct_Name(holder.edtProductname.getText().toString());
                        Adapter_Dashboard_Descounts_for_you.get(position).setProduct_Selling_Price(holder.edtSellingPrice.getText().toString());
                        notifyItemChanged(position);
                    }
                }
            });

            holder.imgadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isOnImagePressedCheck = false;
                    savePosition = position + 1;
                    WhoseAdapter = "DASHBOARD_DISCOUNTFORYOU";
                    selectDashboard_Discountforyou_Image(CardDashBoard_Discount_for_you_Adapter.this);
                }
            });

            holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  int currPosition = items.indexOf();
                    if (Adapter_Dashboard_Descounts_for_you.size() > 1) {
                        Adapter_Dashboard_Descounts_for_you.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    } else {
                        Snackbar snackbar = Snackbar.make(rootDesignOne, R.string.imagedashboardarray_minimu_notice, Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                        snackbar.show();
                    }
                }
            });

        }


        @Override
        public int getItemCount() {
            return Adapter_Dashboard_Descounts_for_you.size();
        }


        class ViewOtherHolder extends ViewHolder {
            ImageView imgList, imgadd, imgdelete;
            EditText edtProductname, edtSellingPrice;
            Button btnSaveDealoftheday;

            public ViewOtherHolder(View itemView) {
                super(itemView);
                imgList = (ImageView) itemView.findViewById(R.id.img_discountforyou_card);
                imgadd = (ImageView) itemView.findViewById(R.id.img_add_design);
                imgdelete = (ImageView) itemView.findViewById(R.id.img_delete_design);
                edtProductname = (EditText) itemView.findViewById(R.id.edt_product_name_discontsforyou);
                edtSellingPrice = (EditText) itemView.findViewById(R.id.edt_product_selling_price_discontsforyou);
                btnSaveDealoftheday = (Button) itemView.findViewById(R.id.btn_save_discontsforyou);
            }
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View v) {
                super(v);
            }
        }

    }

    /* ######################################### Dashboard 0D Colorful card Adapter  ############################################### */
    public class CardDashBoard_Colorful_Card_Adapter extends RecyclerView.Adapter<CardDashBoard_Colorful_Card_Adapter.ViewHolder> {

        public CardDashBoard_Colorful_Card_Adapter() {
            super();
        }

        @Override
        public ViewOtherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_card_design_one_dashboard_colorfulcards, parent, false);
            return new ViewOtherHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            final ViewOtherHolder holder = (ViewOtherHolder) viewHolder;

            if (Adapter_Dashboard_ColorfulCards.get(position).getProduct_Name() != null) {
                holder.edtTitle.setText(Adapter_Dashboard_ColorfulCards.get(position).getProduct_Name());
            }

            if (Adapter_Dashboard_ColorfulCards.get(position).getProduct_Selling_Price() != null) {
                holder.edtSubtitle.setText(Adapter_Dashboard_ColorfulCards.get(position).getProduct_Selling_Price());
            }

            holder.btnSaveColorfulcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.edtTitle.getText().toString().isEmpty()) {
                        holder.edtTitle.setError(getString(R.string.empty_titlename_notice));
                    } else if (holder.edtSubtitle.getText().toString().isEmpty()) {
                        holder.edtSubtitle.setError(getString(R.string.empty_subtitle_notice));
                    } else {
                        Adapter_Dashboard_ColorfulCards.get(position).setProduct_Name(holder.edtTitle.getText().toString());
                        Adapter_Dashboard_ColorfulCards.get(position).setProduct_Selling_Price(holder.edtSubtitle.getText().toString());
                        notifyItemChanged(position);
                    }
                }
            });

            holder.imgadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductDetailsAdapter colorfulcardAdapter = new ProductDetailsAdapter();
                    colorfulcardAdapter.setProduct_Name("");
                    colorfulcardAdapter.setProduct_Selling_Price("");

                    Adapter_Dashboard_ColorfulCards.add(position + 1, colorfulcardAdapter);
                    notifyItemInserted(position + 1);
                }
            });

            holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Adapter_Dashboard_ColorfulCards.size() > 1) {
                        Adapter_Dashboard_ColorfulCards.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    } else {
                        Snackbar snackbar = Snackbar.make(rootDesignOne, R.string.card_minimum_notice_adapter, Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                        snackbar.show();
                    }
                }
            });

        }


        @Override
        public int getItemCount() {
            return Adapter_Dashboard_ColorfulCards.size();
        }


        class ViewOtherHolder extends ViewHolder {
            ImageView imgadd, imgdelete;
            EditText edtTitle, edtSubtitle;
            Button btnSaveColorfulcard;

            public ViewOtherHolder(View itemView) {
                super(itemView);
                imgadd = (ImageView) itemView.findViewById(R.id.img_add_design);
                imgdelete = (ImageView) itemView.findViewById(R.id.img_delete_design);
                edtTitle = (EditText) itemView.findViewById(R.id.edt_title_colorful_cards);
                edtSubtitle = (EditText) itemView.findViewById(R.id.edt_subtitle_colorful_cards);
                btnSaveColorfulcard = (Button) itemView.findViewById(R.id.btn_save_colorfulcards);
            }
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View v) {
                super(v);
            }
        }

    }/* ######################################### Dashboard 0D Colorful card Adapter  ############################################### */

    public class CardDocument_Adapter extends RecyclerView.Adapter<CardDocument_Adapter.ViewHolder> {

        public CardDocument_Adapter() {
            super();
        }

        @Override
        public ViewOtherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card_design_one_dashboard_colorfulcards, parent, false);
            return new ViewOtherHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            final ViewOtherHolder holder = (ViewOtherHolder) viewHolder;

            if (Adapter_Documents.get(position).getName() != null) {
                holder.edtTitle.setText(Adapter_Documents.get(position).getName());
            }

            if (Adapter_Documents.get(position).getLink() != null) {
                holder.edtSubtitle.setText(Adapter_Documents.get(position).getLink());
            }

            holder.btnSaveColorfulcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.edtTitle.getText().toString().isEmpty()) {
                        holder.edtTitle.setError(getString(R.string.empty_doc_notice));
                    } else if (holder.edtSubtitle.getText().toString().isEmpty()) {
                        holder.edtSubtitle.setError(getString(R.string.empty_docurl_notice));
                    } else {
                        Adapter_Documents.get(position).setName(holder.edtTitle.getText().toString());
                        Adapter_Documents.get(position).setLink(holder.edtSubtitle.getText().toString());
                        notifyItemChanged(position);
                    }
                }
            });

            holder.imgadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentAdapter documentAdapter = new DocumentAdapter();
                    documentAdapter.setName("");
                    documentAdapter.setLink("");
                    Adapter_Documents.add(position + 1, documentAdapter);
                    notifyItemInserted(position + 1);
                }
            });

            holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  int currPosition = items.indexOf();
                    if (Adapter_Documents.size() > 1) {
                        Adapter_Documents.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    } else {
                        Snackbar snackbar = Snackbar.make(rootDesignOne, R.string.card_minimum_notice_adapter, Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                        snackbar.show();

                    }
                }
            });

        }


        @Override
        public int getItemCount() {
            return Adapter_Documents.size();
        }


        class ViewOtherHolder extends ViewHolder {
            ImageView imgadd, imgdelete;
            EditText edtTitle, edtSubtitle;
            Button btnSaveColorfulcard;

            public ViewOtherHolder(View itemView) {
                super(itemView);
                imgadd = (ImageView) itemView.findViewById(R.id.img_add_design);
                imgdelete = (ImageView) itemView.findViewById(R.id.img_delete_design);
                edtTitle = (EditText) itemView.findViewById(R.id.edt_title_colorful_cards);
                edtSubtitle = (EditText) itemView.findViewById(R.id.edt_subtitle_colorful_cards);
                btnSaveColorfulcard = (Button) itemView.findViewById(R.id.btn_save_colorfulcards);
            }
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View v) {
                super(v);
            }
        }

    }


    ///////////////////////////////////////////////////////////////////////////////
    public class CardPortfolioAdapter extends RecyclerView.Adapter<CardPortfolioAdapter.ViewHolder> {
        public CardPortfolioAdapter() {
            super();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_card_design_one_portfolio, parent, false);
            return new ViewOtherHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            final ViewOtherHolder holder = (ViewOtherHolder) viewHolder;
            holder.imgList.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up));
            holder.imgList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isOnImagePressedCheck = true;
                    savePosition = position;
                    WhoseAdapter = "PORTFOLIO";
                    selectImage(CardPortfolioAdapter.this);
                }
            });
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.mipmap.ic_launcher)
                    .showImageOnFail(R.mipmap.ic_launcher)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .showImageOnLoading(R.mipmap.ic_launcher).build();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

            imageLoader.displayImage(Utility.DIR_IMAGE_DOWNLOAD_PATH + imagePortfolioArray.get(position), holder.imgList, options, new SimpleImageLoadingListener() {
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
                            // progress.setProgress(Math.round(100.0f * i / i1));
                        }
                    }
            );


            holder.imgadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isOnImagePressedCheck = false;
                    savePosition = position + 1;
                    WhoseAdapter = "PORTFOLIO";
                    selectImage(CardPortfolioAdapter.this);

                }
            });

            holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  int currPosition = items.indexOf();

                    if (imagePortfolioArray.size() > 1) {
                        imagePortfolioArray.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    } else {
                        Snackbar snackbar = Snackbar.make(rootDesignOne, R.string.imagedashboardarray_minimu_notice, Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                        snackbar.show();
                    }

                }
            });

        }


        @Override
        public int getItemCount() {
            return imagePortfolioArray.size();
        }


        class ViewOtherHolder extends ViewHolder {
            ImageView imgList, imgadd, imgdelete;

            public ViewOtherHolder(View itemView) {
                super(itemView);
                imgList = (ImageView) itemView.findViewById(R.id.img_list_card);
                imgadd = (ImageView) itemView.findViewById(R.id.img_add_design);
                imgdelete = (ImageView) itemView.findViewById(R.id.img_delete_design);
            }
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View v) {
                super(v);
            }
        }

    }

    //  **************** Call All_App_Details API ******************************//
    AsyncHttpClient callEachPhaseAppDetailsAPIRequest;

    public void CallEachPhaseAppDetailsApi(String UID, String APP_ID, String APP_NAME, String APP_ICON, String SPLASH_ICON, String APP_CATEGORY, String APP_THEME, String THEME_COLOR, String TEXT_COLOR, String PUBLISH_ID, String APP_PAGE, String APP_PAGES_ID) {
        if (callEachPhaseAppDetailsAPIRequest != null) {
            callEachPhaseAppDetailsAPIRequest.cancelRequests(getActivity(), true);
        }
        callEachPhaseAppDetailsAPIRequest = new AsyncHttpClient();
        callEachPhaseAppDetailsAPIRequest.post("http://fadootutorial.com/appgenerator/eachphaseappdetails.php", RequestAppDetailsParams(UID, APP_ID, APP_NAME, APP_ICON, SPLASH_ICON, APP_CATEGORY, APP_THEME, THEME_COLOR, TEXT_COLOR, PUBLISH_ID, APP_PAGE, APP_PAGES_ID), new All_APP_DETAILS_result());
    }

    public RequestParams RequestAppDetailsParams(String UID, String APP_ID, String APP_NAME, String APP_ICON, String SPLASH_ICON, String APP_CATEGORY, String APP_THEME, String THEME_COLOR, String TEXT_COLOR, String PUBLISH_ID, String APP_PAGE, String APP_PAGES_ID) {
        RequestParams params = new RequestParams();
        params.put("UID", UID);
        params.put("APP_ID", APP_ID);
        params.put("APP_NAME", APP_NAME);
        params.put("APP_ICON", APP_ICON);
        params.put("SPLASH_ICON", SPLASH_ICON);
        params.put("APP_CATEGORY", APP_CATEGORY);
        params.put("APP_THEME", APP_THEME);
        params.put("THEME_COLOR", THEME_COLOR);
        params.put("TEXT_COLOR", TEXT_COLOR);
        params.put("PUBLISH_ID", PUBLISH_ID);
        params.put("APP_PAGE", APP_PAGE);
        params.put("APP_PAGES_ID", APP_PAGES_ID);
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
                    if (response.result.equals("success")) {
                      //  Log.e("###", "Successfully data saved");

                        Fragment fragment = new fragment_design_two();
                        if (fragment != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                                // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                                // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.thirdColor));
                                // }
                            }

                            MainActivity.frameToolbar.setBackgroundColor(getResources().getColor(R.color.thirdColor));
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();
                            Snackbar snackbar = Snackbar.make(rootDesignOne, R.string.message_data_saved, Snackbar.LENGTH_SHORT);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                            snackbar.show();

                        } else {
                           // Log.e("Home", "Error in creating fragment");
                        }

                    } else {
                      //  Log.e("###", "Some thing went");
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            common.hideProgressDialog();
            Snackbar snackbar = Snackbar.make(rootDesignOne, R.string.message_please_try_later, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
            snackbar.show();

        }
    }
}


