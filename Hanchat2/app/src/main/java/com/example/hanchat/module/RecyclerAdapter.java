package com.example.hanchat.module;

import androidx.annotation.LayoutRes;

import com.example.hanchat.R;

public class RecyclerAdapter extends RecyclerManager {

    public static final int GROUPPOST = 1;
    public static final int POSTCOMMENT = 2;
    public static final int OTHERCHATTING = 3;
    public static final int USERCHATTING = 4;

    @Override
    public int getLayoutRes(int viewType) {
        @LayoutRes int LayoutResId = 0;
        switch (viewType) {
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
        }

        return LayoutResId;
    }
}
