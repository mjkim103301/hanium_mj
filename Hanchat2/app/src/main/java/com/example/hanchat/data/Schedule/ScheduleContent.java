package com.example.hanchat.data.Schedule;

import com.example.hanchat.module.adapter.RecyclerManager;

import java.util.Date;

public abstract class ScheduleContent implements RecyclerManager.RecyclerItem {
    long unit_pid;
    long id;
    String title;
    String category;
    Date starttime;
    Date endtime;
    String place;
    String memo;

    // TODO: + 반복


    public ScheduleContent(long unit_pid, long id, String title, String category,
    Date starttime, Date endtime, String place, String memo) {
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
