package com.letsappbuilder.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.letsappbuilder.Adapter.ListItemSelectionOne;
import com.letsappbuilder.MainActivity;
import com.letsappbuilder.R;
import com.letsappbuilder.Response.SignUpResponse;
import com.letsappbuilder.Utils.AppPrefs;
import com.letsappbuilder.Utils.Common;
import com.letsappbuilder.Utils.DbHelper;
import com.google.gson.Gson;
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
public class fragment_selection_one extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private GridLayoutManager lLayout;
    DbHelper dbHelper;
    AppPrefs appPrefs;
    ScrollView rootSelectionOne;
    Common common;
    int POSITION;
    String appcategory;
    TextView tv_status_selection_one;
    EditText edtAppName;
    Dialog dialog;
    DisplayImageOptions options;
    ImageLoader imageLoader;
    String APP_ID, APP_NAME, APP_ICON, SPLASH_ICON, APP_CATEGORY, APP_THEME, THEME_COLOR, TEXT_COLOR, PUBLISH_ID, APP_PAGE, APP_PAGES_ID;
    public static String[] name = {"Personalization", "Ecommerce", "Wallpaper", "Visiting Card", "Resume", "Channel", "News", "Conference", "Wedding", "Casino", "Fitness", "Travel", "Sport", "Blog", "Finance", "Music", "Transportation", "Festival", "Birthday", "Fashion", "Organization", "Hire Cab", "Insurance", "Law Firm", "Worship", "Health", "Photography", "Online Business", "Remote Work", "Business", "Information", "Restaurant", "Education", "Construction", "Real Estate", "Chat"};
    int[] image = {R.drawable.personalization, R.drawable.ecommerce, R.drawable.wallpaper, R.drawable.visit_card, R.drawable.resume, R.drawable.channel, R.drawable.news, R.drawable.conference, R.drawable.wedding, R.drawable.casino, R.drawable.fitness, R.drawable.travel, R.drawable.sport, R.drawable.blog, R.drawable.finance, R.drawable.musician, R.drawable.transportation, R.drawable.festival, R.drawable.birthday, R.drawable.fashion, R.drawable.organization, R.drawable.hire_cab, R.drawable.insurance, R.drawable.law_firm, R.drawable.worship, R.drawable.health, R.drawable.photography, R.drawable.online_business, R.drawable.remote_working, R.drawable.business, R.drawable.information, R.drawable.restaurant, R.drawable.education, R.drawable.consruction, R.drawable.real_estate, R.drawable.chat_filled};
    int[] color = {R.color.firstColor, R.color.secondColor, R.color.thirdColor, R.color.fourthColor, R.color.fifthColor, R.color.sixthColor, R.color.firstColor, R.color.secondColor, R.color.thirdColor, R.color.fourthColor, R.color.fifthColor, R.color.sixthColor};
    public String[] category_description_set;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selection_one, container, false);
        rootSelectionOne = (ScrollView) v.findViewById(R.id.root_fragment_selection_one);
        MainActivity.frameToolbar.setVisibility(View.VISIBLE);
        MainActivity.secondframeToolbar.setVisibility(View.GONE);

        MainActivity.frameToolbar.setBackgroundColor(getResources().getColor(R.color.secondColor));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.secondColor));
        }
        category_description_set = new String[]{getActivity().getResources().getString(R.string.personalization), getActivity().getResources().getString(R.string.ecommerce), getActivity().getResources().getString(R.string.wallpaper), getActivity().getResources().getString(R.string.visiting_card), getActivity().getResources().getString(R.string.resume), getActivity().getResources().getString(R.string.channel), getActivity().getResources().getString(R.string.news), getActivity().getResources().getString(R.string.conference), getActivity().getResources().getString(R.string.wedding), getActivity().getResources().getString(R.string.casino), getActivity().getResources().getString(R.string.fitness), getActivity().getResources().getString(R.string.travel), getActivity().getResources().getString(R.string.sport), getActivity().getResources().getString(R.string.blog), getActivity().getResources().getString(R.string.finance), getActivity().getResources().getString(R.string.music), getActivity().getResources().getString(R.string.transportation), getActivity().getResources().getString(R.string.festival), getActivity().getResources().getString(R.string.birthday), getActivity().getResources().getString(R.string.fashion), getActivity().getResources().getString(R.string.organization), getActivity().getResources().getString(R.string.hire_cab), getActivity().getResources().getString(R.string.insurance), getActivity().getResources().getString(R.string.law_firm), getActivity().getResources().getString(R.string.worship), getActivity().getResources().getString(R.string.health), getActivity().getResources().getString(R.string.photography), getActivity().getResources().getString(R.string.online_business), getActivity().getResources().getString(R.string.remote_work), getActivity().getResources().getString(R.string.business), getActivity().getResources().getString(R.string.information), getActivity().getResources().getString(R.string.restaurant), getActivity().getResources().getString(R.string.education), getActivity().getResources().getString(R.string.construction), getActivity().getResources().getString(R.string.real_estate), getActivity().getResources().getString(R.string.chat)};
        tv_status_selection_one = (TextView) v.findViewById(R.id.tv_status_selection_one);
        dbHelper = new DbHelper(getActivity());
        dialog = new Dialog(getActivity());
        //  Log.e("%%%", dbHelper.selectJsonString(fragment_selection_two.AUTO_APP_ID));
        common = new Common(getActivity());
        appPrefs = new AppPrefs(getActivity());
        // MainActivity.bottomNavigationView.selectTab(1);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        edtAppName = (EditText) v.findViewById(R.id.et_appname);

        MainActivity.animator(getActivity(), MainActivity.imgMainNext);
        MainActivity.animator(getActivity(), MainActivity.imgMainPrev);
        MainActivity.tv_main.setText(R.string.toolbar_selection1);
        lLayout = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        // lLayout = new StaggeredGridLayoutManager(2, 1);

        recyclerView.setLayoutManager(lLayout);
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.mipmap.ic_launcher).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        adapter = new CardAdapter(name, image, color);
        recyclerView.setAdapter(adapter);
      //  Log.e("***One", appPrefs.getAPP_ID());
        if (dbHelper.isNewApp(appPrefs.getAPP_ID())) {
            edtAppName.setText(dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.APP_NAME));
            tv_status_selection_one.setVisibility(View.VISIBLE);
            appcategory = dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.APP_CATEGORY);
            tv_status_selection_one.setText(dbHelper.SelectAttributeviseData(appPrefs.getAPP_ID(), DbHelper.APP_CATEGORY) + " category has been selected.");
        }

        MainActivity.imgMainPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new fragment_main();
                if (fragment != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.firstColor));
                    }

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();
                    MainActivity.frameToolbar.setBackgroundColor(getResources().getColor(R.color.firstColor));

                } else {
                  //  Log.e("Home", "Error in creating fragment");
                }
            }
        });
        MainActivity.imgMainNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtAppName.getText().toString().isEmpty()) {
                    edtAppName.setError(getString(R.string.enter_app_name));
                } else if (appcategory != null) {
                    if (dbHelper.isNewApp(appPrefs.getAPP_ID())) {
                        dbHelper.UpdateSelectionOnePhasedata(appPrefs.getAPP_ID(), edtAppName.getText().toString(), appcategory);
                        if (appPrefs.getIS_NEW_APP().equals("false")) {
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            try {
                                String query = "SELECT * FROM " + DbHelper.TABLE_NAME + " WHERE " + DbHelper.APP_ID + " = '" + appPrefs.getAPP_ID() + "'";
                                Cursor resultset = db.rawQuery(query, null);

                                if (resultset != null) {
                                    //  Log.e("in function%%", resultset.getCount() + "");
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
                                Snackbar snackbar = Snackbar.make(rootSelectionOne, R.string.message_turn_on_internet, Snackbar.LENGTH_LONG);
                                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.secondColor));
                                snackbar.show();
                            }
                        } else {
                            Fragment fragment = new fragment_selection_two();
                            if (fragment != null) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                    getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.secondColor));
                                }
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();

                            } else {
                              //  Log.e("Home", "Error in creating fragment");
                            }
                        }
                    } else {
                        Fragment fragment = new fragment_selection_two();
                        Bundle bundle = new Bundle();
                        bundle.putString("APP_NAME", edtAppName.getText().toString());
                        bundle.putString("APP_CATEGORY", appcategory);
                        bundle.putString("POSITION", String.valueOf(POSITION));
                        fragment.setArguments(bundle);
                        if (fragment != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.secondColor));
                            }
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();

                        } else {
                           // Log.e("Home", "Error in creating fragment");
                        }
                    }

                } else {
                    Snackbar snackbar = Snackbar.make(rootSelectionOne, R.string.select_minimum_category_notice, Snackbar.LENGTH_LONG);
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
        //  MainActivity.loadProgressMenuItem(menu, 40);
    }

    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

        List<ListItemSelectionOne> items;
        List<String> nameList; //new ArrayList is only needed if you absolutely need an ArrayList

        public CardAdapter(String[] names, int[] image, int[] color) {
            super();
            nameList = new ArrayList<String>(Arrays.asList(names));
            items = new ArrayList<ListItemSelectionOne>();
            for (int i = 0; i < names.length; i++) {
                ListItemSelectionOne item = new ListItemSelectionOne();
                item.setName(names[i]);
                item.setImagePath(image[i]);
                item.setColor(color[i % 6]);
                item.setViewtype(i);
                items.add(item);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_card_selection_one, parent, false);
          //  Log.e("$$$", "" + viewType);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final ListItemSelectionOne list = items.get(position);
            // holder.textViewName.setTag(image[position]);
            holder.textViewName.setText(list.getName());
            // holder.cardView.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_up));
            holder.imgList.setImageResource(image[position]);
            holder.imgList.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up));
            holder.imgAbout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.setContentView(R.layout.selection_one_about_dialog);
                    dialog.setCancelable(true);
                    final TextView txtCategory = (TextView) dialog.findViewById(R.id.txt_category);
                    final TextView txtDescription = (TextView) dialog.findViewById(R.id.txt_description);
                    final ImageView imgSplash = (ImageView) dialog.findViewById(R.id.img_splash);
                    txtCategory.setText(items.get(position).getName().toUpperCase());
                    final LinearLayout rootlloutSelectionOnedialog = (LinearLayout) dialog.findViewById(R.id.root_selection_one_dialog);
                    rootlloutSelectionOnedialog.setBackgroundResource(items.get(position).getColor());
                    txtDescription.setText(category_description_set[position]);
                    imageLoader.displayImage(Utility.DIR_IMAGE_DOWNLOAD_PATH + MainActivity.splash_image_set[position], imgSplash, options, new SimpleImageLoadingListener() {
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

                    dialog.show();
                }
            });
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chooseTheme(position);
                }
            });

            holder.relativeLayout.setBackgroundResource(color[position % 6]);

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getItemViewType(int position) {
            return items.get(position).getViewtype();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            CardView cardView;
            public TextView textViewName;
            ImageView imgList, imgAbout;
            RelativeLayout relativeLayout;
            CheckBox checkBox_select_one;

            public ViewHolder(View itemView) {
                super(itemView);
                imgList = (ImageView) itemView.findViewById(R.id.img_list_card);
                imgAbout = (ImageView) itemView.findViewById(R.id.img_about_category);
                textViewName = (TextView) itemView.findViewById(R.id.textViewName);
                relativeLayout = (RelativeLayout) itemView.findViewById(R.id.selection_rrout);
                checkBox_select_one = (CheckBox) itemView.findViewById(R.id.checkBox_selection_one);
                cardView = (CardView) itemView.findViewById(R.id.card_selection_one);
            }
        }
    }

    private void chooseTheme(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(getString(R.string.category_dialog_title));
        builder.setMessage(name[position] + getString(R.string.category_dialog_message));

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        appcategory = name[position];
                        POSITION = position;
                        tv_status_selection_one.setVisibility(View.VISIBLE);
                        tv_status_selection_one.setText(appcategory + getString(R.string.category_dialog_selected_message));
                        if (edtAppName.getText().toString().isEmpty()) {
                            edtAppName.setError(getString(R.string.enter_app_name));
                        } else if (appcategory != null) {
                            if (dbHelper.isNewApp(appPrefs.getAPP_ID())) {
                                dbHelper.UpdateSelectionOnePhasedata(appPrefs.getAPP_ID(), edtAppName.getText().toString(), appcategory);
                                if (appPrefs.getIS_NEW_APP().equals("false")) {
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    try {
                                        String query = "SELECT * FROM " + DbHelper.TABLE_NAME + " WHERE " + DbHelper.APP_ID + " = '" + appPrefs.getAPP_ID() + "'";
                                        Cursor resultset = db.rawQuery(query, null);

                                        if (resultset != null) {
                                            //  Log.e("in function%%", resultset.getCount() + "");
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
                                        Snackbar snackbar = Snackbar.make(rootSelectionOne, R.string.message_turn_on_internet, Snackbar.LENGTH_LONG);
                                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.secondColor));
                                        snackbar.show();
                                    }
                                } else {
                                    Fragment fragment = new fragment_selection_two();
                                    if (fragment != null) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.secondColor));
                                        }
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();

                                    } else {
                                      //  Log.e("Home", "Error in creating fragment");
                                    }
                                }
                            } else {
                                Fragment fragment = new fragment_selection_two();
                                Bundle bundle = new Bundle();
                                bundle.putString("APP_NAME", edtAppName.getText().toString());
                                bundle.putString("APP_CATEGORY", appcategory);
                                bundle.putString("POSITION", String.valueOf(POSITION));
                                fragment.setArguments(bundle);
                                if (fragment != null) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.secondColor));
                                    }
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();

                                } else {
                                   // Log.e("Home", "Error in creating fragment");
                                }
                            }

                        } else {
                            Snackbar snackbar = Snackbar.make(rootSelectionOne, R.string.select_minimum_category_notice, Snackbar.LENGTH_LONG);
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
              //  Log.e("###", str);
                if (str != null) {
                    SignUpResponse response = new Gson().fromJson(str, SignUpResponse.class);
                    if (response.result.equals("success")) {
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
            Snackbar snackbar = Snackbar.make(rootSelectionOne, R.string.message_please_try_later, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.secondColor));
            snackbar.show();
        }
    }


}
