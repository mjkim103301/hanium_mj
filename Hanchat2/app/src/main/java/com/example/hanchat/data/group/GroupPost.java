package com.example.hanchat.data.group;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.example.hanchat.databinding.RcyclerItemGrouppostBinding;
import com.example.hanchat.module.RecyclerAdapter;

public class GroupPost implements RecyclerAdapter.RecyclerItem {
    @Override
    public int getViewType() {
        return RecyclerAdapter.GROUPPOST;
    }

    RcyclerItemGrouppostBinding binding;
    Uri groupProfileImage = null;
    String groupName;
    String writerName;
    String content;

    Context context;

    final int maxLInes = 5;

    public GroupPost(Context context) {
        super();
        this.context = context;
    }

    public RcyclerItemGrouppostBinding getBinding(){
        return binding;
    }

    @Override
    public void setRecyclerContent(final View itemView) {
        binding = DataBindingUtil.bind(itemView);
        binding.setModel(this);
    }

    public void setGroupProfileImage(Uri imagepath){
        groupProfileImage = imagepath;
    }

    public void set(String groupName, String writerName, String content){
        this.groupName = groupName;
        this.writerName = writerName;
        this.content = content;
    }

    public String getGroupName(){return groupName;}
    public String getWriterName(){return writerName;}
    public String getContent(){return content;}

}
