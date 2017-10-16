package com.letsappbuilder.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.letsappbuilder.Adapter.CustomAdapter;
import com.letsappbuilder.Adapter.ListItemSelectionTwo;
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
import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Savaliya Imfotech on 04-12-2016.
 */
public class fragment_design_two extends Fragment {
    List<Integer> listItems;
    Common common;
    DbHelper dbHelper;
    ScrollView rootDesignTwo;
    Bitmap UniversalBitmap;
    LinearLayout llout_appthemecolor, llout_textcolor, root_llout_design_two;
    File destination;
    AppPrefs appPrefs;
    String tempImageView, app_category_design_two, app_icon, splash_icon, app_theme_color, textfamilyselection;
    Spinner spinnerCategory, textFamilyList;
    EditText edtAppnameDesigntwo;
    TextView tvAppthemeColor, tvTextColor;
    DisplayImageOptions options;
    ImageLoader imageLoader;
    String APP_ID, APP_NAME, APP_ICON, SPLASH_ICON, APP_CATEGORY, APP_THEME, THEME_COLOR, TEXT_COLOR, PUBLISH_ID, APP_PAGE, APP_PAGES_ID;
    String[] fontfamilyset = {"sans-serif-smallcaps", "cursive", "casual", "serif-monospace", "monospace", "serif", "sans-serif-condensed", "sans-serif-condensed-light", "sans-serif-black", "sans-serif-medium", "sans-serif", "sans-serif-light", "sans-serif-thin"};
    String[] name = {"Theme 1", "Theme 2", "Theme 3", "Theme 4", "Theme 5", "Theme 6", "Theme 7", "Theme 8", "Theme 9", "Theme 10", "Theme 11", "Theme 12"};
    String[] image = {"http://media.appypie.com/apptheme/business01.png", "http://media.appypie.com/apptheme/business2.png", "http://media.appypie.com/apptheme/business03.jpg", "http://media.appypie.com/apptheme/business04.png", "http://media.appypie.com/apptheme/business05.png", "http://media.appypie.com/apptheme/business06.png", "http://media.appypie.com/apptheme/business_6_1449488024.png", "http://media.appypie.com/apptheme/business08.png", "http://media.appypie.com/apptheme/business09.png", "http://media.appypie.com/apptheme/business10.png", "http://media.appypie.com/apptheme/business11.png", "http://media.appypie.com/apptheme/business12.png"};
    // String[] name = {"Tabbed Activity", "Scrolling Activity", "Navigation Drawer Activity", "Master/Detail Flow", "Login Activity", "Fullscreen Activity", "Basic Activity"};
    // String[] image = {"https://developer.android.com/studio/images/projects/tabbed-activity-template_2-2_2x.png", "https://developer.android.com/studio/images/projects/scrolling-activity-template_2-2_2x.png", "https://developer.android.com/studio/images/projects/navigation-drawer-activity-template_2-2_2x.png", "https://developer.android.com/studio/images/projects/master-detail-flow-template_2-2_2x.png", "https://developer.android.com/studio/images/projects/login-activity-template_2-2_2x.png", "https://developer.android.com/studio/images/projects/fullscreen-activity-template_2-2_2x.png", "https://developer.android.com/studio/images/projects/basic-activity-template_2-2_2x.png"};
    int[] color = {R.color.firstColor, R.color.secondColor, R.color.thirdColor, R.color.fourthColor, R.color.fifthColor, R.color.sixthColor, R.color.firstColor, R.color.secondColor, R.color.thirdColor, R.color.fourthColor, R.color.fifthColor, R.color.sixthColor};
    //  **************** Call Each Phase All_App_Details API ******************************//
    AsyncHttpClient callEachPhaseAppDetailsAPIRequest;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lmanagerDeesignTwo;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageView imgAppIcon, imgSplash, imgUpload, imgUpload2;
    private String userChoosenTask;

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_design_two, container, false);
        MainActivity.frameToolbar.setBackgroundColor(getResources().getColor(R.color.thirdColor));
        MainActivity.frameToolbar.setVisibility(View.VISIBLE);
        MainActivity.secondframeToolbar.setVisibility(View.GONE);

        dbHelper = new DbHelper(getActivity());
        rootDesignTwo = (ScrollView) v.findViewById(R.id.root_fragment_design_two);
        edtAppnameDesigntwo = (EditText) v.findViewById(R.id.edt_appname_design_two);
        spinnerCategory = (Spinner) v.findViewById(R.id.spinner_category_design_two);
        MainActivity.animator(getActivity(), MainActivity.imgMainNext);
        MainActivity.animator(getActivity(), MainActivity.imgMainPrev);
        MainActivity.tv_main.setText(R.string.toolbar_design2);
        common = new Common(getActivity());
        llout_appthemecolor = (LinearLayout) v.findViewById(R.id.apptheme_color);
        root_llout_design_two = (LinearLayout) v.findViewById(R.id.root_llout_design_two);
        llout_textcolor = (LinearLayout) v.findViewById(R.id.text_color);
        appPrefs = new AppPrefs(getActivity());
        tvAppthemeColor = (TextView) v.findViewById(R.id.design_two_apptheme_color);
        tvTextColor = (TextView) v.findViewById(R.id.design_two_text_color);
        imgAppIcon = (ImageView) v.findViewById(R.id.img_app_icon);

        textFamilyList = (Spinner) v.findViewById(R.id.simpleSpinner);
        final List<String> list_textfamily = Arrays.asList(fontfamilyset);

        CustomAdapter customAdapter = new CustomAdapter(getActivity(), fontfamilyset);
        textFamilyList.setAdapter(customAdapter);
        textFamilyList.setSelection(list_textfamily.indexOf(dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.TEXT_COLOR)));
        imgSplash = (ImageView) v.findViewById(R.id.img_splash);
        imgUpload = (ImageView) v.findViewById(R.id.img_app_icon_upload);
        imgUpload2 = (ImageView) v.findViewById(R.id.img_app_icon_upload2);
        listItems = new ArrayList<Integer>();
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        lmanagerDeesignTwo = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(lmanagerDeesignTwo);
        adapter = new CardAdapter(name, image, color);
        recyclerView.setAdapter(adapter);

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        List<String> list = Arrays.asList(fragment_selection_one.name);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, fragment_selection_one.name);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setSelection(list.indexOf(dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.APP_CATEGORY)));
        textfamilyselection = dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.TEXT_COLOR);
        //  Log.e("!!!!", textfamilyselection + "");
        if (dbHelper.isNewApp(appPrefs.getAPP_ID())) {
            app_theme_color = dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.THEME_COLOR);
            tvAppthemeColor.setTextColor(Integer.parseInt(app_theme_color));
            edtAppnameDesigntwo.setText(dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.APP_NAME));
            if (dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.APP_ICON) != null) {
                app_icon = dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.APP_ICON);

                imageLoader.displayImage(Utility.DIR_IMAGE_DOWNLOAD_PATH + dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.APP_ICON), imgAppIcon, options, new SimpleImageLoadingListener() {
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


            }
            if (dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.SPLASH_ICON) != null) {
                splash_icon = dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.SPLASH_ICON);
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).resetViewBeforeLoading(true)
                        .showImageForEmptyUri(R.mipmap.ic_launcher)
                        .showImageOnFail(R.mipmap.ic_launcher)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .showImageOnLoading(R.mipmap.ic_launcher).build();
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

                imageLoader.displayImage(Utility.DIR_IMAGE_DOWNLOAD_PATH + dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.SPLASH_ICON), imgSplash, options, new SimpleImageLoadingListener() {
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
            }
        }
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                app_category_design_two = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        textFamilyList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                textfamilyselection = list_textfamily.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        MainActivity.imgMainNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtAppnameDesigntwo.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(rootDesignTwo, R.string.message_appname_not_empty, Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                    snackbar.show();
                } else {
                    dbHelper.UpdateDesignTwoPhasedata(appPrefs.getAPP_ID(), edtAppnameDesigntwo.getText().toString(), app_category_design_two, app_icon, splash_icon, app_theme_color, textfamilyselection);
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
                            e.printStackTrace();
                        }
                        if (common.isConnected()) {
                            common.showProgressDialog(getString(R.string.progress_updating));
                            CallEachPhaseAppDetailsApi(appPrefs.getUserId(), APP_ID, APP_NAME, APP_ICON, SPLASH_ICON, APP_CATEGORY, APP_THEME, THEME_COLOR, TEXT_COLOR, PUBLISH_ID, APP_PAGE, APP_PAGES_ID);
                        } else {
                            Snackbar snackbar = Snackbar.make(rootDesignTwo, R.string.message_turn_on_internet, Snackbar.LENGTH_SHORT);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                            snackbar.show();
                        }
                    } else {
                        if (common.isConnected()) {
                            Fragment fragment = new fragment_purchase_allappdetails();
                            Bundle bundle = new Bundle();
                            bundle.putString("APP_NAME", dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.APP_NAME));
                            bundle.putString("APP_ICON", Utility.DIR_IMAGE_DOWNLOAD_PATH + dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.APP_ICON));
                            bundle.putString("APP_ID", dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.APP_ID));

                            fragment.setArguments(bundle);

                            if (fragment != null) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                    getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.firstColor));
                                }
                                MainActivity.frameToolbar.setBackgroundColor(getResources().getColor(R.color.firstColor));
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();

                            } else {
                                //  Log.e("Home", "Error in creating fragment");
                            }
                        } else {
                            Snackbar snackbar = Snackbar.make(rootDesignTwo, R.string.message_turn_on_internet, Snackbar.LENGTH_SHORT);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
                            snackbar.show();

                        }
                        //  it is useful for updating database after successful payment  //
                    }
                }
            }
        });

        MainActivity.imgMainPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new fragment_design_one();
                if (fragment != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.thirdColor));
                    }

                    MainActivity.frameToolbar.setBackgroundColor(getResources().getColor(R.color.thirdColor));
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();
                } else {
                    //  Log.e("Home", "Error in creating fragment");
                }

            }
        });

        llout_appthemecolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorChooserDialog dialog = new ColorChooserDialog(getContext());
                dialog.setTitle(R.string.color_dialog_title);
                dialog.setColorListener(new ColorListener() {
                    @Override
                    public void OnColorClick(View v, int color) {
                        //  Log.e("###", color + "");
                        tvAppthemeColor.setTextColor(color);
                        app_theme_color = String.valueOf(color);
                    }
                });
                dialog.show();
            }
        });

        imgAppIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempImageView = "1";
                selectImage();
            }
        });

        imgUpload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempImageView = "2";
                selectImage();
            }
        });
        imgSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempImageView = "2";
                selectImage();
            }
        });
        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempImageView = "1";
                selectImage();
            }
        });

        return v;
    }

    private void selectImage() {
        final CharSequence[] items = {getString(R.string.camera_dialog_take_photo), getString(R.string.camera_dialog_choose_library),
                getString(R.string.camera_dialog_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(R.string.camera_dialog_title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals(getString(R.string.camera_dialog_take_photo))) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals(getString(R.string.camera_dialog_choose_library))) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals(getString(R.string.camera_dialog_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void uploadImage(final String imgString) {
        final CharSequence[] items = {getString(R.string.upload_dialog_photo), getString(R.string.upload_dialog_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(getString(R.string.upload_dialog_title));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity().getApplicationContext());

                if (items[item].equals(getString(R.string.upload_dialog_photo))) {
                    userChoosenTask = "Upload Photo";
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("uploaded_file", getStringImage(UniversalBitmap));
                    params.put("uid", appPrefs.getUserId());
                    params.put("refer_id", appPrefs.getREFERID());
                    if (tempImageView.equals("1")) {
                        params.put("image_type", "app_icon");
                    } else {
                        params.put("image_type", "splash");
                    }
                    final String saltString = getSaltString();
                    params.put("random_id", saltString);

                    // Log.e("!!!", params.toString());
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
                                //  Log.e("****SignUp*****", "" + response.result);

                                if (response.result.equals("success")) {
                                    if (imgString.equals("1")) {
                                        imgAppIcon.setImageBitmap(UniversalBitmap);
                                        app_icon = saltString + ".png";
                                    } else {
                                        imgSplash.setImageBitmap(UniversalBitmap);
                                        splash_icon = saltString + ".png";
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        UniversalBitmap = thumbnail;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if (thumbnail != null) {
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        }

        destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        uploadImage(tempImageView);

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            //  Log.e("!!!!!", destination.getPath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                UniversalBitmap = bm;
                uploadImage(tempImageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals(R.string.camera_dialog_take_photo))
                        cameraIntent();
                    else if (userChoosenTask.equals(R.string.camera_dialog_choose_library))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    public void CallEachPhaseAppDetailsApi(String UID, String APP_ID, String APP_NAME, String APP_ICON, String SPLASH_ICON, String APP_CATEGORY, String APP_THEME, String THEME_COLOR, String TEXT_COLOR, String PUBLISH_ID, String APP_PAGE, String APP_PAGES_ID) {
        if (callEachPhaseAppDetailsAPIRequest != null) {
            callEachPhaseAppDetailsAPIRequest.cancelRequests(getActivity(), true);
        }
        callEachPhaseAppDetailsAPIRequest = new AsyncHttpClient();
        callEachPhaseAppDetailsAPIRequest.post("http://fadootutorial.com/appgenerator/eachphaseappdetails.php", RequestEachPhaseAppDetailsParams(UID, APP_ID, APP_NAME, APP_ICON, SPLASH_ICON, APP_CATEGORY, APP_THEME, THEME_COLOR, TEXT_COLOR, PUBLISH_ID, APP_PAGE, APP_PAGES_ID), new EACH_PHASE_All_APP_DETAILS_result());
    }

    public RequestParams RequestEachPhaseAppDetailsParams(String UID, String APP_ID, String APP_NAME, String APP_ICON, String SPLASH_ICON, String APP_CATEGORY, String APP_THEME, String THEME_COLOR, String TEXT_COLOR, String PUBLISH_ID, String APP_PAGE, String APP_PAGES_ID) {
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

    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

        List<ListItemSelectionTwo> items;

        public CardAdapter(String[] names, String[] image, int[] color) {
            super();
            items = new ArrayList<ListItemSelectionTwo>();
            for (int i = 0; i < names.length; i++) {
                ListItemSelectionTwo item = new ListItemSelectionTwo();
                item.setName(names[i]);
                item.setImagePath(image[i]);
                item.setColor(color[i]);
                items.add(item);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_card_design_two_theme, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            ListItemSelectionTwo list = items.get(position);
            holder.textViewName.setText(list.getName());
            // holder.cardView.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_up));
            //  holder.imgList.setImageResource(image[position]);
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.mipmap.ic_launcher)
                    .showImageOnFail(R.mipmap.ic_launcher)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .showImageOnLoading(R.mipmap.ic_launcher).build();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

            imageLoader.displayImage(image[position], holder.imgList, options, new SimpleImageLoadingListener() {
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

            holder.imgList.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up));
            holder.relativeLayout.setBackgroundResource(color[position]);
            if (dbHelper.isNewApp(appPrefs.getAPP_ID())) {
                if (dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.APP_THEME).equals(String.valueOf(position))) {
                    holder.checkBox_select_two.setChecked(true);
                    listItems.add(position);
                }
            }

            holder.checkBox_select_two.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        listItems.add(position);
                    } else {
                        listItems.remove(listItems.indexOf(position));
                    }
                }
            });
            holder.relativeLayout.setBackgroundResource(color[position]);
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View view) {
                                                             if (holder.checkBox_select_two.isChecked()) {
                                                                 holder.checkBox_select_two.setChecked(false);
                                                                 //  listItems.remove(listItems.indexOf(position));

                                                             } else {
                                                                 holder.checkBox_select_two.setChecked(true);
                                                                 //  listItems.add(position);
                                                             }
                                                         }

                                                     }

            );

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textViewName;
            ImageView imgList;
            RelativeLayout relativeLayout;
            CheckBox checkBox_select_two;

            public ViewHolder(View itemView) {
                super(itemView);
                imgList = (ImageView) itemView.findViewById(R.id.img_list_card);
                textViewName = (TextView) itemView.findViewById(R.id.textViewName);
                relativeLayout = (RelativeLayout) itemView.findViewById(R.id.selection_rrout);
                checkBox_select_two = (CheckBox) itemView.findViewById(R.id.checkBox_selection_two);
            }
        }
    }

    public class EACH_PHASE_All_APP_DETAILS_result extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            common.hideProgressDialog();
            try {
                String str = new String(responseBody, "UTF-8");
                if (str != null) {
                    SignUpResponse response = new Gson().fromJson(str, SignUpResponse.class);
                    if (response.result.equals("success")) {
                        //  Log.e("###", "Successfully data saved");
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
            Snackbar snackbar = Snackbar.make(rootDesignTwo, R.string.message_please_try_later, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.thirdColor));
            snackbar.show();

        }
    }


}
