package com.letsappbuilder.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.letsappbuilder.MainActivity;
import com.letsappbuilder.R;

public class fragment_purchase_successful extends Fragment {
    TextView txtPlanDescription;
    Button bt_install;
    FrameLayout rootPaymentSuccess;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_purchase_success, container, false);
        bt_install = (Button) v.findViewById(R.id.bt_install);
        rootPaymentSuccess = (FrameLayout) v.findViewById(R.id.root_payment_succeess);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.fourthColor));
        }

        MainActivity.frameToolbar.setVisibility(View.GONE);
        MainActivity.secondframeToolbar.setVisibility(View.VISIBLE);
        MainActivity.secondframeToolbar.setBackgroundColor(getResources().getColor(R.color.fourthColor));
        MainActivity.animator(getActivity(), MainActivity.secondimgMainNext);
        MainActivity.animator(getActivity(), MainActivity.secondimgMainPrev);
        MainActivity.tv_main_second.setText(R.string.toolbar_payment);
        txtPlanDescription = (TextView) v.findViewById(R.id.plan_description);
        txtPlanDescription.setText("\nNow your app has been Published and Live\n\t" + "App name :" + getArguments().getString("APP_NAME").trim() + "\n\tPublish ID :" + getArguments().getString("PUBLISH_ID").trim());

        bt_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/jXuS77?appname=" + getArguments().getString("APP_NAME").trim())));
            }
        });

        Snackbar snackbar = Snackbar.make(rootPaymentSuccess, R.string.message_email_sent_spam, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.fourthColor));
        snackbar.show();

        return v;
    }
}
