package com.golobon.gchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.golobon.gchat.adapter.ChatRecyclerAdapter;
import com.golobon.gchat.adapter.SearchUserRecyclerAdapter;
import com.golobon.gchat.model.ChatMessageModel;
import com.golobon.gchat.model.ChatroomModel;
import com.golobon.gchat.model.UserModel;
import com.golobon.gchat.utils.AndroidUtil;
import com.golobon.gchat.utils.FireBaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {
    UserModel otherUser;
    String chatroomId;
    ChatroomModel chatroomModel;
    ChatRecyclerAdapter adapter;

    EditText etMessageInput;
    ImageButton btnSendMessage;
    ImageButton btnBack;
    TextView tvOtherUsername;
    RecyclerView recyclerView;
    ImageView ivUserProfilePic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        UiStyleSettings.setUiStyle(this);

        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        chatroomId = FireBaseUtil.getChatroomId(FireBaseUtil.currentUserId(),
                otherUser.getUserId());

        etMessageInput = findViewById(R.id.et_chat_message_input);
        btnSendMessage = findViewById(R.id.btn_chat_send_message);
        btnBack = findViewById(R.id.btn_chat_back);
        tvOtherUsername = findViewById(R.id.tv_chat_other_username);
        tvOtherUsername.setText(otherUser.getUsername());
        recyclerView = findViewById(R.id.rv_chat_messages);
        ivUserProfilePic = findViewById(R.id.iv_user_profile_pic);

        FireBaseUtil.getOtgerProfilePicStorageReference(otherUser.getUserId())
                .getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri uriProfilePic = task.getResult();
                            AndroidUtil.setProfilePic(ChatActivity.this,
                                    uriProfilePic, ivUserProfilePic);
                        }
                    }
                });

        btnSendMessage.setOnClickListener(v -> {
            String message = etMessageInput.getText().toString().trim();
            if (message.isEmpty()) return;
            else sendMessageToUser(message);
        });
        btnBack.setOnClickListener(v -> onBackPressed());

        getOrCreateChatroomModel();

        setupChatRecyclerView();
    }

    private void setupChatRecyclerView() {
        Query query = FireBaseUtil.getChatroomMessageReference(chatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options =
                new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                        .setQuery(query, ChatMessageModel.class).build();

        adapter = new ChatRecyclerAdapter(options, getApplicationContext());
        LinearLayoutManager manager =  new LinearLayoutManager(this);
        manager.setReverseLayout(true);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private void sendMessageToUser(String message) {
        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderId(FireBaseUtil.currentUserId());
        chatroomModel.setLastMessage(message);
        FireBaseUtil.getChatroomReference(chatroomId).set(chatroomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message,
                FireBaseUtil.currentUserId(),
                Timestamp.now());

        FireBaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            etMessageInput.setText("");
                        }
                    }
                });
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