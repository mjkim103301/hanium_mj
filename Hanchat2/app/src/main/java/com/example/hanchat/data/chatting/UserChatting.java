package com.example.hanchat.data.chatting;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.hanchat.databinding.RcyclerItemChatUserBinding;
import com.example.hanchat.module.adapter.RecyclerAdapter;

public class UserChatting extends Chatting {
    @Override
    public int getViewType() {
        return RecyclerAdapter.USERCHATTING;
    }

    public UserChatting(String str) {
        super(str);
    }

    @Override
    public void setRecyclerContent(View itemView) {
        super.setRecyclerContent(itemView);
        RcyclerItemChatUserBinding binding = DataBindingUtil.bind(itemView);
        binding.setModel(this);
    }

    public UserChatting() {
        super();
    }
}
