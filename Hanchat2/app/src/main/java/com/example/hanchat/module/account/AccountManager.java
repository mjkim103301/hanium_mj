package com.example.hanchat.module.account;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hanchat.module.ApplicationSharedRepository;

import org.json.JSONException;
import org.json.JSONObject;


public class AccountManager {
    public interface Callback {
        public final static int LOGIN_SUCCESS = 1;
        public final static int LOGIN_FAILED = -1;
        public final static int ACCOUNT_CREATE_SUCCESS = 2;
        public final static int ACCOUNT_CREATE_FAILED = -2;
        public final static int ACCOUNT_SIGNUP_SUCCESS = 3;
        public final static int ACCOUNT_SIGNUP_FAILED = -3;
        void backgroundProcess(JSONObject data, int Resultno);
        void foregroundProcess(JSONObject data, int Resultno);
    }




    private static AccountManager instance = null;
    private final static String prefId = "AccountPref";

    Context appContext;
    SharedPreferences pref;
    private Long pid = 0L;
    private String loginToken = "";

    public Long getPid(){
        return pid;
    }
    public String getLoginToken() {
        return loginToken;
    }

    private AccountManager(){
        appContext = ApplicationSharedRepository.getAppContext();
        pref = appContext.getSharedPreferences(prefId, appContext.MODE_PRIVATE);
    }

    public static AccountManager getInstance(){
        if(instance == null)
            instance = new AccountManager();
        return instance;
    }

    public void autoLogin(Context context, Callback callback){

        if(pref.contains("id")){
            String id = pref.getString("id", null);
            String pwd = pref.getString("password", null);

            login(id, pwd, callback);
        }
        else if(pref.contains("pid")){
            pid = pref.getLong("pid", 0L);
            String loginToken = pref.getString("logintoken", "");
            login(pid, loginToken, callback);
        }
        else{
            createUser(callback);
        }
    }
    private void login(String id, String password, final Callback callback){
        LoginProcess.login(id, password, new Callback(){
            @Override
            public void foregroundProcess(JSONObject data, int Resultno) {
                callback.foregroundProcess(data, Resultno);
            }

            @Override
            public void backgroundProcess(JSONObject json, int Resultno) {
                if(Resultno == LOGIN_SUCCESS) {
                    try {
                        loginToken = (String)json.getString("logintoken");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.backgroundProcess(json, Resultno);
            }
        });
    }
    private void login(long pid, String loginToken, final Callback callback){
        LoginProcess.login(pid, loginToken, new Callback() {
            @Override
            public void foregroundProcess(JSONObject data, int Resultno) {
                callback.foregroundProcess(data, Resultno);
            }

            @Override
            public void backgroundProcess(JSONObject json, int Resultno) {
                if(Resultno == LOGIN_SUCCESS){
                    try{
                        //loginToken = (String)json.getString("logintoken");
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                callback.backgroundProcess(json, Resultno);
            }
        });

    }

    public void newLogin(final String id, final String password, final Callback callback){
        login(id, password, new Callback()  {
            @Override
            public void backgroundProcess(JSONObject json, int Resultno) {
                if(Resultno == LOGIN_SUCCESS){
                    pref.edit().putString("id", id).putString("password", password).apply();
                }
                callback.backgroundProcess(json, Resultno);
            }

            @Override
            public void foregroundProcess(JSONObject data, int Resultno) {
                callback.backgroundProcess(data, Resultno);
            }

        });
    }

    public void createUser(final Callback callback){
        CreateAccountProcess.CreateAccount(new Callback() {
            @Override
            public void foregroundProcess(JSONObject data, int Resultno) {
                callback.foregroundProcess(data, Resultno);
            }

            @Override
            public void backgroundProcess(JSONObject json, int Resultno) {
                if(Resultno == ACCOUNT_CREATE_SUCCESS){
                    try{
                        pid = json.getLong("pid");
                        loginToken = json.getString("logintoken");
                        pref.edit().putLong("pid", pid).putString("logintoken", loginToken).apply();

                        login(pid, loginToken, callback);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                callback.backgroundProcess(json, Resultno);
            }
        });

    }

    public void signUp(final String id, final String password, final Callback callback){
        SignUpProcess.signUp(id, password, new Callback() {
            @Override
            public void foregroundProcess(JSONObject data, int Resultno) {
                callback.foregroundProcess(data, Resultno);
            }

            @Override
            public void backgroundProcess(JSONObject json, int Resultno) {
                if(Resultno == ACCOUNT_SIGNUP_SUCCESS){
                    pref.edit().putString("id", id).putString("password", password).apply();
                    login(id, password, callback);
                }

                callback.backgroundProcess(json, Resultno);
            }
        });
    }
    public void checkPassword(String password){

    }

}
