package com.example.hanchat.data;

import android.net.Uri;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.hanchat.databinding.ChatBinding;
import com.example.hanchat.module.RecyclerAdapter;

abstract class Chatting implements RecyclerAdapter.RecyclerItem{
    Uri profileImage = null;
    String chat = "";

    @Override
    public void setRecyclerContent(View itemView) {
        ChatBinding binding = DataBindingUtil.bind(itemView);
        binding.setModel(this);
    }

    public String getChat(){return chat;}
}
