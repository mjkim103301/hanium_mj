package com.example.hanchat.module;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.hanchat.data.calendar.CalendarFragment;
import com.example.hanchat.data.calendar.Month;

import java.util.ArrayList;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    //int numOfPage;
    private ArrayList<Fragment> fragments=new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        //this.numOfPage=numOfPage;
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
       // CalendarFragment fragment=new CalendarFragment();
        return fragments.get(position);
    }

    @Override
    public int getCount() {

        return  fragments.size();
    }

    public void addItem(Fragment fragment){
        fragments.add(fragment);
    }
}
