package com.example.hanchat.ui.group.grouppost;

import com.example.hanchat.data.group.GroupPost;
import com.example.hanchat.data.group.PostComment;
import com.example.hanchat.module.adapter.RecyclerAdapter;
import com.example.hanchat.ui.RestoreRecyclerViewModel;

public class GroupPostViewModel extends RestoreRecyclerViewModel {
    // TODO: Implement the ViewModel

    public void setValue(String groupName, String writerName, String content){
        GroupPost targetPost = new GroupPost();
        targetPost.set(groupName, writerName, content);
        recyclerList.add(targetPost);
        addData();
    }

    @Override
    public void setRecyclerAdapter(RecyclerAdapter recyclerAdapter) {
        super.setRecyclerAdapter(recyclerAdapter);

    }

    public void addData(){
        int size = recyclerList.size();
        for(int i =0; i < 30; i++){
            PostComment pc = new PostComment();
            pc.set("writer " + i, "content" + i * 10);
            recyclerList.add(pc);
        }
        if(adapter != null)
            adapter.notifyItemChanged(size);
    }
}
