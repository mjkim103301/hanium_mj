package com.example.hanchat;

import android.os.Bundle;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanchat.data.calendar.Month;
import com.example.hanchat.module.RecyclerAdapter;

import java.util.ArrayList;

public class NewCalendarActivity extends AppCompatActivity {
    RecyclerAdapter<Month> adapter;
    ArrayList<Month> calendarList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calendar);
        adapter=new RecyclerAdapter();

      RecyclerView view=findViewById(R.id.recycler_Month);
      view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
      view.setAdapter(adapter);


      for (int i=0;i<50;i++){
          calendarList.add(new Month(this));
      }
        adapter.addItem(calendarList);
    }



}
