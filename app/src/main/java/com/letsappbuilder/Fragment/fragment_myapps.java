package com.letsappbuilder.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.letsappbuilder.MainActivity;
import com.letsappbuilder.R;
import com.letsappbuilder.Response.FetchAllDetailsResponse;
import com.letsappbuilder.Response.MyAppResponse;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.letsappbuilder.R.id.img_app_backcover;

/**
 * Created by Savaliya Imfotech on 14-12-2016.
 */
public class fragment_myapps extends Fragment {
    DbHelper dbHelper;
    TextView tvErrorMsg;
    AppPrefs appPrefs;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    public RecyclerView.Adapter adapter;
    public ArrayList<MyAppResponse.MyAppList> items;
    private FrameLayout rootFragmetnMyApps;
    String TEMP_APPID;
    int POSITION;
    Common common;
    int[] color = {R.color.firstColor, R.color.secondColor, R.color.thirdColor, R.color.fourthColor, R.color.fifthColor, R.color.sixthColor};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_apps, container, false);
        rootFragmetnMyApps = (FrameLayout) v.findViewById(R.id.root_my_apps);
        dbHelper = new DbHelper(getActivity());
        common = new Common(this.getActivity());
        MainActivity.frameToolbar.setVisibility(View.GONE);
        MainActivity.secondframeToolbar.setVisibility(View.VISIBLE);

        MainActivity.secondframeToolbar.setBackgroundColor(getResources().getColor(R.color.fourthColor));
        MainActivity.animator(getActivity(), MainActivity.secondimgMainNext);
        MainActivity.animator(getActivity(), MainActivity.secondimgMainPrev);
        MainActivity.tv_main_second.setText(R.string.toolbar_myapps);

        appPrefs = new AppPrefs(getContext());
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        tvErrorMsg = (TextView) v.findViewById(R.id.tv_draft_apps);
        if (common.isConnected()) {
            common.showProgressDialog(getString(R.string.progress_getting));
            CallMYFetchAppDetailsApi(appPrefs.getUserId());
        } else {
            Snackbar snackbar = Snackbar.make(rootFragmetnMyApps, R.string.message_turn_on_internet, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.fourthColor));
            snackbar.show();
        }

        return v;
    }


    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {


        public CardAdapter(ArrayList<MyAppResponse.MyAppList> my_app_list) {
            super();
            items = my_app_list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_card_myapp, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            // holder.textViewName.setTag(image[position]);
            holder.tvappname.setText(items.get(position).APP_NAME);
            holder.tvappcategory.setText(items.get(position).APP_CATEGORY);
            holder.tvapppublishdate.setText(getString(R.string.last_update_concate) + items.get(position).SENTANT);

            holder.imgappicon.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.color_item));
            holder.frameLayoutDraftapp.setTag(items.get(position).APP_ID);
            holder.frameLayoutDraftapp.setBackgroundResource(color[position % 6]);
            if (items.get(position).PUBLISH_ID.equals("0"))
                holder.imgedticon.setImageResource(R.drawable.no_edit);
            else
                holder.imgedticon.setImageResource(R.drawable.edit);

            if (!items.get(position).PUBLISH_ID.toString().trim().equals("0")) {
                holder.tvreferid.setText(getString(R.string.referid_concate) + items.get(position).PUBLISH_ID);
            }
            holder.frameLayoutDraftapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (items.get(position).PUBLISH_ID.toString().trim().equals("0")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(R.string.upgrade_dialog_title);
                        builder.setMessage(getString(R.string.myapps_left_concat) + items.get(position).APP_NAME + getString(R.string.myapps_right_concat));

                        String positiveText = getString(android.R.string.ok);
                        builder.setPositiveButton(positiveText,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (common.isConnected()) {
                                            Fragment fragment = new fragment_purchase();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("APP_NAME", items.get(position).APP_NAME);
                                            bundle.putString("APP_ICON", Utility.DIR_IMAGE_DOWNLOAD_PATH + items.get(position).APP_ICON);
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
                                                // error in creating fragment
                                                //  Log.e("Home", "Error in creating fragment");
                                            }
                                        } else {
                                            Snackbar snackbar = Snackbar.make(rootFragmetnMyApps, R.string.message_turn_on_internet, Snackbar.LENGTH_SHORT);
                                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.fourthColor));
                                            snackbar.show();
                                        }
                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.setCancelable(false);
                        dialog.show();
                    } else {
                        navigate_to_fragment(holder.frameLayoutDraftapp.getTag().toString());
                    }
                }
            });

            holder.imgshareicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.myapp_share_left_concat) + items.get(position).APP_NAME + getString(R.string.myapp_share_right_concat) + items.get(position).PUBLISH_ID);
                    startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_intent_title)));

                }
            });

            holder.bt_install.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/jXuS77?appname=" + items.get(position).APP_NAME.trim())));
                }
            });
            holder.imgdeleteicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.delete_dialog_title);
                    builder.setMessage(R.string.delete_dialog_message);

                    String positiveText = getString(android.R.string.ok);
                    builder.setPositiveButton(positiveText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // common.showProgressDialog("Deleting...");
                                    TEMP_APPID = items.get(position).APP_ID;
                                    POSITION = position;
                                    if (common.isConnected())
                                        CallDeleteAppDetailsApi(items.get(position).APP_ID);
                                    else {
                                        Snackbar snackbar = Snackbar.make(rootFragmetnMyApps, R.string.message_turn_on_internet, Snackbar.LENGTH_SHORT);
                                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.fourthColor));
                                        snackbar.show();
                                    }
                                }
                            });

                    String negativeText = getString(android.R.string.cancel);
                    builder.setNegativeButton(negativeText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false);
                    dialog.show();

                }
            });
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
            imageLoader.displayImage(Common.imageBackgroundURL[position % Common.imageBackgroundURL.length], holder.imgBackCover, options, new SimpleImageLoadingListener() {
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
            imageLoader.loadImage(Common.imageBackgroundURL[position % Common.imageBackgroundURL.length], new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    try {
                        BitmapDrawable drawableBitmap = new BitmapDrawable(loadedImage);
                        holder.imgBackCover.setBackgroundDrawable(drawableBitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            imageLoader.displayImage(Utility.DIR_IMAGE_DOWNLOAD_PATH + items.get(position).APP_ICON, holder.imgappicon, options, new SimpleImageLoadingListener() {
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

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvappname, tvappcategory, tvapppublishdate, tvreferid;
            ImageView imgappicon, imgdeleteicon, imgBackCover, imgedticon, imgshareicon;
            FrameLayout frameLayoutDraftapp;
            Button bt_install;

            public ViewHolder(View itemView) {
                super(itemView);
                tvappname = (TextView) itemView.findViewById(R.id.tv_app_name);
                tvappcategory = (TextView) itemView.findViewById(R.id.tv_app_category);
                tvapppublishdate = (TextView) itemView.findViewById(R.id.tv_app_published_date);
                imgappicon = (ImageView) itemView.findViewById(R.id.img_app_icon);
                imgdeleteicon = (ImageView) itemView.findViewById(R.id.img_delete_myapp);
                frameLayoutDraftapp = (FrameLayout) itemView.findViewById(R.id.framellout_draft_apps);
                imgBackCover = (ImageView) itemView.findViewById(img_app_backcover);
                imgedticon = (ImageView) itemView.findViewById(R.id.img_edt_myapp);
                tvreferid = (TextView) itemView.findViewById(R.id.tv_refer_id);
                imgshareicon = (ImageView) itemView.findViewById(R.id.img_share_myapp);
                bt_install = (Button) itemView.findViewById(R.id.bt_install);
            }
        }
    }

    public void navigate_to_fragment(String app_id) {
        appPrefs.setIS_NEW_APP("false");
        appPrefs.setAPP_ID(app_id);
        if (common.isConnected()) {
            common.showProgressDialog(getString(R.string.progress_loading));
            CallSecondFetchAppDetailsApi(appPrefs.getUserId(), app_id);
        } else {
            Snackbar snackbar = Snackbar.make(rootFragmetnMyApps, R.string.message_turn_on_internet, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.fourthColor));
            snackbar.show();
        }
    }

    //  **************** Call DELETE MY APP API ******************************//
    AsyncHttpClient callDeleteMyAppDetailsAPIRequest;

    public void CallDeleteAppDetailsApi(String APP_ID) {
        if (callDeleteMyAppDetailsAPIRequest != null) {
            callDeleteMyAppDetailsAPIRequest.cancelRequests(getActivity(), true);
        }
        callDeleteMyAppDetailsAPIRequest = new AsyncHttpClient();
        callDeleteMyAppDetailsAPIRequest.post("http://fadootutorial.com/appgenerator/deletemyapp.php", RequestDeletetailsParams(APP_ID), new DELETE_MY_APP_DETAILS_result());
    }

    public RequestParams RequestDeletetailsParams(String APP_ID) {
        RequestParams params = new RequestParams();
        params.put("APP_ID", APP_ID);
        return params;
    }

    public class DELETE_MY_APP_DETAILS_result extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                String str = new String(responseBody, "UTF-8");
                // Log.e("$$$", str);
                SignUpResponse response = new Gson().fromJson(str, SignUpResponse.class);
                // Log.e("****SignUp*****", "" + response.result);

                if (response.result.equals("success")) {
                    dbHelper.DeleteFromMyApps(TEMP_APPID.trim());
                    items.remove(POSITION);
                    adapter.notifyItemRemoved(POSITION);
                    adapter.notifyDataSetChanged();
                    Snackbar snackbar = Snackbar.make(rootFragmetnMyApps, R.string.message_app_success_delete, Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.fourthColor));
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(rootFragmetnMyApps, R.string.message_please_try_later, Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.fourthColor));
                    snackbar.show();
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Snackbar snackbar = Snackbar.make(rootFragmetnMyApps, R.string.message_something_went_wrong, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.fourthColor));
            snackbar.show();

        }
    }

    //  **************** Call FETCH_All_APP_DETAILS API ******************************//
    AsyncHttpClient callSecondFetchAppDetailsAPIRequest;

    public void CallSecondFetchAppDetailsApi(String UID, String APP_ID) {
        if (callSecondFetchAppDetailsAPIRequest != null) {
            callSecondFetchAppDetailsAPIRequest.cancelRequests(getActivity(), true);
        }
        callSecondFetchAppDetailsAPIRequest = new AsyncHttpClient();
        callSecondFetchAppDetailsAPIRequest.post("http://fadootutorial.com/appgenerator/fetchappdetails.php", RequestFetchAppDetailsParams(UID, APP_ID), new SECOND_FETCH_All_APP_DETAILS_result());
    }

    public RequestParams RequestFetchAppDetailsParams(String UID, String APP_ID) {
        RequestParams params = new RequestParams();
        params.put("UID", UID);
        params.put("APP_ID", APP_ID);
        return params;
    }

    public class SECOND_FETCH_All_APP_DETAILS_result extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            common.hideProgressDialog();
            try {
                String str = new String(responseBody, "UTF-8");
                //  Log.e("$$$", str);
                if (str != null) {
                    FetchAllDetailsResponse response = new Gson().fromJson(str, FetchAllDetailsResponse.class);
                    if (!response.APP_ID.equals("ERROR")) {
                        appPrefs.setIS_NEW_APP("false");
                        appPrefs.setAPP_ID(response.APP_ID);
                        //  Log.e("^^^", response.APP_ID);
                        if (dbHelper.isNewApp(response.APP_ID)) {
                            dbHelper.UpdateFreshDataFromServerToLocal(response.APP_ID, response.APP_NAME, response.APP_ICON, response.SPLASH_ICON, response.APP_CATEGORY, response.APP_THEME, response.THEME_COLOR, response.TEXT_COLOR, response.PUBLISH_ID, response.APP_PAGE, response.APP_PAGES_ID);
                        } else {
                            dbHelper.InsertFreshDataFromServerToLocal(response.APP_ID, response.APP_NAME, response.APP_ICON, response.SPLASH_ICON, response.APP_CATEGORY, response.APP_THEME, response.THEME_COLOR, response.TEXT_COLOR, response.PUBLISH_ID, response.APP_PAGE, response.APP_PAGES_ID);
                        }
                        Fragment fragment = new fragment_selection_one();
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

                    } else {
                        appPrefs.setIS_NEW_APP("true");
                        Snackbar snackbar = Snackbar.make(rootFragmetnMyApps, R.string.message_something_went_wrong, Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.fourthColor));
                        snackbar.show();

                    }
/*
                    try {
                        JSONObject jsonobject = new JSONObject(response.APP_PAGE);
                        JSONObject jsonmidObject = jsonobject.getJSONObject("APP_PAGES");
                        JSONObject jsoninObject = jsonmidObject.getJSONObject("0A");
                        JSONArray jarray = jsoninObject.getJSONArray("0A");
                        Log.e("@@@", jarray.length() + "");
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jb = (JSONObject) jarray.get(i);
                            String url = jb.getString("x");
                            Log.e("%%%", url);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
*/
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            common.hideProgressDialog();
            Snackbar snackbar = Snackbar.make(rootFragmetnMyApps, R.string.message_please_try_later, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.fourthColor));
            snackbar.show();
        }
    }


    //  **************** Call MY_All_APP_DETAILS API ******************************//
    AsyncHttpClient callMYAppDetailsAPIRequest;

    public void CallMYFetchAppDetailsApi(String UID) {
        if (callMYAppDetailsAPIRequest != null) {
            callMYAppDetailsAPIRequest.cancelRequests(getActivity(), true);
        }
        callMYAppDetailsAPIRequest = new AsyncHttpClient();
        callMYAppDetailsAPIRequest.post("http://fadootutorial.com/appgenerator/myappslist.php", RequestFetchAppDetailsParams(UID), new FETCH_All_APP_DETAILS_result());
    }

    public RequestParams RequestFetchAppDetailsParams(String UID) {
        RequestParams params = new RequestParams();
        params.put("UID", UID);
        return params;
    }

    public class FETCH_All_APP_DETAILS_result extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            common.hideProgressDialog();
            try {
                String str = new String(responseBody, "UTF-8");
                //  Log.e("$$$", str);
                if (str != null) {
                    MyAppResponse response = new Gson().fromJson(str, MyAppResponse.class);
                    if (!response.myAppLists.get(0).APP_ID.equals("ERROR")) {
                        tvErrorMsg.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter = new CardAdapter(response.myAppLists);
                        recyclerView.setAdapter(adapter);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            common.hideProgressDialog();
            Snackbar snackbar = Snackbar.make(rootFragmetnMyApps, R.string.message_please_try_later, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.fourthColor));
            snackbar.show();
        }
    }

}
