package com.example.mycalendar.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.example.mycalendar.TSLiveData;

public class CalendarHeaderViewModel extends ViewModel {
    public TSLiveData<Long> mHeaderDate = new TSLiveData<>();

    public void setHeaderDate(long headerDate) {
        this.mHeaderDate.setValue(headerDate);
    }
}
