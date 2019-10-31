package com.example.hanchat.module.account;

import com.example.hanchat.R;
import com.example.hanchat.module.connecter.HttpConnecter;

import org.json.JSONObject;

public class CreateAccountProcess {
    public static void CreateAccount(final AccountManager.Callback callback) {
        HttpConnecter httpConnecter = HttpConnecter.getinstance(R.string.server_ip, R.string.server_port);

        try {
            httpConnecter.Post(R.string.route_createUser, null, new HttpConnecter.ResponseRecivedCallback() {
                @Override
                public void DataReceived(JSONObject data) {
                    callback.backgroundProcess(data, 0);

                }

                @Override
                public void DataInvoked(JSONObject data) {
                    try {
                        if (data.getBoolean("result")) {
                            callback.backgroundProcess(data, ACCOUNT_CREATE_SUCCESS);
                        } else {
                            callback.backgroundProcess(data, AccountManager.ACCOUNT_CREATE_FAILED);
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
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
