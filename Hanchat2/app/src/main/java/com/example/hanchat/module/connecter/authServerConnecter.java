package com.example.hanchat.module.connecter;

import android.content.SharedPreferences;

import com.example.hanchat.module.AccountManager;

public class authServerConnecter extends HttpConnecter {

    private long pid;
    private String Logintoken;
    protected authServerConnecter(String Host) {
        super(Host);
        AccountManager am = AccountManager.getInstance();
        pid = am.getPid();
        Logintoken = am.getLoginToken();
    }
}
