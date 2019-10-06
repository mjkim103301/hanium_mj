package com.example.hanchat.data;

import android.view.View;

import com.example.hanchat.module.RecyclerAdapter;
import com.example.hanchat.module.RecyclerManager;

public class EmptyData implements RecyclerManager.RecyclerItem {
    @Override
    public int getViewType() {
        return RecyclerAdapter.EMPTY;
    }

    @Override
    public void setRecyclerContent(View itemView) {

    }
}
