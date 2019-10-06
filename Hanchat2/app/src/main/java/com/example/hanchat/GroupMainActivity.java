package com.example.hanchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanchat.data.GroupPost;
import com.example.hanchat.module.RecyclerAdapter;

import java.util.ArrayList;


public class GroupMainActivity extends NavActivity {
    RecyclerAdapter<GroupPost> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmain);


        RecyclerView rv = findViewById(R.id.Rview_Groupmain);
        rv.setLayoutManager(new LinearLayoutManager(this));


        adapter = new RecyclerAdapter<>();
        adapter.setItemViewAction(new RecyclerAdapter.ItemViewAction() {
            @Override
            public void setItemView(final View itemView, final RecyclerAdapter.RecyclerItem item, final int viewType) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(GroupMainActivity.this, GroupPostActivity.class);
                        GroupPost post = (GroupPost)item;
                        intent.putExtra("GroupName", post.getGroupName());
                        intent.putExtra("WriterName", post.getWriterName());
                        intent.putExtra("Content", post.getContent());

                        startActivity(intent);
                    }
                });
            }
        });
        adapter.setLastPositionAction(new RecyclerAdapter.LastPositionAction() {
            @Override
            public void lastPositionFunc(RecyclerAdapter adapter) {
                addData();
            }
        });
        rv.setAdapter(adapter);

        addData();
    }

    private void addData(){

        ArrayList<GroupPost> list = new ArrayList<>();

        for(int i = 0;i < 20; i++){
            GroupPost gp = new GroupPost(this);
            gp.set(String.format("Group %d", 20 - i), String.format("Writer %d", i),
                    String.format("Content %d", i * 10));
            list.add(gp);
        }
        adapter.addContent(list);
    }
}

