package com.example.hanchat.data;

import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.example.hanchat.databinding.RcyclerItemEmptyviewBinding;
import com.example.hanchat.module.adapter.RecyclerAdapter;
import com.example.hanchat.module.adapter.RecyclerManager;

public class EmptyData implements RecyclerManager.RecyclerItem {
    int width = 300;
    int height = 300;
    String text;
    boolean progressing = false;

    @Override
    public int getViewType() {
        return RecyclerAdapter.EMPTY;
    }

    @Override
    public void setRecyclerContent(View itemView) {
        RcyclerItemEmptyviewBinding binding = DataBindingUtil.bind(itemView);
        binding.setModel(this);
    }


    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(View view, int height){
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = (int)height;
        view.setLayoutParams(params);
    }


    public String getText(){
        return text;
    }
}
