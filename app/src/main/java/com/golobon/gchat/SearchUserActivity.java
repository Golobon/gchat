package com.golobon.gchat;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.golobon.gchat.adapter.SearchUserRecyclerAdapter;
import com.golobon.gchat.model.UserModel;
import com.golobon.gchat.utils.FireBaseUtil;
import com.google.firebase.firestore.Query;

public class SearchUserActivity extends AppCompatActivity {
    EditText etSearchUserInput;
    ImageButton btnSearchUser;
    ImageButton btnBack;
    RecyclerView rvFoundUsers;

    SearchUserRecyclerAdapter adapter;

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
     void setupSearchRecyclerView(String searchUserInfo) {
        Query query = FireBaseUtil.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("username", searchUserInfo);

        FirestoreRecyclerOptions<UserModel> options =
                new FirestoreRecyclerOptions.Builder<UserModel>()
                        .setQuery(query, UserModel.class).build();

        adapter = new SearchUserRecyclerAdapter(options, getApplicationContext());
        rvFoundUsers.setLayoutManager(new LinearLayoutManager(this));
        rvFoundUsers.setAdapter(adapter);
        adapter.startListening();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
        }
    }
}