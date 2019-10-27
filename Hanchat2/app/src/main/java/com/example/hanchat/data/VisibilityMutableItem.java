package com.example.hanchat.data;

import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hanchat.module.adapter.RecyclerManager;

public abstract class VisibilityMutableItem implements RecyclerManager.RecyclerItem {
    boolean isvisible = true;

    public void setVisibility(boolean visibility){
        this.isvisible = visibility;
    }

    @Override
    public void setRecyclerContent(View itemView) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        if(isvisible){
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            itemView.setVisibility(View.VISIBLE);
        }
        else{
            params.height = 0;
            params.width = 0;
            itemView.setVisibility(View.GONE);
        }
    }

    public void setVisible(boolean isvisible){this.isvisible = isvisible;}
}
