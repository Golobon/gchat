package com.golobon.gchat;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.golobon.gchat.adapter.SearchUserRecyclerAdapter;
import com.golobon.gchat.model.UserModel;
import com.golobon.gchat.utils.FireBaseUtil;
import com.google.firebase.firestore.Query;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
                //whereEqualTo("username", searchUserInfo);
                .whereGreaterThanOrEqualTo("username", searchUserInfo)
                .whereLessThanOrEqualTo("username", searchUserInfo+ '\uf8ff');

        FirestoreRecyclerOptions<UserModel> options =
                new FirestoreRecyclerOptions.Builder<UserModel>()
                        .setQuery(query, UserModel.class).build();

        adapter = new SearchUserRecyclerAdapter(options, getApplicationContext());
        rvFoundUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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
            adapter.notifyDataSetChanged();
        }
    }
    static class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }
}