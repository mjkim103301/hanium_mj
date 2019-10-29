package com.example.hanchat.module.account;

import android.content.Context;

import com.example.hanchat.R;
import com.example.hanchat.module.AccountManager;
import com.example.hanchat.module.HTTPConnecter;
import com.example.hanchat.module.Tools;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.hanchat.module.Tools.Encrypt;

public class LoginProcess {
    static HTTPConnecter httpConnecter = null;

    public static void login(final String id, final String password, final Context context, final AccountManager.Callback callback){
        httpConnecter = HTTPConnecter.getinstance(R.string.server_ip, R.string.server_port, context);
        Map<String, String> data = new HashMap<>();
        data.put("id", id);
        try {
            httpConnecter.Post("/apptest/account/getsalts", data, new HTTPConnecter.Callback() {
                @Override
                public Object DataReceived(String ReceiveString) {
                    JSONObject json = null;
                    try {
                        json = new JSONObject(ReceiveString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return json;
                }

                @Override
                public void HandlerMethod(Object obj) {
                    if (obj == null)
                        return;
                    JSONObject json = (JSONObject) obj;
                    try {
                        if (json.getBoolean("result")) {
                            String[] salts = new String[5];
                            for (int i = 0; i < 5; i++) {
                                salts[i] = json.getString("salt" + i);
                            }
                            saltGetted(id, password, salts, callback);
                        } else {
                            callback.setAccount(json, AccountManager.LOGIN_FAILED);
                        }
                    } catch (Exception e) {

                    }


                }
            });
        }
        catch (Exception e){}
    }

    public static void login(final long pid, final Context context, final AccountManager.Callback callback){
        httpConnecter = HTTPConnecter.getinstance(R.string.server_ip, R.string.server_port, context);
        Map<String, Long> data = new HashMap<>();
        data.put("pid", pid);

        connectToServer(data, callback);
    }

    private static void saltGetted(String id, String password, String[] salts, final AccountManager.Callback callback){
        password = Encrypt(password, salts);
        Map<String, String> data = new HashMap<>();

        data.put("id", id);
        data.put("password", password);

        connectToServer(data, callback);
    }

    private static void connectToServer(Map<String, ?> data, final AccountManager.Callback callback) {
        try {
            httpConnecter.Post("/apptest/account/login", data, new HTTPConnecter.Callback() {
                @Override
                public Object DataReceived(String ReceiveString) {
                    JSONObject json = null;
                    try {
                        json = new JSONObject(ReceiveString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return json;
                }

                @Override
                public void HandlerMethod(Object obj) {
                    if (obj == null)
                        return;
                    JSONObject json = (JSONObject) obj;
                    try {
                        if (json.getBoolean("result")) {
                            callback.setAccount(json, AccountManager.LOGIN_SUCCESS);
                        } else {
                            callback.setAccount(json, AccountManager.LOGIN_FAILED);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}
