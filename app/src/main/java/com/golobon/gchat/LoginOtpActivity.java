package com.golobon.gchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.golobon.gchat.utils.AndroidUtil;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {
    String phoneNumber;
    Long timeOutSeconds = 60L;
    String veryficationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;

    EditText etOtp;
    Button btnNext;
    ProgressBar progressBar;
    TextView tvResendOtp;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        UiStyleSettings.setUiStyle(this);

        etOtp = findViewById(R.id.et_login_otp);
        btnNext = findViewById(R.id.btn_login_next);
        progressBar = findViewById(R.id.pb_login_next);
        tvResendOtp = findViewById(R.id.tv_resend_otp_text);

        phoneNumber = getIntent().getExtras().getString("phone");

        sendOtp(phoneNumber, false);
    }
    void sendOtp(String phoneNumber, boolean isResend) {
        setInProgress(true);
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(timeOutSeconds, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                 signIn(phoneAuthCredential);
                                 setInProgress(false);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                AndroidUtil.showToast(getApplicationContext(),
                                        "Не удолдась запросить код");
                                setInProgress(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                veryficationCode = s;
                                resendingToken = forceResendingToken;
                                AndroidUtil.showToast(getApplicationContext(),
                                        "Код успешно запрошен");
                                setInProgress(false);
                            }
                        });
        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    void signIn(PhoneAuthCredential phoneAuthCredential) {
        //Login and go to nest activity

    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
        }
    }

}