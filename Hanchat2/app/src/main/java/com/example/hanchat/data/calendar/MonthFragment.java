package com.example.hanchat.data.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import com.example.hanchat.R;
import com.example.hanchat.module.GridAdapter;


import java.util.GregorianCalendar;

public class MonthFragment extends Fragment {
    public Month month=new Month();
    GridView gridView;//그리드 뷰
    public GridAdapter gridAdapter;//그리드 어댑터


    int real_month;
    public MonthFragment(int i){
        this.real_month=i;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.recycler_recycler_calendar, container, false);

        gridView=(GridView) view.findViewById(R.id.gridView);

        month.setCalendarList(real_month);//날짜를 세팅해준다.

        gridAdapter=new GridAdapter(getContext(), month.getCalendarList(), month.day,real_month );

        gridView.setAdapter(gridAdapter);



        return view;
    }
    public int getYear(){
        return month.cal.get(GregorianCalendar.YEAR);
    }//해당 년도
    public int getMonth(){
        return month.cal.get(GregorianCalendar.MONTH);
    }//해당 달

    public int getDay(){
        return month.day;
    }//오늘
//    public int getReal_month(){
//        return real_month;
//    }


}