package com.example.hanchat.module;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarAdapter extends RecyclerView.Adapter {

    public class ViewHolder {

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성.
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {//position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.

    }

    @Override
    public int getItemCount() {//전체 아이템 갯수 리턴.
        return 0;
    }
}
