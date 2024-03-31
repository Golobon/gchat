package com.golobon.gchat.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;

public class FireBaseUtil {
     public static DocumentReference currentUserDetails() {
         return allUserCollectionReference()
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
    public static CollectionReference allUserCollectionReference() {
        return FirebaseFirestore.getInstance()
                .collection("users");
    }
    public static DocumentReference getChatroomReference(String chatroomId) {
        return FirebaseFirestore.getInstance()
                .collection("chatrooms")
                .document(chatroomId);
    }
    public static String getChatroomId(String userId1, String userId2) {
        if (userId1.hashCode() < userId2.hashCode()) {
            return userId1 + "_" + userId2;
        } else return userId2 + "_" + userId1;
    }

    public static CollectionReference getChatroomMessageReference(String chatroomId) {
            return getChatroomReference(chatroomId).collection("chats");
    }

    public static CollectionReference allChatroomCollectionReference() {
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DocumentReference getOtherUserFromChatroom(List<String> userIds) {
         if (userIds.get(0).equals(FireBaseUtil.currentUserId())) {
             return allUserCollectionReference().document(userIds.get(1));
         } else return allUserCollectionReference().document(userIds.get(0));
    }
    public static String timestampToString(Timestamp timestamp) {
         return new SimpleDateFormat("HH:mm").format(timestamp.toDate());
    }
    public static void logOut() {
         FirebaseAuth.getInstance().signOut();
    }
}
