package com.letsappbuilder;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.letsappbuilder.Custom.FlatButton;
import com.letsappbuilder.Fragment.fragment_chat;
import com.letsappbuilder.Fragment.fragment_design_one;
import com.letsappbuilder.Fragment.fragment_design_two;
import com.letsappbuilder.Fragment.fragment_draftapps;
import com.letsappbuilder.Fragment.fragment_main;
import com.letsappbuilder.Fragment.fragment_myapps;
import com.letsappbuilder.Fragment.fragment_myapps_chat;
import com.letsappbuilder.Fragment.fragment_myapps_push_notification;
import com.letsappbuilder.Fragment.fragment_myapps_upgrade;
import com.letsappbuilder.Fragment.fragment_profile;
import com.letsappbuilder.Fragment.fragment_selection_one;
import com.letsappbuilder.Fragment.fragment_selection_two;
import com.letsappbuilder.Utils.AppPrefs;
import com.letsappbuilder.Utils.Common;
import com.letsappbuilder.Utils.DbHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static DrawerLayout drawer;
    public static FrameLayout frameToolbar;
    public static ImageView imgMainPrev, imgMainNext;
    public static TextView tv_main, tv_main_second;
    public static FrameLayout secondframeToolbar;
    public static ImageView secondimgMainPrev, secondimgMainNext;
    public static String[] app_icon_set;
    public static String[] splash_image_set = {"splash0.jpg", "splash1.jpg", "splash2.jpg", "splash3.jpg", "splash4.jpg", "splash5.jpg", "splash6.jpg", "splash7.jpg", "splash8.jpg", "splash9.jpg", "splash10.jpg", "splash11.jpg", "splash12.jpg", "splash13.jpg", "splash14.jpg", "splash15.jpg", "splash16.jpg", "splash17.jpg", "splash18.jpg", "splash19.jpg", "splash20.jpg", "splash21.jpg", "splash22.jpg", "splash23.jpg", "splash24.jpg", "splash25.jpg", "splash26.jpg", "splash27.jpg", "splash28.jpg", "splash29.jpg", "splash30.jpg", "splash31.jpg", "splash32.jpg", "splash33.jpg", "splash34.jpg", "splash35.jpg"};
    List<Integer> listItems;
    AppPrefs appPrefs;
    DbHelper dbHelper;
    Common common;

    public static void animator(Context context, ImageView imageButton) {
        imageButton.setVisibility(View.INVISIBLE);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.color_item);
        animation.setInterpolator(new AccelerateInterpolator());
        imageButton.setAnimation(animation);
        imageButton.setVisibility(View.VISIBLE);
        animation.start();
    }

    public static void animator(Context context, final FloatingActionButton imageButton) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.color_item);
        animation.setInterpolator(new AccelerateInterpolator());
        imageButton.setAnimation(animation);
        imageButton.setVisibility(View.VISIBLE);
        animation.start();
    }

    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public Dialog showRateDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams WMLP = dialog.getWindow().getAttributes();
        WMLP.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(WMLP);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_rate);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
        final FlatButton flatButton = (FlatButton) dialog.findViewById(R.id.button);
        final boolean[] canGoToPlayStore = {false};
        final float[] ratingGivenByUser = {0};


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                flatButton.setVisibility(View.VISIBLE);
                ratingGivenByUser[0] = rating;
                if (rating >= 4) {
                    canGoToPlayStore[0] = true;
                    flatButton.setText(R.string.show_rate_title);
                } else {
                    canGoToPlayStore[0] = false;
                    flatButton.setText(R.string.rate_dialog_title);
                }
            }
        });

        flatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canGoToPlayStore[0]) {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.letsappbuilder");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(goToMarket);
                    dialog.cancel();
                } else {
                    dialog.cancel();
                }

            }
        });
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app_icon_set = new String[splash_image_set.length];
        for (int i = 0; i < splash_image_set.length; i++) {
            app_icon_set[i] = "app" + i + ".png";
        }
        common = new Common(MainActivity.this);
        dbHelper = new DbHelper(getApplicationContext());
        frameToolbar = (FrameLayout) findViewById(R.id.main_toolbar_framellout);
        imgMainNext = (ImageView) findViewById(R.id.next_main);
        imgMainPrev = (ImageView) findViewById(R.id.prev_main);
        secondframeToolbar = (FrameLayout) findViewById(R.id.second_toolbar_framellout);
        secondframeToolbar.setVisibility(View.VISIBLE);
        secondimgMainNext = (ImageView) findViewById(R.id.next_main_second);
        secondimgMainPrev = (ImageView) findViewById(R.id.prev_main_second);
        tv_main = (TextView) findViewById(R.id.tv_main);
        tv_main_second = (TextView) findViewById(R.id.tv_main_second);
        //animator(getApplicationContext(), imgMainNext);
        //animator(getApplicationContext(), imgMainPrev);
        appPrefs = new AppPrefs(this);
        listItems = new ArrayList<Integer>();

        //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        displayView(0);
        if (getIntent().getStringExtra("CHAT") != null) {
            Fragment fragment = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.firstColor));
            }
            fragment = new fragment_myapps_chat();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else if (drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.closeDrawer(Gravity.LEFT);
                }
            } else {
                //  Log.e("Home", "Error in creating fragment");
            }

        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MainActivity.secondimgMainNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new fragment_myapps_push_notification();
                if (fragment != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.fourthColor));
                    }
                    MainActivity.frameToolbar.setBackgroundColor(getResources().getColor(R.color.fourthColor));
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();

                } else {
                    //  Log.e("Home", "Error in creating fragment");
                }
            }
        });


    }

    public void displayView(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.firstColor));
                }
                fragment = new fragment_main();
                break;
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.secondColor));
                }
                fragment = new fragment_selection_one();
                break;
            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.secondColor));
                }
                fragment = new fragment_selection_two();
                break;

            case 3:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.secondColor));
                }
                fragment = new fragment_draftapps();
                break;

            case 4:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.fourthColor));
                }
                fragment = new fragment_myapps();
                break;

            case 5:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.firstColor));
                }
                fragment = new fragment_myapps_chat();
                break;
            case 6:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.fourthColor));
                }
                fragment = new fragment_chat();
                break;
            case 7:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.sixthColor));
                }
                fragment = new fragment_profile();
                break;
            case 8:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.firstColor));
                }
                fragment = new fragment_myapps_upgrade();
                break;

            default:
                break;

        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();
            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                drawer.closeDrawer(Gravity.RIGHT);
            } else if (drawer.isDrawerOpen(Gravity.LEFT)) {
                drawer.closeDrawer(Gravity.LEFT);
            }
        } else {
            //  Log.e("Home", "Error in creating fragment");
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
        }
        Fragment frag = getSupportFragmentManager().findFragmentById(
                R.id.frame_layout_main);
        if (frag instanceof fragment_main) {
            this.finish();
        } else if (frag instanceof fragment_chat) {
            Fragment fragment = new fragment_myapps_chat();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();
            } else {
                //  Log.e("Home", "Error in creating fragment");
            }
        } else if (frag instanceof fragment_selection_two) {
            Fragment fragment = new fragment_selection_one();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();
            } else {
                //  Log.e("Home", "Error in creating fragment");
            }
        } else if (frag instanceof fragment_design_one) {
            Fragment fragment = new fragment_selection_two();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();
            } else {
                //  Log.e("Home", "Error in creating fragment");
            }
        } else if (frag instanceof fragment_design_two) {
            Fragment fragment = new fragment_design_one();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();
            } else {
                //   Log.e("Home", "Error in creating fragment");
            }
        } else {
            Fragment fragment = new fragment_main();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, fragment).setCustomAnimations(R.anim.slide_up, android.R.anim.fade_out).commit();
            } else {
                //  Log.e("Home", "Error in creating fragment");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            displayView(0);
        } else if (id == R.id.nav_profile) {
            displayView(7);
        } else if (id == R.id.nav_draft_apps) {
            displayView(3);
        } else if (id == R.id.nav_myapps) {
            displayView(4);
        } else if (id == R.id.nav_chat) {
            displayView(5);
        } else if (id == R.id.nav_upgrade) {
            displayView(8);
        } else if (id == R.id.nav_rateus) {
            showRateDialog().show();
        } else if (id == R.id.nav_tutorialmode) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCF2AYpddhQqXZAjzL7bsp4Q/videos?view=0&shelf_id=0&sort=dd")));
        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.intent_share_message));
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_intent_title)));

        } else if (id == R.id.nav_feedback) {
            try {
                View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                Uri uri = Uri.fromFile(store(getScreenShot(rootView), getSaltString() + ".png"));
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"letsappbuilderteam@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Hello");
                intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, getString(R.string.intent_send_mail)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_logout) {
            appPrefs.setAPP_ID("");
            appPrefs.setIS_NEW_APP("");
            appPrefs.setTEMP_IMAGE("");
            appPrefs.setPROFILEPICTURE("");
            appPrefs.setCHAT_APP_ID("");
            appPrefs.setAdditionalposition("");
            appPrefs.setMAIL("");
            appPrefs.setNAME("");
            appPrefs.setPASS("");
            appPrefs.setREFERID("");
            appPrefs.setUserId("");
            dbHelper.DeleteDataWhileLogout();
            startActivity(new Intent(MainActivity.this, com.letsappbuilder.IntroScreen.MainActivity.class));
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 3) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    public File store(Bitmap bm, String fileName) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

}
