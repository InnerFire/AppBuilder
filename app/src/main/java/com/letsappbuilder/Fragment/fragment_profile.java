package com.letsappbuilder.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.letsappbuilder.MainActivity;
import com.letsappbuilder.R;
import com.letsappbuilder.Response.SignUpResponse;
import com.letsappbuilder.Utils.AppPrefs;
import com.letsappbuilder.Utils.Common;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class fragment_profile extends Fragment {

    TextView txtChangepassword;
    ImageView imgBackCover, imgProfile;
    EditText email, fullname;
    AppPrefs prefs;
    Common common;
    Dialog dialog;
    Bitmap UniversalBitmap;
    private int REQUEST_CAMERA = 112, SELECT_FILE = 115;
    String imagepath;
    DisplayImageOptions options;
    ImageLoader imageLoader;
    ScrollView rootProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        MainActivity.frameToolbar.setVisibility(View.GONE);
        MainActivity.secondframeToolbar.setVisibility(View.VISIBLE);
        MainActivity.secondframeToolbar.setBackgroundColor(getResources().getColor(R.color.sixthColor));
        MainActivity.animator(getActivity(), MainActivity.secondimgMainNext);
        MainActivity.animator(getActivity(), MainActivity.secondimgMainPrev);
        MainActivity.tv_main_second.setText(R.string.toolbar_profile);

        init(v);
        clicklistner();
        return v;
    }

    public void init(View v) {
        rootProfile = (ScrollView) v.findViewById(R.id.rootProfile);
        rootProfile = (ScrollView) v.findViewById(R.id.rootProfile);
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .displayer(new RoundedBitmapDisplayer(120))
                .showImageForEmptyUri(R.drawable.profile)
                .showImageOnFail(R.drawable.profile)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.profile).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        dialog = new Dialog(getActivity());
        txtChangepassword = (TextView) v.findViewById(R.id.txtChangePassword);
        imgBackCover = (ImageView) v.findViewById(R.id.img_profile_bgcover);
        imgProfile = (ImageView) v.findViewById(R.id.img_profile);
        email = (EditText) v.findViewById(R.id.edt_profile_email);
        fullname = (EditText) v.findViewById(R.id.edt_profile_name);
        prefs = new AppPrefs(getActivity());
        common = new Common(this.getActivity());
        email.setText(prefs.getMAIL());
        fullname.setText(prefs.getNAME());
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
                    // userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals(getString(R.string.camera_dialog_choose_library))) {
                    //   userChoosenTask = "Choose from Library";
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
            Log.e("!!!!!", destination.getPath());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        String saltStr = salt.toString();
        return saltStr;

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
                    //  userChoosenTask = "Upload Photo";
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("uid", prefs.getUserId());
                    params.put("email", prefs.getMAIL());
                    final String saltString = getSaltString();
                    params.put("random_id", saltString);
                    params.put("uploaded_file", getStringImage(UniversalBitmap));

                  //  Log.e("!!!", params.toString());
                    client.post("http://fadootutorial.com/appgenerator/updateprofileicon.php", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            common.showProgressDialog(getString(R.string.progress_uploading));
                           // Log.e("$$$", "start");
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            common.hideProgressDialog();
                            String str = null;
                            try {
                                str = new String(responseBody, "UTF-8");
                                SignUpResponse response = new Gson().fromJson(str, SignUpResponse.class);
                              //  Log.e("****SignUp*****", "" + response.result);
                                if (response.result.equals("success")) {
                                    prefs.setPROFILEPICTURE("uploads/" + saltString + ".png");
                                    imageLoader.displayImage(Utility.PROFILE_DIR_IMAGE_DOWNLOAD_PATH + prefs.getPROFILEPICTURE(), imgProfile, options, new SimpleImageLoadingListener() {
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
                                    Snackbar snackbar = Snackbar.make(rootProfile, R.string.message_image_upload_error, Snackbar.LENGTH_LONG);
                                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.sixthColor));
                                    snackbar.show();

                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            common.hideProgressDialog();
                            Snackbar snackbar = Snackbar.make(rootProfile, R.string.message_something_went_wrong, Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.sixthColor));
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
                uploadImage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void clicklistner() {

        if (!prefs.getPROFILEPICTURE().isEmpty()) {
            imagepath = Utility.PROFILE_DIR_IMAGE_DOWNLOAD_PATH + prefs.getPROFILEPICTURE();
        } else {
            imagepath = Utility.PROFILE_DIR_IMAGE_DOWNLOAD_PATH;
        }
        imageLoader.displayImage(imagepath, imgProfile, options, new SimpleImageLoadingListener() {
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
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        imgBackCover.setImageResource(R.drawable.nav_bgimage);

        txtChangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.change_password_dialog);
                dialog.setCancelable(true);
                final EditText Password = (EditText) dialog.findViewById(R.id.edt_ChangePassword);
                final EditText ConfirmPassword = (EditText) dialog.findViewById(R.id.edt_Confirm_Changepassword);
                Button btn = (Button) dialog.findViewById(R.id.bt_go);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Password.getText().toString().trim().equals("")) {
                            Password.setError(getString(R.string.new_password));
                        } else if (ConfirmPassword.getText().toString().trim().equals("")) {
                            ConfirmPassword.setError(getString(R.string.retype_password));
                        } else if (!Password.getText().toString().trim().equals(ConfirmPassword.getText().toString().trim())) {
                            Snackbar snackbar = Snackbar.make(rootProfile, R.string.password_not_match, Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.sixthColor));
                            snackbar.show();
                        } else {
                            // ************************* Calling Change Password API   *****************
                            if (common.isConnected()) {
                                common.showProgressDialog(getString(R.string.progress_updating));
                                CallChangePasswordAPI(prefs.getUserId(), prefs.getMAIL(), Password.getText().toString());

                            } else {
                                Snackbar snackbar = Snackbar.make(rootProfile, R.string.message_turn_on_internet, Snackbar.LENGTH_LONG);
                                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.sixthColor));
                                snackbar.show();
                            }
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    /* **************  Call Change Password API   *************************  */

    AsyncHttpClient callChangePasswordAPIRequest;

    public void CallChangePasswordAPI(String uid, String email, String password) {
        if (callChangePasswordAPIRequest != null) {
            callChangePasswordAPIRequest.cancelRequests(getActivity(), true);
        }
        callChangePasswordAPIRequest = new AsyncHttpClient();
        callChangePasswordAPIRequest.post("http://fadootutorial.com/appgenerator/changepassword.php", RequestChangePassword(uid, email, password), new Change_Password_Result());
    }

    public RequestParams RequestChangePassword(String uid, String email, String password) {
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("email", email);
        params.put("password", password);
        return params;
    }


    public class Change_Password_Result extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            common.hideProgressDialog();
            try {
                String str = new String(responseBody, "UTF-8");

                if (str != null) {
                    SignUpResponse response = new Gson().fromJson(str, SignUpResponse.class);
                    if (response.result.equals("success")) {
                        Snackbar snackbar = Snackbar.make(rootProfile, R.string.message_new_password_updated, Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.sixthColor));
                        snackbar.show();
                        dialog.cancel();
                    } else {
                        Snackbar snackbar = Snackbar.make(rootProfile, R.string.message_please_try_later, Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.sixthColor));
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
            Snackbar snackbar = Snackbar.make(rootProfile, R.string.message_something_went_wrong, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.sixthColor));
            snackbar.show();
        }
    }


}
