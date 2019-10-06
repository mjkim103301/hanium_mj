package com.example.hanchat.data;

import android.net.Uri;
import android.view.View;

import com.example.hanchat.module.RecyclerAdapter;

public abstract class Chatting implements RecyclerAdapter.RecyclerItem{
    Uri profileImage = null;
    String chat = "";

    public Chatting() {
        super();
    }
    public Chatting(String str) {
        super();
        chat = str;
    }

    public String getChat(){return chat;}
}
