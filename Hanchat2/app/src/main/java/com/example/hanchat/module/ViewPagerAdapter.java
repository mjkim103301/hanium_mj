package com.example.hanchat.module;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.hanchat.data.calendar.CalendarFragment;
import com.example.hanchat.data.calendar.Month;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int numOfPage;
    public ViewPagerAdapter(@NonNull FragmentManager fm, int numOfPage) {
        super(fm, numOfPage);
        this.numOfPage=numOfPage;
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        CalendarFragment fragment=new CalendarFragment();
        return fragment;
    }

    @Override
    public int getCount() {
        return numOfPage;
    }
}
