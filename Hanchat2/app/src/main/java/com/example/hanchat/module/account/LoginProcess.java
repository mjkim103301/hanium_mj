package com.example.hanchat.module.account;

import com.example.hanchat.R;
import com.example.hanchat.module.connecter.HttpConnecter;

import org.json.JSONObject;

import static com.example.hanchat.module.Tools.Encrypt;

public class LoginProcess {
    static HttpConnecter httpConnecter = null;

    public static void login(final String id, final String password, final AccountManager.Callback callback){
        httpConnecter = HttpConnecter.getinstance(R.string.server_ip, R.string.server_port);
        JSONObject data = new JSONObject();
        try{
            data.put("id", id);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        httpConnecter.Post(R.string.route_getSalts, data, new HttpConnecter.ResponseRecivedCallback() {
            @Override
            public void DataReceived(JSONObject data) {

            }

            @Override
            public void DataInvoked(JSONObject data) {
                try {
                    if (data.getBoolean("result")) {
                        String[] salts = new String[5];
                        for (int i = 0; i < 5; i++) {
                            salts[i] = data.getString("salt" + i);
                        }
                        saltGetted(id, password, salts, callback);
                    } else {
                        callback.backgroundProcess(data, AccountManager.LOGIN_FAILED);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void ExceptionThrowed(Exception e) {

            }
        });

    }

    public static void login(final long pid, final String loginToken, final AccountManager.Callback callback){
        httpConnecter = HttpConnecter.getinstance(R.string.server_ip, R.string.server_port);
        JSONObject data = new JSONObject();
        try{
            data.put("pid", pid);
            data.put("datatoken", loginToken);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        connectToServer(data, callback);
    }

    private static void saltGetted(String id, String password, String[] salts, final AccountManager.Callback callback){
        password = Encrypt(password, salts);
        JSONObject data = new JSONObject();

        try{
            data.put("id", id);
            data.put("password", password);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        connectToServer(data, callback);
    }

    private static void connectToServer(JSONObject data, final AccountManager.Callback callback) {
        httpConnecter.Post(R.string.route_login, data, new HttpConnecter.ResponseRecivedCallback() {
            @Override
            public void DataReceived(JSONObject data) {

            }

            @Override
            public void DataInvoked(JSONObject data) {
                try {
                    if (data.getBoolean("result")) {
                        callback.backgroundProcess(data, AccountManager.LOGIN_SUCCESS);
                    } else {
                        callback.backgroundProcess(data, AccountManager.LOGIN_FAILED);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void ExceptionThrowed(Exception e) {

            }
        });

    }


}
