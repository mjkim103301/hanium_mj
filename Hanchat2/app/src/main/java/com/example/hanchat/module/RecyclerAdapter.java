package com.example.hanchat.module;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanchat.R;

import java.util.ArrayList;



//T에 Recycleritem을 상속받는 데이터 클래스
public class RecyclerAdapter<T extends RecyclerAdapter.Recycleritem> extends RecyclerView.Adapter {

    private ArrayList<T> contents = new ArrayList<>();

    public interface Recycleritem{
        void setRecyclerContent(View itemView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setContent(T content){
            content.setRecyclerContent(itemView);
        }
    }

    public RecyclerAdapter(){
        super();
    }
    public RecyclerAdapter(ArrayList<T> list){
        super();
        addContent(list);
    }


    public void addContent(ArrayList<T> list){
        contents.addAll(list);
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.content_grouppost, parent, false);
        RecyclerAdapter.ViewHolder vh = new RecyclerAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        T content = contents.get(position);
        ((RecyclerAdapter.ViewHolder) holder).setContent(content);

        if(position > contents.size() - 8)
            req_add();
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public void req_add(){

    }
}
