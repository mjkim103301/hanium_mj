package com.example.hanchat.data;

import android.view.View;

import com.example.hanchat.module.RecyclerAdapter;

public class OtherChatting extends Chatting {
    @Override
    public int getViewType() {
        return RecyclerAdapter.OTHERCHATTING;
    }

}
