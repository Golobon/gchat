package com.golobon.gchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class LoginOtpActivity extends AppCompatActivity {
    String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        UiStyleSettings.setUiStyle(this);

        phoneNumber = getIntent().getExtras().getString("phone");

        Toast.makeText(this, phoneNumber, Toast.LENGTH_SHORT).show();

    }
}