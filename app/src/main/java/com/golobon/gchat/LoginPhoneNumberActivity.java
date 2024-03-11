package com.golobon.gchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.hbb20.CountryCodePicker;

public class LoginPhoneNumberActivity extends AppCompatActivity {
    CountryCodePicker countryCodePicker;
    EditText phoneInput;
    Button btnSendOtp;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone_number);

        UiStyleSettings.setUiStyle(this);

        countryCodePicker = findViewById(R.id.country_code_picker);
        phoneInput = findViewById(R.id.et_mob_namber);
        btnSendOtp = findViewById(R.id.btn_sent_otp);
        progressBar = findViewById(R.id.pb_login_mob_number);

        progressBar.setVisibility(View.GONE);

        countryCodePicker.registerCarrierNumberEditText(phoneInput);

        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!countryCodePicker.isValidFullNumber()) {
                    phoneInput.setError("Phone number is not valid");
                    return;
                }
                Intent intent = new Intent(LoginPhoneNumberActivity.this,
                        LoginOtpActivity.class);
                intent.putExtra("phone", countryCodePicker.getFullNumberWithPlus());
                startActivity(intent);
            }
        });
    }
}