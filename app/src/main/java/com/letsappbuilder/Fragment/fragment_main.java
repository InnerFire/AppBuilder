package com.letsappbuilder.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.letsappbuilder.MainActivity;
import com.letsappbuilder.R;
import com.letsappbuilder.Response.FetchAllDetailsResponse;
import com.letsappbuilder.Utils.AppPrefs;
import com.letsappbuilder.Utils.Common;
import com.letsappbuilder.Utils.DbHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import cz.msebera.android.httpclient.Header;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

/**
 * Created by Savaliya Imfotech on 24-05-2016.
 */
public class fragment_main extends Fragment {
    String[] image = {"http://media.appypie.com/appypie-slider-video/images/first.jpg", "http://media.appypie.com/appypie-slider-video/images/church.jpg", "http://media.appypie.com/appypie-slider-video/images/radio.jpg", "http://media.appypie.com/appypie-slider-video/images/restaurant.jpg", "http://media.appypie.com/appypie-slider-video/images/business.jpg", "http://media.appypie.com/appypie-slider-video/images/realEstate.jpg", "http://media.appypie.com/appypie-slider-video/images/marriage.jpg", "http://media.appypie.com/appypie-slider-video/images/work.jpg"};
    DbHelper dbHelper;
    AppPrefs appPrefs;
    Common common;
    PulsatorLayout pulsatorLayout;
    //  **************** Call FETCH_All_APP_DETAILS API ******************************//
    AsyncHttpClient callFetchAppDetailsAPIRequest;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private FloatingActionButton fab_create_new_app;
    private FrameLayout rootFragmentMain;

    public String getSaltString() {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        MainActivity.secondframeToolbar.setVisibility(View.VISIBLE);
        MainActivity.frameToolbar.setVisibility(View.GONE);

        //  Log.e("@@@", "on create view");
        common = new Common(getActivity());
        pulsatorLayout = (PulsatorLayout) v.findViewById(R.id.pulsator);
        pulsatorLayout.start();
        rootFragmentMain = (FrameLayout) v.findViewById(R.id.rootFragmentMain);
        fab_create_new_app = (FloatingActionButton) v.findViewById(R.id.fab_create_apps);
        appPrefs = new AppPrefs(getActivity());
        dbHelper = new DbHelper(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.firstColor));
        }
        MainActivity.secondframeToolbar.setBackgroundColor(getResources().getColor(R.color.firstColor));
        MainActivity.animator(getActivity(), MainActivity.secondimgMainNext);
        MainActivity.animator(getActivity(), MainActivity.secondimgMainPrev);
        MainActivity.tv_main_second.setText(R.string.toolbar_dahsboard);

        fab_create_new_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appPrefs.setIS_NEW_APP("true");
                appPrefs.setAPP_ID(getSaltString());
                //  Log.e("***Main", appPrefs.getAPP_ID());
                Fragment fragment = new fragment_selection_one();
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();

                } else {
                    //  Log.e("Home", "Error in creating fragment");
                }

            }
        });
        MainActivity.secondimgMainPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.drawer.openDrawer(GravityCompat.START);
            }
        });
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
// new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);

        /*recyclerView.setLayoutManager(layoutManager);

        adapter = new CardAdapter(name, image);
        recyclerView.setAdapter(adapter);
*/
        return v;
    }

/*
    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

        List<ListIetem> items;

        public CardAdapter(String[] names, String[] urls) {
            super();
            items = new ArrayList<ListIetem>();
            for (int i = 0; i < names.length; i++) {
                ListIetem item = new ListIetem();
                item.setName(names[i]);
                item.setImagePath(urls[i]);
                items.add(item);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_card_view, parent, false);

            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            ListIetem list = items.get(position);
            // holder.textViewName.setTag(image[position]);
            holder.textViewName.setText(list.getName());
            holder.imgList.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up));
            holder.imgList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                        Log.e("Home", "Error in creating fragment");
                    }
                }
            });
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.mipmap.ic_launcher1)
                    .showImageOnFail(R.mipmap.ic_launcher1)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .showImageOnLoading(R.mipmap.ic_launcher1).build();
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

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textViewName;
            ImageView imgList;

            public ViewHolder(View itemView) {
                super(itemView);
                imgList = (ImageView) itemView.findViewById(R.id.img_list_card);
                textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            }
        }
    }
*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

    }

    public void CallFetchAppDetailsApi(String UID, String APP_ID) {
        if (callFetchAppDetailsAPIRequest != null) {
            callFetchAppDetailsAPIRequest.cancelRequests(getActivity(), true);
        }
        callFetchAppDetailsAPIRequest = new AsyncHttpClient();
        callFetchAppDetailsAPIRequest.post("http://fadootutorial.com/appgenerator/fetchappdetails.php", RequestFetchAppDetailsParams(UID, APP_ID), new FETCH_All_APP_DETAILS_result());
    }

    public RequestParams RequestFetchAppDetailsParams(String UID, String APP_ID) {
        RequestParams params = new RequestParams();
        params.put("UID", UID);
        params.put("APP_ID", APP_ID);
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
                    FetchAllDetailsResponse response = new Gson().fromJson(str, FetchAllDetailsResponse.class);
                    if (!response.APP_ID.equals("ERROR")) {
                        appPrefs.setIS_NEW_APP("false");
                        dbHelper.UpdateFreshDataFromServerToLocal(response.APP_ID, response.APP_NAME, response.APP_ICON, response.SPLASH_ICON, response.APP_CATEGORY, response.APP_THEME, response.THEME_COLOR, response.TEXT_COLOR, response.PUBLISH_ID, response.APP_PAGE, response.APP_PAGES_ID);
                    } else {
                        appPrefs.setIS_NEW_APP("true");
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
                    Fragment fragment = new fragment_selection_one();
                    if (fragment != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                            // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.secondColor));
                            // }
                        }

                        MainActivity.frameToolbar.setBackgroundColor(getResources().getColor(R.color.secondColor));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();

                    } else {
                        // error in creating fragment
                        //  Log.e("Home", "Error in creating fragment");
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            common.hideProgressDialog();
            Snackbar snackbar = Snackbar.make(rootFragmentMain, R.string.message_please_try_later, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.firstColor));
            snackbar.show();
        }
    }


}
