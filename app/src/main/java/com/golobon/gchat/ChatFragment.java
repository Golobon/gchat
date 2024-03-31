package com.golobon.gchat;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.golobon.gchat.adapter.ListChatRecyclerAdapter;
import com.golobon.gchat.adapter.SearchUserRecyclerAdapter;
import com.golobon.gchat.model.ChatroomModel;
import com.golobon.gchat.model.UserModel;
import com.golobon.gchat.utils.FireBaseUtil;
import com.google.firebase.firestore.Query;

public class ChatFragment extends Fragment {
    RecyclerView rvChatList;
    ListChatRecyclerAdapter adapter;
    public ChatFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        rvChatList = view.findViewById(R.id.rv_chat_list);
        setupRecyclerView();
        return view;
    }

    void setupRecyclerView() {
        Query query = FireBaseUtil.allChatroomCollectionReference()
                .whereArrayContains("userIds", FireBaseUtil.currentUserId())
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatroomModel> options =
                new FirestoreRecyclerOptions.Builder<ChatroomModel>()
                        .setQuery(query, ChatroomModel.class).build();

        adapter = new ListChatRecyclerAdapter(options, getContext());
        rvChatList.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
        rvChatList.setAdapter(adapter);
        adapter.startListening();
    }
    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
        }
    }
    static class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.d("LOG_TAG", "meet a IOOBE in RecyclerView");
                Log.d("LOG_TAG", e.toString());
            }
        }
    }
}