package com.example.hanchat.data.calendar;

import com.example.hanchat.module.GridAdapter;

public class Day  {
    GridAdapter.ViewHolder holder;
    int day=0;
    public Day(int day){
        this.day=day;
    }
    public String getDay(){
        return String.valueOf(day);
    }

    public void h(GridAdapter.ViewHolder v){
        this.holder =v;
    }

    public GridAdapter.ViewHolder getHolder(){
        return holder;
    }

}
