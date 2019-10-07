package com.example.hanchat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanchat.data.group.GroupPost;
import com.example.hanchat.module.RecyclerAdapter;
import com.example.hanchat.module.RecyclerManager;

import java.util.ArrayList;


public class GroupMainActivity extends NavActivity {
    RecyclerAdapter<GroupPost> adapter;

    GroupMainActivity getthis(){
        return this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmain);


        RecyclerView rv = findViewById(R.id.Rview_Groupmain);
        rv.setLayoutManager(new LinearLayoutManager(this));


        adapter = new RecyclerAdapter();
        adapter.setItemViewCreateAction(new RecyclerManager.ItemViewCreateAction() {
            @Override
            public void ItemViewCreated(final RecyclerManager.ViewHolder holder) {
                if(holder.getItem().getViewType() == RecyclerAdapter.GROUPPOST){
                   TextView tv_content = ((GroupPost)holder.getItem()).getBinding().ContentGroupPostContent;
                    tv_content.setMaxLines(8);
                    tv_content.setEllipsize(TextUtils.TruncateAt.END);
                    tv_content.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            TextView tv = ((GroupPost)holder.getItem()).getBinding().ContentGroupPostContent;
                            if(tv.getLayout() == null) return false;
                            int ellipseCount = tv.getLayout().getEllipsisCount(tv.getLineCount() - 1);
                            if(ellipseCount > 0){
                                ((GroupPost)holder.getItem()).getBinding().ContentMore.setVisibility(View.VISIBLE);
                            }
                            else{
                                ((GroupPost)holder.getItem()).getBinding().ContentMore.setVisibility(View.GONE);
                            }
                            //tv_content.getViewTreeObserver().removeOnPreDrawListener(this);
                            return true;
                        }
                    });
                }
            }
        });

        adapter.setItemViewBindAction(new RecyclerManager.ItemViewBindAction() {
            @Override
            public void ItemViewBinded(RecyclerManager.ViewHolder holder, final RecyclerManager.RecyclerItem item) {
                if(item.getViewType() == RecyclerAdapter.GROUPPOST){
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
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
            }
        });

        adapter.setLastPositionAction(new RecyclerManager.LastPositionAction() {
            @Override
            public void lastPositionFunc(RecyclerManager adapter) {
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
                    //String.format("Content %d", i * 10));
                    "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh");
            list.add(gp);
            if(i % 3 == 0){
                gp.set(String.format("Group %d", 20 - i), String.format("Writer %d", i),
                        String.format("Content %d", i * 10));
            }
        }
        adapter.addItem(list);
    }
}

