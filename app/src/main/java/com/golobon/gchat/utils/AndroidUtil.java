package com.golobon.gchat.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.golobon.gchat.model.UserModel;

public class AndroidUtil {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    public static void passUserModelAsIntent(Intent intent, UserModel userModel) {
        intent.putExtra("username", userModel.getUsername());
        intent.putExtra("userPhone", userModel.getPhone());
        intent.putExtra("userId", userModel.getUserId());
        intent.putExtra("fcmToken", userModel.getfcmToken());
    }
    public static UserModel getUserModelFromIntent(Intent intent) {
        UserModel otherUser = new UserModel();
        otherUser.setUsername(intent.getStringExtra("username"));
        otherUser.setPhone(intent.getStringExtra("userPhone"));
        otherUser.setUserId(intent.getStringExtra("userId"));
        otherUser.setfcmToken(intent.getStringExtra("fcmToken"));
        return otherUser;
    }
    public static void setProfilePic (Context context, Uri imageUri,
                                      ImageView imageView) {
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }
}
