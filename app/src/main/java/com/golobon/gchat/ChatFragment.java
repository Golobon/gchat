package com.golobon.gchat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChatFragment extends Fragment {
    RecyclerView rvChatList;
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

    private void setupRecyclerView() {
    }
}