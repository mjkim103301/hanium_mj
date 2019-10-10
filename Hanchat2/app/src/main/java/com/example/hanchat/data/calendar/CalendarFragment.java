package com.example.hanchat.data.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.hanchat.R;

public class CalendarFragment extends Fragment {
    Month month;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        month=new Month();
        month.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.recycler_recycler_calendar, container, false);
    }

}
