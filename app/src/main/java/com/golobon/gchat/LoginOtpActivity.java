package com.golobon.gchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.golobon.gchat.utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {
    String phoneNumber;
    Long timeOutSeconds = 30L;
    String veryficationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;

    EditText etSendingOtp;
    Button btnNext;
    ProgressBar progressBar;
    TextView tvTryGetOtpOneMoreTime;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        UiStyleSettings.setUiStyle(this);

        etSendingOtp = findViewById(R.id.et_second_otp_code);
        btnNext = findViewById(R.id.btn_second_next);
        progressBar = findViewById(R.id.pb_second);
        tvTryGetOtpOneMoreTime = findViewById(R.id.tv_second_resend_otp);

        phoneNumber = getIntent().getExtras().getString("phone");

        btnNext.setOnClickListener(v -> {
            String enteredOtp = etSendingOtp.getText().toString();
            PhoneAuthCredential credential =
                    PhoneAuthProvider.getCredential(veryficationCode, enteredOtp);
            signIn(credential);
            setInProgress(true);
        });

        tvTryGetOtpOneMoreTime.setOnClickListener(v -> sendOtp(phoneNumber, true));

        sendOtp(phoneNumber, false);
    }
    void sendOtp(String phoneNumber, boolean isResend) {
        startResendTimer();
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
                                        "Не удалось запросить код");
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
        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            LoginOtpActivity.this.setInProgress(false);
                            Intent intent = new Intent(LoginOtpActivity.this, LoginUserNameActivity.class);
                            intent.putExtra("phone", phoneNumber);
                            LoginOtpActivity.this.startActivity(intent);
                        } else {
                            AndroidUtil.showToast(LoginOtpActivity.this.getApplicationContext(), "Авторизация неудачна");
                            LoginOtpActivity.this.setInProgress(false);
                        }
                    }
                });
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
    void startResendTimer() {
        tvTryGetOtpOneMoreTime.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeOutSeconds--;
                tvTryGetOtpOneMoreTime.setText("Повторно запросить код можно через: " +
                        timeOutSeconds + " сек");
                if (timeOutSeconds <= 0) {
                    timeOutSeconds = 30L;
                    timer.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvTryGetOtpOneMoreTime.setEnabled(true);
                            tvTryGetOtpOneMoreTime.setText("Повторно запросить код (жмите сюда)");
                        }
                    });
                }
            }
        }, 0, 1000);
    }
}