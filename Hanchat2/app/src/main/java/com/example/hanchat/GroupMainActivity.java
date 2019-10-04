package com.example.hanchat;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupMainActivity extends NavActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmain);

        ArrayList<String> list = new ArrayList<>();
        for(int i = 0;i < 100; i++){
            list.add(String.format("Text %d", i));
        }

        RecyclerView rv = findViewById(R.id.rv_grouppost);
        rv.setLayoutManager(new LinearLayoutManager(this));

        RecyclerAdapter adapter = new RecyclerAdapter(list);
        rv.setAdapter(adapter);
    }
}

class RecyclerAdapter extends RecyclerView.Adapter {

    private ArrayList<String> contents = null;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }


    public RecyclerAdapter(ArrayList<String> list) {
        super();
        contents = list;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_item, parent, false);
        RecyclerAdapter.ViewHolder vh = new RecyclerAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String text = contents.get(position);
        ((RecyclerAdapter.ViewHolder) holder).tv_content.setText(text);
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }
}
