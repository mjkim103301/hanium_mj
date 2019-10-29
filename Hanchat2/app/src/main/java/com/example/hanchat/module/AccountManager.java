package com.example.hanchat.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hanchat.module.account.CreateAccountProcess;
import com.example.hanchat.module.account.LoginProcess;
import com.example.hanchat.module.account.SignUpProcess;

import org.json.JSONException;
import org.json.JSONObject;


public class AccountManager {
    public interface Callback {
        void setAccount(JSONObject json, int Resultno);
    }

    public final static int LOGIN_SUCCESS = 1;
    public final static int LOGIN_FAILED = -1;
    public final static int ACCOUNT_CREATE_SUCCESS = 2;
    public final static int ACCOUNT_CREATE_FAILED = -2;
    public final static int ACCOUNT_SIGNUP_SUCCESS = 3;
    public final static int ACCOUNT_SIGNUP_FAILED = -3;


    private static AccountManager instance = null;
    private final static String prefId = "AccountPref";
    SharedPreferences pref = null;
    private Long pid = 0L;
    private String loginToken = "";

    public Long getPid(){
        return pid;
    }
    public String getLoginToken() {
        return loginToken;
    }

    private AccountManager(Context context){
        pref = context.getSharedPreferences(prefId, Context.MODE_PRIVATE);
    }

    public static AccountManager getInstance(Context context){
        if(instance == null)
            instance = new AccountManager(context);
        return instance;
    }

    public void autoLogin(Context context, Callback callback){
        if(!pref.contains("id")){
            createUser(context, callback);
            return;
        }
        String id = pref.getString("id", null);
        String pwd = pref.getString("password", null);

        login(id, pwd, context, callback);
    }
    private void login(String id, String password, Context context, final Callback callback){
        LoginProcess.login(id, password, context, new Callback(){
            @Override
            public void setAccount(JSONObject json, int Resultno) {
                if(Resultno == AccountManager.LOGIN_SUCCESS) {
                    try {
                        loginToken = (String)json.getString("logintoken");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.setAccount(json, Resultno);
            }
        });
    }
    private void login(long pid, Context context, final Callback callback){
        LoginProcess.login(pid, context, new Callback() {
            @Override
            public void setAccount(JSONObject json, int Resultno) {
                if(Resultno == AccountManager.LOGIN_SUCCESS){
                    try{
                        loginToken = (String)json.getString("logintoken");
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                callback.setAccount(json, Resultno);
            }
        });
    }

    public void newLogin(final String id, final String password, final Context context, final Callback callback){
        login(id, password, context, new Callback() {
            @Override
            public void setAccount(JSONObject json, int Resultno) {
                if(Resultno == LOGIN_SUCCESS){
                    pref.edit().putString("id", id).putString("password", password).apply();
                }
                callback.setAccount(json, Resultno);
            }
        });
    }

    public void createUser(final Context context, final Callback callback){
        CreateAccountProcess.CreateAccount(context, new Callback() {
            @Override
            public void setAccount(JSONObject json, int Resultno) {
                if(Resultno == ACCOUNT_CREATE_SUCCESS){
                    try{
                        pid = json.getLong("pid");
                        pref.edit().putLong("pid", pid).apply();

                        login(pid, context, callback);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                callback.setAccount(json, Resultno);
            }
        });

    }

    public void signUp(final String id, final String password, final Context context, final Callback callback){
        SignUpProcess.signUp(id, password, context, new Callback() {
            @Override
            public void setAccount(JSONObject json, int Resultno) {
                if(Resultno == ACCOUNT_SIGNUP_SUCCESS){
                    pref.edit().putString("id", id).putString("password", password).apply();
                    login(id, password, context, callback);
                }

                callback.setAccount(json, Resultno);
            }
        });
    }
    public void checkPassword(String password){

    }

}
