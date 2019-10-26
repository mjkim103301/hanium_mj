package com.example.hanchat.ui.calendar;

import android.content.Context;

import com.example.hanchat.module.ViewPagerAdapter;

import java.util.ArrayList;

import androidx.lifecycle.ViewModel;

public class CalendarViewModel extends ViewModel {//다른 프레그먼트 갔다가 되돌아왔을때 정보 저장되어 있도록 만들기
    // TODO: Implement the ViewModel

    protected ArrayList<CalendarFragment> calendarList = new ArrayList<>();
    protected ViewPagerAdapter pagerAdapter;

//    public SavingViewStateViewPager(Context context){
//
//    }





}
