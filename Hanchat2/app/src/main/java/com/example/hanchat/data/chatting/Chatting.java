package com.example.hanchat.data.chatting;

import android.net.Uri;
import android.view.View;

import com.example.hanchat.data.VisibilityMutableItem;

public abstract class Chatting extends VisibilityMutableItem {
    Uri profileImage = null;
    String chat = "";

    public Chatting() {
        super();
    }
    public Chatting(String str) {
        super();
        chat = str;
    }

    @Override
    public void setRecyclerContent(View itemView) {
        super.setRecyclerContent(itemView);
    }

    public String getChat(){return chat;}
}
