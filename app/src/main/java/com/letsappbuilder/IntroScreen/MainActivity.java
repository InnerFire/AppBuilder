package com.letsappbuilder.IntroScreen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.letsappbuilder.LoginActivity;
import com.letsappbuilder.R;
import com.letsappbuilder.Utils.AppPrefs;

/**
 * Main activity.
 */
public class MainActivity extends AppCompatActivity {
    static Button btnSkip;
    AppPrefs appPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity_main);
        appPrefs = new AppPrefs(getApplicationContext());
        if (!appPrefs.getUserId().isEmpty()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.firstColor));
        }

        btnSkip = (Button) findViewById(R.id.btn_skip);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MainFragment.instance())
                    .commit();
        }
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
