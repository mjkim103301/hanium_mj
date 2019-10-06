package com.example.hanchat;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanchat.data.GroupPost;
import com.example.hanchat.data.PostComment;
import com.example.hanchat.module.RecyclerAdapter;
import com.example.hanchat.module.RecyclerManager;

import java.util.ArrayList;

public class GroupPostActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouppost);

        ArrayList<RecyclerManager.RecyclerItem> list = new ArrayList<>();

        Intent intent = getIntent();
        GroupPost post = new GroupPost(this);
        post.set(intent.getExtras().getString("GroupName"),
                intent.getExtras().getString("WriterName"),
                intent.getExtras().getString("Content"));

        list.add(post);
        for(int i =0; i < 30; i++){
            PostComment pc = new PostComment();
            pc.set("writer " + i, "content" + i * 10);
            list.add(pc);
        }

        RecyclerView rv = findViewById(R.id.Rview_GroupPost);
        RecyclerManager adapter = new RecyclerAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter.addContent(list);
        rv.setAdapter(adapter);
    }
}