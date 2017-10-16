package com.letsappbuilder.Fragment;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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

import com.letsappbuilder.Adapter.AppDetailClass;
import com.letsappbuilder.MainActivity;
import com.letsappbuilder.R;
import com.letsappbuilder.Utils.AppPrefs;
import com.letsappbuilder.Utils.Common;
import com.letsappbuilder.Utils.DbHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;


public class fragment_draftapps extends Fragment {
    DbHelper dbHelper;
    TextView tvErrorMsg;
    AppPrefs appPrefs;
    int[] color = {R.color.firstColor, R.color.secondColor, R.color.thirdColor, R.color.fourthColor, R.color.fifthColor, R.color.sixthColor};
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private FrameLayout rootDraftapps;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_draft_apps, container, false);
        MainActivity.frameToolbar.setVisibility(View.GONE);
        MainActivity.secondframeToolbar.setVisibility(View.VISIBLE);
        dbHelper = new DbHelper(getActivity());
        appPrefs = new AppPrefs(getContext());
        rootDraftapps = (FrameLayout) v.findViewById(R.id.root_frame_llout);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        MainActivity.secondframeToolbar.setBackgroundColor(getResources().getColor(R.color.secondColor));
        MainActivity.animator(getActivity(), MainActivity.secondimgMainNext);
        MainActivity.animator(getActivity(), MainActivity.secondimgMainPrev);
        MainActivity.tv_main_second.setText(R.string.toolbar_draftapps);

        tvErrorMsg = (TextView) v.findViewById(R.id.tv_draft_apps);
        if (dbHelper.GetDraftAppDetails().size() > 0) {
            tvErrorMsg.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new CardAdapter(dbHelper.GetDraftAppDetails());
            recyclerView.setAdapter(adapter);
        }
        recyclerView.setHasFixedSize(true);

        return v;
    }

    public void navigate_to_fragment(String app_id) {
        Fragment fragment = new fragment_selection_one();
        appPrefs.setIS_NEW_APP("true");
        appPrefs.setAPP_ID(app_id);
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

    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

        ArrayList<AppDetailClass> items;

        public CardAdapter(ArrayList<AppDetailClass> list_drafts) {
            super();
            items = list_drafts;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_card_draft_app, parent, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            // holder.textViewName.setTag(image[position]);
            holder.tvappname.setText(items.get(position).getAPP_NAME());
            holder.tvappcategory.setText(items.get(position).getAPP_CATEGORY());
            holder.tvapppublishdate.setText(R.string.not_yet_published);
            holder.imgappicon.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.color_item));
            holder.frameLayoutDraftapp.setTag(items.get(position).APP_ID);
            holder.frameLayoutDraftapp.setBackgroundResource(color[position]);

            holder.frameLayoutDraftapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigate_to_fragment(holder.frameLayoutDraftapp.getTag().toString());
                }
            });

            holder.imgdeletedraftapp.setOnClickListener(new View.OnClickListener() {
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
                                    // do for deletion
                                    dbHelper.DeleteFromMyApps(items.get(position).APP_ID);
                                    items.remove(position);
                                    adapter.notifyItemRemoved(position);
                                    adapter.notifyDataSetChanged();
                                    Snackbar snackbar = Snackbar.make(rootDraftapps, R.string.message_app_success_delete, Snackbar.LENGTH_SHORT);
                                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.secondColor));
                                    snackbar.show();

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

            holder.imgappicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  navigate_to_fragment();
                }
            });
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
            imageLoader.displayImage(Common.imageBackgroundURL[position % Common.imageBackgroundURL.length], holder.imgBackcover, options, new SimpleImageLoadingListener() {
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
                        holder.imgBackcover.setBackgroundDrawable(drawableBitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            imageLoader.displayImage(Utility.DIR_IMAGE_DOWNLOAD_PATH + items.get(position).getAPP_ICON(), holder.imgappicon, options, new SimpleImageLoadingListener() {
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
            ImageView imgappicon, imgdeletedraftapp, imgBackcover;
            FrameLayout frameLayoutDraftapp;

            public ViewHolder(View itemView) {
                super(itemView);
                tvappname = (TextView) itemView.findViewById(R.id.tv_app_name);
                tvappcategory = (TextView) itemView.findViewById(R.id.tv_app_category);
                tvapppublishdate = (TextView) itemView.findViewById(R.id.tv_app_published_date);
                imgappicon = (ImageView) itemView.findViewById(R.id.img_app_icon);
                imgdeletedraftapp = (ImageView) itemView.findViewById(R.id.img_delete_draftapp);
                frameLayoutDraftapp = (FrameLayout) itemView.findViewById(R.id.framellout_draft_apps);
                imgBackcover = (ImageView) itemView.findViewById(R.id.img_app_backcover);
            }
        }
    }

}
