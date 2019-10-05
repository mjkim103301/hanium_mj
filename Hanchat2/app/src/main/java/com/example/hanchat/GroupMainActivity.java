package com.example.hanchat;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanchat.data.GroupPost;
import com.example.hanchat.module.RecyclerAdapter;

import java.util.ArrayList;


public class GroupMainActivity extends NavActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmain);

        ArrayList<GroupPost> list = new ArrayList<>();

        for(int i = 0;i < 20; i++){
            GroupPost gp = new GroupPost();
            gp.set(String.format("Group %d", 20 - i), String.format("Writer %d", i),
                    "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                            "dddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                            "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                            "ddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                            "ddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                            "ddddddddddddddddddddddddddddddddddd");//String.format("Content %d", i * 10));
            list.add(gp);
        }

        RecyclerView rv = findViewById(R.id.Rview_Groupmain);
        rv.setLayoutManager(new LinearLayoutManager(this));


        RecyclerAdapter<GroupPost> adapter = new RecyclerAdapter<>();
        adapter.addContent(list);
        rv.setAdapter(adapter);

    }
}

