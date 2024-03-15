package com.golobon.gchat.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.golobon.gchat.R;
import com.golobon.gchat.model.UserModel;
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


    }
    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_raw, parent, false);
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
            ivUserPhoto = itemView.findViewById(R.id.iv_user_photo);
        }
    }
}
