package com.example.hanchat.data;

import android.net.Uri;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.hanchat.databinding.ContentPostcommentBinding;
import com.example.hanchat.module.RecyclerAdapter;

public class PostComment implements RecyclerAdapter.RecyclerItem {
    Uri profileImage;
    String writer;
    String content;

    @Override
    public int getViewType() {
        return RecyclerAdapter.POSTCOMMENT;
    }

    @Override
    public void setRecyclerContent(final View itemView) {
        ContentPostcommentBinding binding = DataBindingUtil.bind(itemView);
        binding.setModel(this);
    }

    public void set(String writer, String content){
        this.writer = writer;
        this.content = content;
    }

    public String getWriter(){return writer;}
    public String getContent(){return content;}

}