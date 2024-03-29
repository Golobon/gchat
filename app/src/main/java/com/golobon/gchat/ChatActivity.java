package com.golobon.gchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.golobon.gchat.model.ChatroomModel;
import com.golobon.gchat.model.UserModel;
import com.golobon.gchat.utils.AndroidUtil;
import com.golobon.gchat.utils.FireBaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {
    UserModel otherUser;
    String chatroomId;
    ChatroomModel chatroomModel;
    EditText etMessageInput;
    ImageButton btnSendMessage;
    ImageButton btnBack;
    TextView tvOtherUsername;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        chatroomId = FireBaseUtil.getChatroomId(FireBaseUtil.currentUserId(),
                otherUser.getUserId());

        etMessageInput = findViewById(R.id.et_chat_message_input);
        btnSendMessage = findViewById(R.id.btn_chat_send_message);
        btnBack = findViewById(R.id.btn_chat_back);
        tvOtherUsername = findViewById(R.id.tv_chat_other_username);
        tvOtherUsername.setText(otherUser.getUsername());
        recyclerView = findViewById(R.id.rv_chat_messages);

        btnBack.setOnClickListener(v -> onBackPressed());
        
        getOrCreateChatroomModel();
    }

    private void getOrCreateChatroomModel() {
        FireBaseUtil.getChatroomReference(chatroomId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    chatroomModel = task.getResult().toObject(ChatroomModel.class);
                    if (chatroomModel == null) {
                        chatroomModel = new ChatroomModel(chatroomId,
                                Arrays.asList(FireBaseUtil.currentUserId(),
                                        otherUser.getUserId()),
                                Timestamp.now(),
                                "");
                        FireBaseUtil.getChatroomReference(chatroomId).set(chatroomModel);
                    }
                }
            }
        });
    }
}