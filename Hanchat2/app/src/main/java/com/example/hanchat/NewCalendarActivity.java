package com.example.hanchat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.hanchat.data.calendar.CalendarFragment;
import com.example.hanchat.data.calendar.Day;
import com.example.hanchat.data.calendar.Month;
import com.example.hanchat.module.GridAdapter;
import com.example.hanchat.module.ViewPagerAdapter;


import java.util.ArrayList;
import java.util.Calendar;

public class NewCalendarActivity extends AppCompatActivity {

    ViewPager viewPager;
    //ArrayList<Month> calendarList=new ArrayList<>();
    ViewPagerAdapter pagerAdapter;

    TextView tv_calendarBar;
    GridView gridView;
    Button btn_today;

CalendarFragment calendarFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calendar);

        //adapter=new RecyclerAdapter();
        tv_calendarBar=(TextView)findViewById(R.id.tv_calendarBar);
        gridView=(GridView)findViewById(R.id.gridView);
        viewPager=(ViewPager) findViewById(R.id.viewPager_month);
        pagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);
        btn_today=(Button)findViewById(R.id.btn_today);
        for(int i=-25; i<25; i++){

            CalendarFragment fragment=new CalendarFragment(i);
           pagerAdapter.addItem(fragment);

        }

        pagerAdapter.notifyDataSetChanged();
        int year=((CalendarFragment)pagerAdapter.getItem(25)).month.year;
        int month=((CalendarFragment)pagerAdapter.getItem(25)).month.month;
        tv_calendarBar.setText(year+"년 "+(month+1)+"월");

        btn_today.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(25);
                int today= ((CalendarFragment)pagerAdapter.getItem(25)).getDay();
             // (((CalendarFragment)pagerAdapter.getItem(25)).gridAdapter.getToday(today).getHolder()).dayItem.setBackgroundResource(R.drawable.border);//getToday에서 NullPointException 발생

            }
        });

        btn_today.performClick();//today 버튼 강제클릭 코드: 위치 바꾸지 마!!

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int year=((CalendarFragment)pagerAdapter.getItem(position)).getYear();
                int month=((CalendarFragment)pagerAdapter.getItem(position)).getMonth()+1;
                tv_calendarBar.setText(year+"년 "+month+"월");

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }



}
