package com.example.hanchat.ui;

import android.os.Parcelable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.example.hanchat.module.adapter.RecyclerAdapter;
import com.example.hanchat.module.adapter.RecyclerManager;

import java.util.ArrayList;

/*
    리사이클러뷰의 정보를 저장하는 뷰모델
    1. 리사이클러뷰에 담길 아이템들의 정보 저장
    2. 리사이클러뷰 스크롤 위치와 데이터 복원
 */
public abstract class RestoreRecyclerViewModel extends ViewModel {
    private Parcelable state = null;
    protected ArrayList<RecyclerManager.RecyclerItem> recyclerList = new ArrayList<>();
    protected RecyclerAdapter adapter;

    //onActivityCreate에 할것
    public void setRecyclerAdapter(RecyclerAdapter recyclerAdapter){
        this.adapter = recyclerAdapter;
        recyclerAdapter.setList(recyclerList);
        if(state != null){
            recyclerAdapter.getParentView().getLayoutManager().onRestoreInstanceState(state);
        }
    }

    public void setRecyclerAdapterWithParentView(RestoreRecyclerFragment fragment, final RecyclerAdapter recyclerAdapter){
        setRecyclerAdapter(recyclerAdapter);
        fragment.setRestoreRecycler(new RestoreRecyclerFragment.RestoreRecycler() {
            @Override
            public void onDestroyView() {
                saveState();
            }
        });
    }

    //onDestroyView에 할것
    public void saveState(){
        if(adapter != null)
            this.state = adapter.getParentView().getLayoutManager().onSaveInstanceState();
        adapter = null;
    }

    //정보 지우기
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
