package com.golobon.gchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.golobon.gchat.R;
import com.golobon.gchat.model.ChatMessageModel;
import com.golobon.gchat.utils.FireBaseUtil;

import java.nio.channels.GatheringByteChannel;

public class ChatRecyclerAdapter
        extends FirestoreRecyclerAdapter <ChatMessageModel, ChatRecyclerAdapter.ChatModelViewHolder> {
    Context context;
    public ChatRecyclerAdapter(
            @NonNull FirestoreRecyclerOptions<ChatMessageModel> options, Context context) {
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position, @NonNull ChatMessageModel model) {
        if (model.getSenderId().equals(FireBaseUtil.currentUserId())) {
            holder.llSender.setVisibility(View.GONE);
            holder.llReceiver.setVisibility(View.VISIBLE);
            holder.tvReceiver.setText(model.getMessage());
        } else {
            holder.llReceiver.setVisibility(View.GONE);
            holder.llSender.setVisibility(View.VISIBLE);
            holder.tvSender.setText(model.getMessage());
        }
    }
    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_raw, parent, false);
        return new ChatModelViewHolder(view);
    }
    class ChatModelViewHolder extends RecyclerView.ViewHolder {
    LinearLayout llSender, llReceiver;
    TextView tvSender, tvReceiver;
        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);
            llSender = itemView.findViewById(R.id.ll_chat_sender);
            llReceiver = itemView.findViewById(R.id.ll_chat_receiver);
            tvSender = itemView.findViewById(R.id.tv_chat_sender);
            tvReceiver = itemView.findViewById(R.id.tv_chat_receiver);
        }
    }
}
