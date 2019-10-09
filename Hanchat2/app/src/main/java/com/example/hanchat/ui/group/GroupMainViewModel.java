package com.example.hanchat.ui.group;

import androidx.lifecycle.ViewModel;

import com.example.hanchat.data.group.GroupPost;
import com.example.hanchat.module.RecyclerAdapter;

import java.util.ArrayList;

public class GroupMainViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private ArrayList<GroupPost> postLIst;

    public void setRecyclerAdapter(RecyclerAdapter ra){
        ra.setList(postLIst);
    }


    int addData(){
        int size = postLIst.size();
        for(int i = 0;i < 20; i++){
            GroupPost gp = new GroupPost();
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
            postLIst.add(gp);
            if(i % 3 == 0){
                gp.set(String.format("Group %d", 20 - i), String.format("Writer %d", i),
                        String.format("Content %d", i * 10));
            }
        }
        return size;
    }
}
