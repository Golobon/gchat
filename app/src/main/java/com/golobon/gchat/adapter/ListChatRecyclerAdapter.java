package com.golobon.gchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.golobon.gchat.ChatActivity;
import com.golobon.gchat.R;
import com.golobon.gchat.model.ChatroomModel;
import com.golobon.gchat.model.UserModel;
import com.golobon.gchat.utils.AndroidUtil;
import com.golobon.gchat.utils.FireBaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class ListChatRecyclerAdapter
        extends FirestoreRecyclerAdapter <ChatroomModel, ListChatRecyclerAdapter.ChatroomModelViewHolder> {
    Context context;
    public ListChatRecyclerAdapter(
            @NonNull FirestoreRecyclerOptions<ChatroomModel> options, Context context) {
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull ChatroomModelViewHolder holder, int position, @NonNull ChatroomModel model) {
        FireBaseUtil.getOtherUserFromChatroom(model.getUserIds())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean isMessSendByMe = model.getLastMessageSenderId().equals(FireBaseUtil.currentUserId());
                            UserModel otherUserModel = task.getResult().toObject(UserModel.class);

                            FireBaseUtil.getOtgerProfilePicStorageReference(otherUserModel.getUserId())
                                    .getDownloadUrl()
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> t) {
                                            if (t.isSuccessful()) {
                                                Uri uriProfilePic = t.getResult();
                                                AndroidUtil.setProfilePic(context,
                                                        uriProfilePic, holder.ivUserProfilePic);
                                            }
                                        }
                                    });

                            holder.tvUserName.setText(otherUserModel.getUsername());
                            holder.tvLastMessTime.setText(FireBaseUtil.timestampToString(model.getLastMessageTimestamp()));

                            if (isMessSendByMe) holder.tvLastMessText.setText("я : " + model.getLastMessage());
                            else holder.tvLastMessText.setText(model.getLastMessage());

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, ChatActivity.class);
                                    AndroidUtil.passUserModelAsIntent(intent, otherUserModel);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }
                            });
                        }
                    }
                });
    }
    @NonNull
    @Override
    public ChatroomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_recycler_row, parent, false);
        return new ChatroomModelViewHolder(view);
    }
    class ChatroomModelViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        TextView tvLastMessText;
        TextView tvLastMessTime;
        ImageView ivUserProfilePic;
        public ChatroomModelViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_chat_list_username_text);
            tvLastMessText = itemView.findViewById(R.id.tv_chat_list_last_mess_text);
            tvLastMessTime = itemView.findViewById(R.id.tv_chat_list_last_mess_time_text);
            ivUserProfilePic = itemView.findViewById(R.id.iv_user_profile_pic);
        }
    }
}
