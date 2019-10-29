package com.example.hanchat.data.group;

import android.net.Uri;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.hanchat.databinding.RcyclerItemPostcommentBinding;
import com.example.hanchat.module.adapter.RecyclerAdapter;

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
        RcyclerItemPostcommentBinding binding = DataBindingUtil.bind(itemView);
        binding.setModel(this);
    }

    public void set(String writer, String content){
        this.writer = writer;
        this.content = content;
    }

    public String getWriter(){return writer;}
    public String getContent(){return content;}

}