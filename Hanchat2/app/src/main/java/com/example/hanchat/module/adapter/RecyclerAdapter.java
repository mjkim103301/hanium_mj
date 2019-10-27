package com.example.hanchat.module.adapter;

import androidx.annotation.LayoutRes;

import com.example.hanchat.R;

public class RecyclerAdapter<T extends RecyclerManager.RecyclerItem> extends RecyclerManager {

    public static final int EMPTY = 0;
    public static final int GROUPPOST = 1;
    public static final int POSTCOMMENT = 2;
    public static final int OTHERCHATTING = 3;
    public static final int USERCHATTING = 4;

    public static final int DayCalendar=5;
    public static final int MonthCalendar=6;

    @Override
    public int getLayoutRes(int viewType) {
        @LayoutRes int LayoutResId = R.layout.rcycler_item_emptyview;
        switch (viewType) {
            case EMPTY:
                LayoutResId = R.layout.rcycler_item_emptyview;
                break;
            case GROUPPOST:
                LayoutResId = R.layout.rcycler_item_grouppost;
                break;
            case POSTCOMMENT:
                LayoutResId = R.layout.rcycler_item_postcomment;
                break;
            case OTHERCHATTING:
                LayoutResId = R.layout.rcycler_item_chat_other;
                break;
            case USERCHATTING:
                LayoutResId = R.layout.rcycler_item_chat_user;
                break;

            case DayCalendar:
                LayoutResId=R.layout.recycler_calendar_item;
                break;
            case MonthCalendar:
                LayoutResId=R.layout.recycler_recycler_calendar;
                break;
        }

        return LayoutResId;
    }
}
