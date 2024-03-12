package com.golobon.gchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.golobon.gchat.utils.FireBaseUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        UiStyleSettings.setUiStyle(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (FireBaseUtil.isLoggedIn()) {
                    intent = new Intent(SplashActivity.this,
                            MainActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this,
                            LoginPhoneNumberActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1000);

    }
}