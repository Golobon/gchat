package com.golobon.gchat.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.golobon.gchat.model.UserModel;
import com.golobon.gchat.utils.AndroidUtil;
import com.golobon.gchat.utils.FireBaseUtil;

public class SearchUserRecyclerAdapter
        extends FirestoreRecyclerAdapter <UserModel, SearchUserRecyclerAdapter.UserModelViewHolder> {
    Context context;
    public SearchUserRecyclerAdapter(
            @NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel model) {
        if (model.getUserId().equals(FireBaseUtil.currentUserId())) {
            holder.tvUserName.setText(model.getUsername() + " (я)");
        } else {
            holder.tvUserName.setText(model.getUsername());
        }
        holder.tvUserPhone.setText(model.getPhone());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                AndroidUtil.passUserModelAsIntent(intent, model);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row, parent, false);
        return new UserModelViewHolder(view);
    }
    class UserModelViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        TextView tvUserPhone;
        ImageView ivUserPhoto;
        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_username_text);
            tvUserPhone = itemView.findViewById(R.id.tv_user_phone);
            ivUserPhoto = itemView.findViewById(R.id.iv_user_profile_pic);
        }
    }
}
