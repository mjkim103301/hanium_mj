package com.example.hanchat.data.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.example.hanchat.R;

public class CalendarFragment extends Fragment {
    Month month=new Month();
    GridView gridView;//그리드 뷰
    Month.GridAdapter gridAdapter;//그리드 어댑터
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recycler_recycler_calendar, container, false);
        gridView=(GridView) view.findViewById(R.id.gridView);
        month.setCalendarList();//날짜를 세팅해준다.

        gridAdapter=new Month.GridAdapter(getArguments(), month.getCalendarList());

        gridView.setAdapter(gridAdapter);



        return view;
    }

}
