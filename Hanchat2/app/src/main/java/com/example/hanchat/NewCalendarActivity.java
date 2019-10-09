package com.example.hanchat;

import android.os.Bundle;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.hanchat.data.calendar.Month;
import com.example.hanchat.module.RecyclerAdapter;
import com.example.hanchat.module.ViewPagerAdapter;

import java.util.ArrayList;

public class NewCalendarActivity extends AppCompatActivity {
    //RecyclerAdapter<Month> adapter;
    ViewPagerAdapter<Month> pagerAdapter;
    ViewPager viewPager;
    ArrayList<Month> calendarList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calendar);
        //adapter=new RecyclerAdapter();

        viewPager=(ViewPager) findViewById(R.id.viewPager_month);

        pagerAdapter=new ViewPagerAdapter(this);


        for (int i=0;i<50;i++){
            calendarList.add(new Month(this));
        }
        //adapter.addItem(calendarList);
        pagerAdapter.addItem(calendarList);

        viewPager.setAdapter(pagerAdapter);
//      RecyclerView view=findViewById(R.id.viewPager_month);
//      view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
//      view.setAdapter(adapter);



    }



}
