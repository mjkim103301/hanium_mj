package com.example.hanchat.ui.group.grouppostlist;

import android.os.Parcelable;

import com.example.hanchat.data.group.GroupPost;
import com.example.hanchat.ui.RestoreRecyclerViewModel;

public class GroupPostlistViewModel extends RestoreRecyclerViewModel {
    // TODO: Implement the ViewModel

    int position = 0;
    Parcelable p = null;

    public GroupPostlistViewModel() {
        super();
        addData();
    }


    void addData(){
        int size = recyclerList.size();
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
            recyclerList.add(gp);
            if(i % 3 == 0){
                gp.set(String.format("Group %d", 20 - i), String.format("Writer %d", i),
                        String.format("Content %d", i * 10));
            }
        }
        if(adapter != null)
            adapter.notifyItemChanged(size);

    }
}
