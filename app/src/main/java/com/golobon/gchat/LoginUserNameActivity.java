package com.golobon.gchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.golobon.gchat.model.UserModel;
import com.golobon.gchat.utils.FireBaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginUserNameActivity extends AppCompatActivity {
    EditText etUsernameInput;
    Button btnLetIn;
    ProgressBar progressBar;
    String phoneNumber;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user_name);

        UiStyleSettings.setUiStyle(this);

        etUsernameInput = findViewById(R.id.et_third_username);
        btnLetIn = findViewById(R.id.btn_third_let_in);
        progressBar = findViewById(R.id.pb_third);

        phoneNumber = getIntent().getExtras().getString("phone");

        getUserName();

        btnLetIn.setOnClickListener(v -> setUsername());
    }

    void setUsername () {
        setInProgress(true);
        String username = etUsernameInput.getText().toString();
        if (username.isEmpty() || username.length() < 3) {
            etUsernameInput.setError("Имя слишком короткое");
            return;
        }
        if (userModel != null) {
            userModel.setUsername(username);
        } else {
            userModel = new UserModel(phoneNumber, username, Timestamp.now());
        }
        FireBaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginUserNameActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    void getUserName() {
        setInProgress(true);
        FireBaseUtil.currentUserDetails()
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        setInProgress(false);
                        if (task.isSuccessful()) {
                            userModel = task.getResult().toObject(UserModel.class);
                            if (userModel != null) {
                                etUsernameInput.setText(userModel.getUsername());
                            }
                        }
                    }
                });
    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            btnLetIn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            btnLetIn.setVisibility(View.VISIBLE);
        }
    }
}