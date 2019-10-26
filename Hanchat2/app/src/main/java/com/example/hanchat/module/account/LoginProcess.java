package com.example.hanchat.module.account;

import android.content.Context;

import com.example.hanchat.R;
import com.example.hanchat.module.AccountManager;
import com.example.hanchat.module.HTTPConnecter;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginProcess {

    public static void login(String id, String password, Context context, final AccountManager.AccountSetting accountSet){
        HTTPConnecter httpConnecter = HTTPConnecter.getinstance(R.string.server_ip, R.string.server_port, context);
        Map<String, String> data = new HashMap<>();
        data.put("id", id);
        data.put("password", password);

        httpConnecter.Post("/apptest/login", data, new HTTPConnecter.Callback() {
            @Override
            public Object DataReceived(String ReceiveString) {
                try{
                    JSONObject json = new JSONObject(ReceiveString);
                    accountSet.setAccount(json);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            public void HandlerMethod(Object obj) {

            }
        });

    }

}
