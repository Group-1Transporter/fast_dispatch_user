package com.transporteruser.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.transporteruser.bean.Message;
import com.transporteruser.databinding.ChatListBinding;

import java.util.ArrayList;

public class MessageShowAdapter extends RecyclerView.Adapter<MessageShowAdapter.MessageViewHolder> {
    String currentUserId;
    ArrayList<Message> messageList;
    public MessageShowAdapter(ArrayList<Message> messageList){
        this.messageList = messageList;
        currentUserId = FirebaseAuth.getInstance().getUid();
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatListBinding binding = ChatListBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new MessageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        if(currentUserId == message.getFrom()){
//            holder.binding.received.setVisibility(View.GONE);
//            holder.binding.sent.setVisibility(View.VISIBLE);
//            holder.binding.tvChatSent.setText(message.getMessage());
            holder.binding.ll1.setVisibility(View.GONE);
            holder.binding.senderTime.setVisibility(View.GONE);
            holder.binding.senderMsg.setVisibility(View.GONE);
            //binding.myTime.setText(message.getDate()+" "+message.getTime());
           holder.binding.myMsg.setText(message.getMessage());
        }else if(currentUserId != message.getFrom()){
//            holder.binding.received.setVisibility(View.VISIBLE);
//            holder.binding.sent.setVisibility(View.GONE);
//            holder.binding.tvChatReceived.setText(message.getMessage());
            holder.binding.ll2.setVisibility(View.GONE);
            holder.binding.myMsg.setVisibility(View.GONE);
            holder.binding.myTime.setVisibility(View.GONE);
            //binding.senderTime.setText(message.getDate()+" "+message.getTime());
            holder.binding.senderMsg.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        ChatListBinding binding;
        public MessageViewHolder(ChatListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
