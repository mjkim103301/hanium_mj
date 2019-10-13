package com.example.hanchat.ui.module;

import android.os.Parcelable;

import androidx.lifecycle.ViewModel;

import com.example.hanchat.module.RecyclerAdapter;
import com.example.hanchat.module.RecyclerManager;

import java.util.ArrayList;

public abstract class RestoreRecyclerViewModel extends ViewModel {
    private Parcelable state = null;
    protected ArrayList<RecyclerManager.RecyclerItem> recyclerList = new ArrayList<>();
    protected RecyclerAdapter adapter;

    public void setRecyclerAdapter(RecyclerAdapter recyclerAdapter){
        this.adapter = recyclerAdapter;
        recyclerAdapter.setList(recyclerList);
        if(state != null){
            recyclerAdapter.getParentView().getLayoutManager(). onRestoreInstanceState(state);
        }
    }

    public void saveState(){
        if(adapter != null)
            this.state = adapter.getParentView().getLayoutManager().onSaveInstanceState();
        adapter = null;
    }

    public void clear(){
        state = null;
        adapter = null;
        recyclerList.clear();
    }

    public ArrayList<RecyclerManager.RecyclerItem> getLIst(){
        return recyclerList;
    }

    public RecyclerAdapter getAdapter() {
        return adapter;
    }
}
