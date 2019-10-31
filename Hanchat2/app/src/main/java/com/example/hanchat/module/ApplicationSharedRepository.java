package com.example.hanchat.module;

import android.content.Context;

public final class ApplicationSharedRepository {
    private static Context appContext = null;


    public static void setAppContext(Context context){
        appContext = context;
    }

    public static Context getAppContext(){
        return appContext;
    }
}
