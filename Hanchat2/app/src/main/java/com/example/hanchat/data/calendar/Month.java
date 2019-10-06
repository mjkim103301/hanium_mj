package com.example.hanchat.data.calendar;

import android.content.Context;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanchat.NewCalendarActivity;
import com.example.hanchat.databinding.RecyclerRecyclerCalendarBinding;
import com.example.hanchat.module.RecyclerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Month implements RecyclerAdapter.RecyclerItem{

    ArrayList<Day> calendarList=new ArrayList<>();
    Calendar now=Calendar.getInstance();//calendar  객체 얻어오기
    Calendar cal=new GregorianCalendar();//gregorianCalendar 생성
    int year=now.get(Calendar.YEAR);//올해
    int month=now.get(Calendar.MONTH);//이번달
    int day=now.get(Calendar.DATE);//오늘
    int dayOfWeek=now.get(Calendar.DAY_OF_WEEK);//오늘이 이번주의 몇번째 날인지 알려줌
    int lastDay=now.getActualMaximum(Calendar.DATE);//해당 월의 마지막 일 반환
    Context context1;
    public Month(Context context){
        context1=context;
        setCalendarList();


    }
    public void setCalendarList(){//GregorianCalendar cal){
        for(int i=1;i<=lastDay;i++){
            calendarList.add(new Day(i));
        }

    }
    public ArrayList<Day> getCalendarList(){
        return calendarList;
    }


    @Override
    public int getViewType() {
        return RecyclerAdapter.MonthCalendar;
    }

    @Override
    public void setRecyclerContent(View itemView) {
        RecyclerRecyclerCalendarBinding binding= DataBindingUtil.bind(itemView);
        binding.setModel(this);
        RecyclerAdapter<Day> adapter=new RecyclerAdapter<>();
        binding.recyclerDay.setLayoutManager(new GridLayoutManager(context1, 7, GridLayoutManager.VERTICAL, false));

        binding.recyclerDay.setAdapter(adapter);
        adapter.addItem(calendarList);
    }
}
