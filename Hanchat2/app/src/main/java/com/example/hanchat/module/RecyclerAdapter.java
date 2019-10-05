package com.example.hanchat.module;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanchat.R;

import java.util.ArrayList;



//T에 Recycleritem을 상속받는 데이터 클래스
public class RecyclerAdapter<T extends RecyclerAdapter.RecyclerItem> extends RecyclerView.Adapter {

    private ArrayList<T> items = new ArrayList<>();
    public static final int GROUPPOST = 0;
    public static final int POSTCOMMENT = 1;

    ItemFunction func = null;

    public interface RecyclerItem {
        int getViewType();
        void setRecyclerContent(final View itemView);
    }

    public interface ItemFunction {
        void setView(final View itemView, final RecyclerItem item, final int viewType);
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



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @LayoutRes int LayoutResId = 0;
        switch (viewType){
            case GROUPPOST:
                LayoutResId = R.layout.content_grouppost;
                break;
            case POSTCOMMENT:
                LayoutResId = R.layout.content_postcomment;
        }
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(LayoutResId, parent, false);


        RecyclerAdapter.ViewHolder vh = new RecyclerAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        T content = items.get(position);
        ((RecyclerAdapter.ViewHolder) holder).setContent(content);
        if(func != null){
            func.setView(((ViewHolder)holder).itemView, content, getItemViewType(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    public void setViewFunc(ItemFunction func){this.func = func;}

    public void req_add(){

    }


    public void addContent(ArrayList<T> list){
        items.addAll(list);
    }

}
