package com.example.hanchat.data.calendar;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import java.util.Calendar;

import com.example.hanchat.databinding.RecyclerCalendarItemBinding;
import com.example.hanchat.module.RecyclerAdapter;

public class Day implements RecyclerAdapter.RecyclerItem {


    int day=0;
    public Day(int day){
        this.day=day;
    }


    public String getDay(){
        return String.valueOf(day);
    }

    @Override
    public int getViewType() {
        return RecyclerAdapter.DayCalendar;
    }

    @Override
    public void setRecyclerContent(View itemView) {
        RecyclerCalendarItemBinding binding= DataBindingUtil.bind(itemView);
        binding.setModelCalendar(this);

    }
}
