package com.example.hanchat;

import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;

import com.example.hanchat.data.calendar.CalendarFragment;
import com.example.hanchat.data.calendar.Month;
import com.example.hanchat.module.ViewPagerAdapter;


import java.util.ArrayList;

public class NewCalendarActivity extends AppCompatActivity {

    ViewPager viewPager;
    //ArrayList<Month> calendarList=new ArrayList<>();
    ViewPagerAdapter pagerAdapter;

    GridView gridView;
    int month;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calendar);

        //adapter=new RecyclerAdapter();
        gridView=(GridView)findViewById(R.id.gridView);
        viewPager=(ViewPager) findViewById(R.id.viewPager_month);
        pagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        for(month=-25;month<25;month++){

            CalendarFragment fragment=new CalendarFragment();
           pagerAdapter.addItem(fragment);
        }
        pagerAdapter.notifyDataSetChanged();
        //viewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener) gridView);






    }
    public int setMonth(){
        return 0;
    }


}
