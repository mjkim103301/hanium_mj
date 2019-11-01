package com.example.hanchat.module;

import android.content.Context;

/*
    앱에서 공유하는 정보
 */
public final class ApplicationSharedRepository {
    private static Context appContext = null;
    public static void setAppContext(Context context){
        appContext = context;
    }

    public static Context getAppContext(){
        return appContext;
    }
}
