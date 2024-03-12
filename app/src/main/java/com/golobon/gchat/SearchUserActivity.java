package com.golobon.gchat;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class SearchUserActivity extends AppCompatActivity {
    EditText etSearchUserInput;
    ImageButton btnSearchUser;
    ImageButton btnBack;
    RecyclerView rvFoundUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        UiStyleSettings.setUiStyle(this);

        etSearchUserInput = findViewById(R.id.et_search_username_input);
        btnSearchUser = findViewById(R.id.btn_search_user);
        btnBack = findViewById(R.id.btn_search_back);
        rvFoundUsers = findViewById(R.id.rv_search_user);

        etSearchUserInput.requestFocus();

        btnBack.setOnClickListener(v -> onBackPressed());

        btnSearchUser.setOnClickListener(v -> {
            String searchUserInfo = etSearchUserInput.getText().toString();
            if (searchUserInfo.isEmpty() || searchUserInfo.length() < 3) {
                etSearchUserInput.setError("Некорректное имя");
                return;
            }
            setupSearchRecyclerView(searchUserInfo);
        });
    }

    private void setupSearchRecyclerView(String searchUserInfo) {

    }
}