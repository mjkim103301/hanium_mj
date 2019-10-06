package com.example.hanchat.data;

import android.view.View;

import com.example.hanchat.module.RecyclerAdapter;

public class UserChatting extends Chatting {
    @Override
    public int getViewType() {
        return RecyclerAdapter.OTHERCHATTING;
    }

}
