package com.letsappbuilder.Fragment;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Savaliya Imfotech on 24-05-2016.
 */
public class fragment_selection_two extends Fragment {
    static List<ListItemSelectionTwo> items;
    public String IMAGE_THEME_PATH = "http://fadootutorial.com/Theme/Theme";
    String[] name = {"11", "12", "13", "21", "22", "31", "41", "51", "61", "71"};
    String[] image = {"http://media.appypie.com/apptheme/business01.png", "http://media.appypie.com/apptheme/business2.png", "http://media.appypie.com/apptheme/business03.jpg", "http://media.appypie.com/apptheme/business04.png", "http://media.appypie.com/apptheme/business05.png", "http://media.appypie.com/apptheme/business06.png", "http://media.appypie.com/apptheme/business_6_1449488024.png", "http://media.appypie.com/apptheme/business08.png", "http://media.appypie.com/apptheme/business09.png", "http://media.appypie.com/apptheme/business10.png", "http://media.appypie.com/apptheme/business11.png", "http://media.appypie.com/apptheme/business12.png"};
    int[] color = {R.color.firstColor, R.color.secondColor, R.color.thirdColor, R.color.fourthColor, R.color.fifthColor, R.color.sixthColor, R.color.firstColor, R.color.secondColor, R.color.thirdColor, R.color.fourthColor, R.color.fifthColor, R.color.sixthColor};
    DbHelper dbHelper;
    String apptheme;
    Common common;
    AppPrefs appPrefs;
    TextView tv_status_selection_two;
    String APP_ID, APP_NAME, APP_ICON, SPLASH_ICON, APP_CATEGORY, APP_THEME, THEME_COLOR, TEXT_COLOR, PUBLISH_ID, APP_PAGE, APP_PAGES_ID;
    LinearLayout rootSelectionTwo;
    //  **************** Call Each Phase All_App_Details API ******************************//
    AsyncHttpClient callEachPhaseAppDetailsAPIRequest;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selection_two, container, false);
        rootSelectionTwo = (LinearLayout) v.findViewById(R.id.root_fragment_selection_two);
        MainActivity.frameToolbar.setVisibility(View.VISIBLE);
        MainActivity.secondframeToolbar.setVisibility(View.GONE);
        MainActivity.frameToolbar.setBackgroundColor(getResources().getColor(R.color.secondColor));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.secondColor));
        }

        dbHelper = new DbHelper(getActivity());
        common = new Common(getActivity());
        tv_status_selection_two = (TextView) v.findViewById(R.id.tv_status_selection_two);
        appPrefs = new AppPrefs(getActivity());
        MainActivity.animator(getActivity(), MainActivity.imgMainNext);
        MainActivity.animator(getActivity(), MainActivity.imgMainPrev);
        MainActivity.tv_main.setText(R.string.toolbar_selection2);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerSelectionTwo);
        recyclerView.setHasFixedSize(true);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, 0);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        adapter = new CardAdapter(name, image, color);
        recyclerView.setAdapter(adapter);

        if (dbHelper.isNewApp(appPrefs.getAPP_ID())) {
            List list = Arrays.asList(name);
            tv_status_selection_two.setVisibility(View.VISIBLE);
            tv_status_selection_two.setText(dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.APP_THEME) + getString(R.string.category_selectiontwo_message));
            apptheme = dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.APP_THEME);
            recyclerView.scrollToPosition(list.indexOf(apptheme.trim()));
        }

        MainActivity.imgMainPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

            }
        });

        MainActivity.imgMainNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (apptheme != null) {
                    if (dbHelper.isNewApp(appPrefs.getAPP_ID())) {
                        dbHelper.UpdateSelectionTwoPhasedata(appPrefs.getAPP_ID(), apptheme);
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
                                Snackbar snackbar = Snackbar.make(rootSelectionTwo, R.string.message_turn_on_internet, Snackbar.LENGTH_LONG);
                                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.secondColor));
                                snackbar.show();

                            }

                        } else {
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
                    } else {
                        dbHelper.insertSelectionPhasedata(appPrefs.getAPP_ID(), getArguments().getString("APP_NAME"), getArguments().getString("APP_CATEGORY"), apptheme, getArguments().getString("POSITION"));
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
                } else {
                    Snackbar snackbar = Snackbar.make(rootSelectionTwo, R.string.select_minimum_theme_notice, Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.secondColor));
                    snackbar.show();
                }
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    private void chooseTheme(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(R.string.theme_dialog_title);
        builder.setMessage(name[position] + getString(R.string.theme_dialog_message));

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        apptheme = name[position];
                        tv_status_selection_two.setVisibility(View.VISIBLE);
                        tv_status_selection_two.setText(apptheme + getString(R.string.category_selectiontwo_message));

                        if (apptheme != null) {
                            if (dbHelper.isNewApp(appPrefs.getAPP_ID())) {
                                dbHelper.UpdateSelectionTwoPhasedata(appPrefs.getAPP_ID(), apptheme);
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
                                        Snackbar snackbar = Snackbar.make(rootSelectionTwo, R.string.message_turn_on_internet, Snackbar.LENGTH_LONG);
                                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.secondColor));
                                        snackbar.show();

                                    }

                                } else {
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
                            } else {
                                dbHelper.insertSelectionPhasedata(appPrefs.getAPP_ID(), getArguments().getString("APP_NAME"), getArguments().getString("APP_CATEGORY"), apptheme, getArguments().getString("POSITION"));
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
                                    //   Log.e("Home", "Error in creating fragment");
                                }
                            }
                        } else {
                            Snackbar snackbar = Snackbar.make(rootSelectionTwo, R.string.select_minimum_theme_notice, Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.secondColor));
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

    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {


        public CardAdapter(String[] names, String[] image, int[] color) {
            super();
            items = new ArrayList<ListItemSelectionTwo>();
            for (int i = 0; i < names.length; i++) {
                ListItemSelectionTwo item = new ListItemSelectionTwo();
                item.setName(names[i]);
                item.setImagePath(image[i]);
                item.setColor(color[i]);
                item.setView_type(i);
                items.add(item);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_card_selection_two, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final ListItemSelectionTwo list = items.get(position);
            holder.textViewName.setText("Theme " + list.getName());
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.mipmap.ic_launcher)
                    .showImageOnFail(R.mipmap.ic_launcher)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .showImageOnLoading(R.mipmap.ic_launcher).build();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
            imageLoader.displayImage(IMAGE_THEME_PATH + name[position] + ".png", holder.imgList, options, new SimpleImageLoadingListener() {
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
            holder.imgList.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.color_item));
            holder.relativeLayout.setBackgroundResource(color[position]);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chooseTheme(position);
                }
            });
            holder.relativeLayout.setBackgroundResource(color[position]);

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textViewName;
            FloatingActionButton floatingActionButton;
            ImageView imgList;
            RelativeLayout relativeLayout;
            CheckBox checkBox_select_two;
            CardView cardView;

            public ViewHolder(View itemView) {
                super(itemView);
                imgList = (ImageView) itemView.findViewById(R.id.img_list_card);
                textViewName = (TextView) itemView.findViewById(R.id.textViewName);
                relativeLayout = (RelativeLayout) itemView.findViewById(R.id.selection_rrout);
                checkBox_select_two = (CheckBox) itemView.findViewById(R.id.checkBox_selection_two);
                cardView = (CardView) itemView.findViewById(R.id.card_selection_one);
            }
        }
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
            Snackbar snackbar = Snackbar.make(rootSelectionTwo, R.string.message_please_try_later, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.secondColor));
            snackbar.show();
        }
    }
}
