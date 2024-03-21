package com.golobon.gchat.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.golobon.gchat.model.UserModel;
import com.google.firebase.firestore.auth.User;

public class AndroidUtil {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    public static void passUserModelAsIntent(Intent intent, UserModel userModel) {
        intent.putExtra("username", userModel.getUsername());
        intent.putExtra("userPhone", userModel.getPhone());
        intent.putExtra("userId", userModel.getUserId());
    }
    public static UserModel getUserModelFromIntent(Intent intent) {
        UserModel otherUser = new UserModel();
        otherUser.setUsername(intent.getStringExtra("username"));
        otherUser.setPhone(intent.getStringExtra("userPhone"));
        otherUser.setUserId(intent.getStringExtra("userId"));
        return otherUser;
    }
}
