package com.golobon.gchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.golobon.gchat.model.UserModel;
import com.golobon.gchat.utils.AndroidUtil;

public class ChatActivity extends AppCompatActivity {
    UserModel otherUser;
    EditText etMessageInput;
    ImageButton btnSendMessage;
    ImageButton btnBack;
    TextView tvOtherUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        etMessageInput = findViewById(R.id.et_chat_message_input);
        btnSendMessage = findViewById(R.id.btn_chat_send_message);
        btnBack = findViewById(R.id.btn_chat_back);
        tvOtherUsername = findViewById(R.id.tv_chat_other_username);
        tvOtherUsername.setText(otherUser.getUsername());

        btnBack.setOnClickListener(v -> onBackPressed());
    }
}