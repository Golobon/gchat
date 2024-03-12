package com.golobon.gchat.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireBaseUtil {
     public static DocumentReference currentUserDetails() {
         return FirebaseFirestore
                 .getInstance()
                 .collection("users")
                 .document(currentUserId());
     }
     public static boolean isLoggedIn () {
         if (currentUserId() != null) {
             return true;
         }
         return false;
     }
    public static String currentUserId() {
        return FirebaseAuth
                .getInstance()
                .getUid();
    }
}
