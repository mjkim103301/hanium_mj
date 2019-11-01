package com.example.hanchat.module.connecter;


import android.graphics.Bitmap;

import androidx.annotation.StringRes;

import com.example.hanchat.module.AccountManager;

import org.json.JSONObject;


/*
    pid와 logintoken을 자동으로 같이 보내주는 커넥터

 */
public class AuthHttpConnecter {

    final private HttpConnecter baseConnecter;

    public AuthHttpConnecter(HttpConnecter baseConnecter){
        this.baseConnecter = baseConnecter;
    }

    private void putAuthData(JSONObject data){
        AccountManager am = AccountManager.getInstance();
        try {
            data.put("pid", am.getPid());
            data.put("logintoken", am.getLoginToken());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendImage(@StringRes int Pathname, JSONObject data, Bitmap bitmap, HttpConnecter.ResponseRecivedCallback responseRecivedCallback) {
        putAuthData(data);
        baseConnecter.sendImage(Pathname, data, bitmap, responseRecivedCallback);
    }
    public void sendImage(String Pathname, JSONObject data, Bitmap bitmap, HttpConnecter.ResponseRecivedCallback responseRecivedCallback) {
        putAuthData(data);
        baseConnecter.sendImage(Pathname, data, bitmap, responseRecivedCallback);
    }

    public void Post(@StringRes int Pathname, JSONObject data, HttpConnecter.ResponseRecivedCallback responseRecivedCallback) {
        putAuthData(data);
        baseConnecter.Post(Pathname, data, responseRecivedCallback);
    }
    public void Post(String Pathname, JSONObject data, HttpConnecter.ResponseRecivedCallback responseRecivedCallback) {
        putAuthData(data);
        baseConnecter.Post(Pathname, data, responseRecivedCallback);
    }
}
