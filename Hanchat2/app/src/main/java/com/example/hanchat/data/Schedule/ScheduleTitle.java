package com.example.hanchat.data.Schedule;

import android.view.View;

import com.google.api.client.util.DateTime;

public class ScheduleTitle extends ScheduleContent {
    public ScheduleTitle(long unit_pid, long id, String title, String category, DateTime starttime, DateTime endtime, String place, String memo) {
        super(unit_pid, id, title, category, starttime, endtime, place, memo);
    }

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public void setRecyclerContent(View itemView) {

    }
}
