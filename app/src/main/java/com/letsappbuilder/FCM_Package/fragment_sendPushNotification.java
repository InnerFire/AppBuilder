package com.letsappbuilder.FCM_Package;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.gson.Gson;
import com.letsappbuilder.Fragment.Utility;
import com.letsappbuilder.MainActivity;
import com.letsappbuilder.R;
import com.letsappbuilder.Response.SignUpResponse;
import com.letsappbuilder.Utils.AppPrefs;
import com.letsappbuilder.Utils.Common;
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class fragment_sendPushNotification extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private Button buttonSendPush;
    private RadioGroup radioGroup;
    private Spinner spinner;
    AppPrefs appPrefs;
    Common common;
    private EditText editTextTitle, editTextMessage;
    ImageView imgPushNotification;
    FrameLayout frameImagePush;
    private boolean isSendAllChecked;
    private List<String> devices;
    Bitmap UniversalBitmap;
    private int REQUEST_CAMERA = 112, SELECT_FILE = 115;
    public String imagepath;
    DisplayImageOptions options;
    ImageLoader imageLoader;
    Switch switchImageUpload;
    ScrollView rootPushNotify;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_send_push_notification, container, false);
        rootPushNotify = (ScrollView) v.findViewById(R.id.root_fragment_sendpush_notification);
        appPrefs = new AppPrefs(getActivity());
        common = new Common(this.getActivity());

        MainActivity.secondframeToolbar.setBackgroundColor(getResources().getColor(R.color.firstColor));
        MainActivity.secondframeToolbar.setVisibility(View.VISIBLE);
        MainActivity.animator(getActivity(), MainActivity.secondimgMainNext);
        MainActivity.animator(getActivity(), MainActivity.secondimgMainPrev);
        MainActivity.tv_main_second.setText(R.string.toolbar_notification);

        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        spinner = (Spinner) v.findViewById(R.id.spinnerDevices);
        buttonSendPush = (Button) v.findViewById(R.id.buttonSendPush);
        switchImageUpload = (Switch) v.findViewById(R.id.img_switch_image_upload);

        editTextTitle = (EditText) v.findViewById(R.id.editTextTitle);
        editTextMessage = (EditText) v.findViewById(R.id.editTextMessage);
        imgPushNotification = (ImageView) v.findViewById(R.id.imgPushnotification);
        frameImagePush = (FrameLayout) v.findViewById(R.id.frame_push_image_upload);
        devices = new ArrayList<>();


        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.profile)
                .showImageOnFail(R.drawable.profile)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.profile).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        radioGroup.setOnCheckedChangeListener(this);
        buttonSendPush.setOnClickListener(this);

        loadRegisteredDevices();
        appPrefs.setTEMP_IMAGE("uploads/banner_auto.jpg");
        imageLoader.displayImage(Utility.PROFILE_DIR_IMAGE_DOWNLOAD_PATH + appPrefs.getTEMP_IMAGE(), imgPushNotification, options, new SimpleImageLoadingListener() {
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

        frameImagePush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        switchImageUpload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    frameImagePush.setVisibility(View.VISIBLE);
                } else {
                    frameImagePush.setVisibility(View.GONE);
                }
            }
        });

        return v;
    }


    //method to load all the devices from database
    private void loadRegisteredDevices() {
        common.showProgressDialog(getString(R.string.progress_loading));
        CallLoadRegisterAppUsersApi(appPrefs.getCHAT_APP_ID());
    }

    //this method will send the push
    //from here we will call sendMultiple() or sendSingle() push method
    //depending on the selection
    private void sendPush() {
        if (isSendAllChecked) {
            sendMultiplePush();
        } else {
            sendSinglePush();
        }
    }

    //  ####################   Load Register APP User Api   ###################
    AsyncHttpClient callLoadRegisterAppUserAPIRequest;

    public void CallLoadRegisterAppUsersApi(String appid) {
        if (callLoadRegisterAppUserAPIRequest != null) {
            callLoadRegisterAppUserAPIRequest.cancelRequests(getActivity(), true);
        }
        callLoadRegisterAppUserAPIRequest = new AsyncHttpClient();
        callLoadRegisterAppUserAPIRequest.post(EndPoints.URL_FETCH_DEVICES, RequestLoadAppUsers(appid), new Request_Load_App_Users());
    }

    public RequestParams RequestLoadAppUsers(String appid) {
        RequestParams params = new RequestParams();

        params.put("app_id", appid);

        return params;
    }

    public class Request_Load_App_Users extends AsyncHttpResponseHandler {


        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
            common.hideProgressDialog();
          //  Log.e("$$$", "On Success");
            JSONObject obj = null;
            try {
                String str = new String(responseBody, "UTF-8");

                obj = new JSONObject(str);
              //  Log.e("Hello=>", obj.toString());

                //  if (!obj.getBoolean("ERROR")) {
                JSONArray jsonDevices = obj.getJSONArray("emails");
                if (!jsonDevices.getJSONObject(0).getString("email").equals("ERROR")) {
                    for (int i = 0; i < jsonDevices.length(); i++) {
                        JSONObject d = jsonDevices.getJSONObject(i);
                        devices.add(d.getString("email"));
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_spinner_dropdown_item,
                            devices);

                    spinner.setAdapter(arrayAdapter);
                }
            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
          //  Log.e("###", "Something went wrong");
            common.hideProgressDialog();
        }
    }

    //  ####################   Token Register Api   ###################
    AsyncHttpClient callSendMultiplePushAPIRequest;

    public void CallSendMultiplePushApi(String title, String message, String image, String app_id) {
        if (callSendMultiplePushAPIRequest != null) {
            callSendMultiplePushAPIRequest.cancelRequests(getActivity(), true);
        }
        callSendMultiplePushAPIRequest = new AsyncHttpClient();
        callSendMultiplePushAPIRequest.post(EndPoints.URL_SEND_MULTIPLE_PUSH, RequestSendMultiplePush(title, message, image, app_id), new Request_Send_Multiple_Push());
    }

    public RequestParams RequestSendMultiplePush(String title, String message, String image, String app_id) {
        RequestParams params = new RequestParams();

        params.put("title", title);
        params.put("message", message);
        if (switchImageUpload.isChecked()) {
            params.put("image", image);
        }
        params.put("app_id", app_id);
       // Log.e("%%%", params.toString());
        return params;
    }

    public class Request_Send_Multiple_Push extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
         //   Log.e("$$$", "On Success");
            try {
                String str = new String(responseBody, "UTF-8");
             //   Log.e("***********", "response is" + str);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
          //  Log.e("###", "Something went wrong");
        }
    }

    private void sendMultiplePush() {
        final String title = editTextTitle.getText().toString();
        final String message = editTextMessage.getText().toString();
        String image = appPrefs.getTEMP_IMAGE();
        CallSendMultiplePushApi(title, message, image, appPrefs.getCHAT_APP_ID());
    }

    private void sendSinglePush() {
        final String title = editTextTitle.getText().toString();
        final String message = editTextMessage.getText().toString();
        String image = appPrefs.getTEMP_IMAGE();
        final String email = spinner.getSelectedItem().toString();
        CallSendSinglePushApi(title, message, image, email);
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
                    if (result)
                        cameraIntent();
                } else if (items[item].equals(getString(R.string.camera_dialog_choose_library))) {
                    if (result)
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
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        uploadImage();
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

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
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

    private void uploadImage() {
        final CharSequence[] items = {getString(R.string.upload_dialog_photo), getString(R.string.upload_dialog_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(R.string.upload_dialog_title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity().getApplicationContext());

                if (items[item].equals(getString(R.string.upload_dialog_photo))) {
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    final String saltString = getSaltString();
                    params.put("random_id", saltString);
                    params.put("uploaded_file", getStringImage(UniversalBitmap));

                   // Log.e("!!!", params.toString());
                    client.post("http://fadootutorial.com/appgenerator/pushimageupload.php", params, new AsyncHttpResponseHandler() {
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
                                SignUpResponse response = new Gson().fromJson(str, SignUpResponse.class);
                             //   Log.e("****SignUp*****", "" + response.result);
                                if (response.result.equals("success")) {
                                    imagepath = "uploads/" + saltString + ".png";
                                    appPrefs.setTEMP_IMAGE(imagepath);
                                    imageLoader.displayImage(Utility.PROFILE_DIR_IMAGE_DOWNLOAD_PATH + appPrefs.getTEMP_IMAGE(), imgPushNotification, options, new SimpleImageLoadingListener() {
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

                                } else {
                                    Snackbar snackbar = Snackbar.make(rootPushNotify, R.string.error_upload_picture, Snackbar.LENGTH_SHORT);
                                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.firstColor));
                                    snackbar.show();
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            common.hideProgressDialog();
                            Snackbar snackbar = Snackbar.make(rootPushNotify, R.string.error_something_went_wrong, Snackbar.LENGTH_SHORT);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.firstColor));
                            snackbar.show();
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
                //	Uri selectedImage = data.getData();
                //			Log.e("!!!!!",selectedImage+"");
                uploadImage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    //  ####################   Token Register Api   ###################
    AsyncHttpClient callSendSinglePushAPIRequest;

    public void CallSendSinglePushApi(String title, String message, String image, String email) {
        if (callSendSinglePushAPIRequest != null) {
            callSendSinglePushAPIRequest.cancelRequests(getActivity(), true);
        }
        callSendSinglePushAPIRequest = new AsyncHttpClient();
        callSendSinglePushAPIRequest.post(EndPoints.URL_SEND_SINGLE_PUSH, RequestSendSinglePush(title, message, image, email), new Request_Send_Single_Push());
    }

    public RequestParams RequestSendSinglePush(String title, String message, String image, String email) {
        RequestParams params = new RequestParams();

        params.put("title", title);
        params.put("message", message);
        if (switchImageUpload.isChecked()) {
            params.put("image", image);
        }
        params.put("email", email);
        return params;
    }

    public class Request_Send_Single_Push extends AsyncHttpResponseHandler {


        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
          //  Log.e("$$$", "On Success");
            try {
                String str = new String(responseBody, "UTF-8");
               // Log.e("***********", "response is" + str);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
          //  Log.e("###", "Something went wrong");
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioButtonSendAll:
                isSendAllChecked = true;
                spinner.setVisibility(View.GONE);
                break;

            case R.id.radioButtonSendOne:
                isSendAllChecked = false;
                spinner.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    public void onClick(View view) {
        if (editTextTitle.getText().toString().equals(""))
            editTextTitle.setError("Title should not be empty");
        else if (editTextMessage.getText().toString().equals(""))
            editTextMessage.setError("Mesaage should not be empty");
        else
            sendPush();
    }
}
