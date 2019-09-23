package com.example.phonedbtest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.ContentHandler;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private ArrayList<String> mData=null;
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView1;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView1=itemView.findViewById(R.id.text1);
        }
    }
    RecyclerViewAdapter(ArrayList<String> list){
        mData=list;
    }
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context=viewGroup.getContext();
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.recyclerview_item, viewGroup, false);
        RecyclerViewAdapter.ViewHolder vh=new RecyclerViewAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int i) {
        String text=mData.get(i);
        viewHolder.textView1.setText(text);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


}
