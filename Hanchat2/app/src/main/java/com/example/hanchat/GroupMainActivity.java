package com.example.hanchat;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanchat.module.RecyclerAdapter;

import java.util.ArrayList;

public class GroupMainActivity extends NavActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmain);

        ArrayList<String> list = new ArrayList<>();
        for(int i = 0;i < 100; i++){
            list.add(String.format("Text %d", i));
        }

        RecyclerView rv = findViewById(R.id.rv_grouppost);
        rv.setLayoutManager(new LinearLayoutManager(this));

        RecyclerAdapter adapter = new RecyclerAdapter(list);
        rv.setAdapter(adapter);
    }
}

