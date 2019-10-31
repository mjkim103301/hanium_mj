package com.example.hanchat.module.account;

import android.content.Context;

import com.example.hanchat.R;
import com.example.hanchat.module.AccountManager;
import com.example.hanchat.module.connecter.HttpConnecter;

import org.json.JSONObject;

public class CreateAccountProcess {
    public static void CreateAccount(Context context, final AccountManager.Callback callback) {
        HttpConnecter httpConnecter = HttpConnecter.getinstance(R.string.server_ip, R.string.server_port, context);

        try {
            httpConnecter.Post("/apptest/account/createuser", null, new HttpConnecter.Callback() {
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
                    if (obj == null) {
                        return;
                    }
                    JSONObject json = (JSONObject) obj;
                    try {
                        if (json.getBoolean("result")) {
                            callback.setAccount(json, AccountManager.ACCOUNT_CREATE_SUCCESS);
                        } else {
                            callback.setAccount(json, AccountManager.ACCOUNT_CREATE_FAILED);
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
