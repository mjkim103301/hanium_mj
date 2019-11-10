package com.example.hanchat.data.Schedule;

import com.example.hanchat.module.adapter.RecyclerManager;
import com.google.api.client.util.DateTime;

public abstract class ScheduleContent implements RecyclerManager.RecyclerItem {
    long unit_pid;
    long id;
    String title;
    String category;
    DateTime starttime;
    DateTime endtime;
    String place;
    String memo;

    // TODO: + 반복


    public ScheduleContent(long unit_pid, long id, String title, String category,
    DateTime starttime, DateTime endtime, String place, String memo) {
        this.unit_pid = unit_pid;
        this.id = id;
        this.title = title;
        this.category = category;
        this.starttime = starttime;
        this.endtime = endtime;
        this.place = place;
        this.memo = memo;
    }
}
