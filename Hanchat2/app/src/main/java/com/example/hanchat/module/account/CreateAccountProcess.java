package com.example.hanchat.module.account;

import android.content.Context;

import com.example.hanchat.R;
import com.example.hanchat.module.AccountManager;
import com.example.hanchat.module.HTTPConnecter;
import com.example.hanchat.module.Tools;

import org.json.JSONObject;

public class CreateAccountProcess {
    public static void CreateAccount(Context context, final AccountManager.Callback callback) {
        HTTPConnecter httpConnecter = HTTPConnecter.getinstance(R.string.server_ip, R.string.server_port, context);

        try {
            httpConnecter.Post("/createuser", null, new HTTPConnecter.Callback() {
                @Override
                public Object DataReceived(String ReceiveString) {
                    JSONObject json = null;
                    try {
                        json = new JSONObject(ReceiveString);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
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

        }
    }
}
