package com.letsappbuilder.Fragment;

import android.content.DialogInterface;
import android.graphics.Bitmap;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.letsappbuilder.FCM_Package.fragment_sendPushNotification;
import com.letsappbuilder.MainActivity;
import com.letsappbuilder.R;
import com.letsappbuilder.Response.MyAppResponse;
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

/**
 * Created by Savaliya Imfotech on 14-12-2016.
 */
public class fragment_myapps_push_notification extends Fragment {
    DbHelper dbHelper;
    TextView tvErrorMsg;
    AppPrefs appPrefs;
    Common common;
    int[] color = {R.color.firstColor, R.color.secondColor, R.color.thirdColor, R.color.fourthColor, R.color.fifthColor, R.color.sixthColor};
    //  *********************** Call MY_All_APP_DETAILS API ******************************//
    AsyncHttpClient callMYAppDetailsAPIRequest;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private FrameLayout rootFragmetnMyApps;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_apps, container, false);
        MainActivity.frameToolbar.setVisibility(View.GONE);
        MainActivity.secondframeToolbar.setVisibility(View.VISIBLE);

        rootFragmetnMyApps = (FrameLayout) v.findViewById(R.id.root_my_apps);
        dbHelper = new DbHelper(getActivity());
        common = new Common(getActivity());
        MainActivity.secondframeToolbar.setBackgroundColor(getResources().getColor(R.color.fourthColor));
        MainActivity.animator(getActivity(), MainActivity.secondimgMainNext);
        MainActivity.animator(getActivity(), MainActivity.secondimgMainPrev);
        MainActivity.tv_main_second.setText(R.string.toolbar_notification);

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
            Snackbar snackbar = Snackbar.make(rootFragmetnMyApps, R.string.message_turn_on_internet, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.fourthColor));
            snackbar.show();
        }
        return v;
    }

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

    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

        ArrayList<MyAppResponse.MyAppList> items;

        public CardAdapter(ArrayList<MyAppResponse.MyAppList> my_app_list) {
            super();
            items = my_app_list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_card_myapp_pushnotification, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            // holder.textViewName.setTag(image[position]);
            holder.tvappname.setText(items.get(position).APP_NAME);
            holder.tvappcategory.setText(items.get(position).APP_CATEGORY);
            holder.tvapppublishdate.setText(items.get(position).SENTANT);
            holder.imgappicon.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.color_item));
            holder.frameLayoutDraftapp.setTag(items.get(position).APP_ID);
            holder.frameLayoutDraftapp.setBackgroundResource(color[position % 6]);
            if (items.get(position).PUBLISH_ID.equals("0"))
                holder.imgnotification.setImageResource(R.drawable.expired);
            else
                holder.imgnotification.setImageResource(R.drawable.notification);

            holder.frameLayoutDraftapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (items.get(position).PUBLISH_ID.toString().trim().equals("0")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(R.string.upgrade_dialog_title);
                        builder.setMessage(R.string.myapps_left_concat + items.get(position).APP_NAME + R.string.myapps_right_concat);

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
                                            Snackbar snackbar = Snackbar.make(rootFragmetnMyApps, R.string.message_turn_on_internet, Snackbar.LENGTH_LONG);
                                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.fourthColor));
                                            snackbar.show();
                                        }
                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.setCancelable(false);
                        dialog.show();
                    } else {
                        appPrefs.setCHAT_APP_ID(items.get(position).APP_ID);
                        //  Log.e("@@@", appPrefs.getCHAT_APP_ID());
                        Fragment fragment = new fragment_sendPushNotification();
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
                            //   Log.e("Home", "Error in creating fragment");
                        }
                    }
                }
            });

            holder.imgappicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new fragment_chat();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();
                    } else {
                        //  Log.e("Home", "Error in creating fragment");
                    }
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
                            // progress.setProgress(Math.round(100.0f * i / i1));
                        }
                    }
            );

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvappname, tvappcategory, tvapppublishdate;
            ImageView imgappicon, imgBackCover, imgnotification;
            FrameLayout frameLayoutDraftapp;

            public ViewHolder(View itemView) {
                super(itemView);
                tvappname = (TextView) itemView.findViewById(R.id.tv_app_name);
                tvappcategory = (TextView) itemView.findViewById(R.id.tv_app_category);
                tvapppublishdate = (TextView) itemView.findViewById(R.id.tv_app_published_date);
                imgappicon = (ImageView) itemView.findViewById(R.id.img_app_icon);
                imgBackCover = (ImageView) itemView.findViewById(R.id.img_app_backcover);
                frameLayoutDraftapp = (FrameLayout) itemView.findViewById(R.id.framellout_draft_apps);
                imgnotification = (ImageView) itemView.findViewById(R.id.notification_icon);
            }
        }
    }

    public class FETCH_All_APP_DETAILS_result extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            common.hideProgressDialog();
            try {
                String str = new String(responseBody, "UTF-8");
                // Log.e("$$$", str);
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
            Snackbar snackbar = Snackbar.make(rootFragmetnMyApps, R.string.message_please_try_later, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.fourthColor));
            snackbar.show();
        }
    }


}
