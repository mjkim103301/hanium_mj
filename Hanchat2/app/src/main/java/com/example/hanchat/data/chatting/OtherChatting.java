package com.example.hanchat.data.chatting;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.hanchat.databinding.RcyclerItemChatOtherBinding;
import com.example.hanchat.module.adapter.RecyclerAdapter;

public class OtherChatting extends Chatting {
    @Override
    public int getViewType() {
        return RecyclerAdapter.OTHERCHATTING;
    }

    public OtherChatting() {
        super();
    }

    @Override
    public void setRecyclerContent(View itemView) {
        super.setRecyclerContent(itemView);
        RcyclerItemChatOtherBinding binding = DataBindingUtil.bind(itemView);
        binding.setModel(this);
    }

    public OtherChatting(String str) {
        super(str);
    }
}
