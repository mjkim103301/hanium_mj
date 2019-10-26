package com.example.hanchat.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hanchat.module.account.LoginProcess;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;


public class AccountManager {
    public interface AccountSetting {
        void setAccount(JSONObject json);
    }

    private static AccountManager instance = null;
    final private String prefId = "AccountPref";
    private String pid = "0";

    public String getPid(){
        return pid;
    }

    private AccountManager(Context context){
        SharedPreferences pref = context.getSharedPreferences(prefId, Context.MODE_PRIVATE);
        if(!pref.contains("id")){

            return;
        }
        String id = pref.getString("id", null);
        String pwd = pref.getString("password", null);

        login(id, pwd, context);
    }

    public static AccountManager getInstance(Context context){
        if(instance == null)
            instance = new AccountManager(context);
        return instance;
    }

    private void login(String id, String encryptedPassword, Context context){
        LoginProcess.login(id, encryptedPassword, context, new AccountSetting(){
            @Override
            public void setAccount(JSONObject json) {
                try {
                    pid = (String)json.get("pid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void newLogin(String id, String password, Context context){
        String encryptedpassword = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            encryptedpassword = bytesToHex(digest.digest(password.getBytes()));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        login(id, encryptedpassword, context);

        SharedPreferences.Editor editor = context.getSharedPreferences(prefId, Context.MODE_PRIVATE).edit();

        editor.putString("id", id);
        editor.putString("password", encryptedpassword);
        editor.commit();

    }

    public static String bytesToHex(byte[] bytes) {
            StringBuilder builder = new StringBuilder();
            for (byte b: bytes) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        }

}
