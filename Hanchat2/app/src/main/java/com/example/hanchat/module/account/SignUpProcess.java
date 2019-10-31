package com.example.hanchat.module.account;

import com.example.hanchat.R;
import com.example.hanchat.module.Tools;
import com.example.hanchat.module.connecter.HttpConnecter;

import org.json.JSONObject;

public class SignUpProcess {
    public static void signUp(String id, String password, final AccountManager.Callback callback){
        String[] salt = new String[5];
        for (int i = 0; i < 5; i++) {
            salt[i] = Tools.getRandomString(32);
        }

        password = Tools.Encrypt(password, salt);

        JSONObject data = new JSONObject();
        try{
            data.put("id", id);
            data.put("password", password);
            for(int i = 0 ;i < 5; i++){
                data.put("salt" + i , salt[i]);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        HttpConnecter.getinstance(R.string.server_ip, R.string.server_port).Post(null, data,
                new HttpConnecter.ResponseRecivedCallback() {
            @Override
            public void DataReceived(JSONObject data) {

            }

            @Override
            public void DataInvoked(JSONObject data) {
                try {
                    if (data.getBoolean("result"))
                        callback.backgroundProcess(data, AccountManager.ACCOUNT_SIGNUP_SUCCESS);
                    else{
                        callback.backgroundProcess(data, AccountManager.ACCOUNT_SIGNUP_FAILED);
                    }
                }
                catch (Exception e){

                }
            }

            @Override
            public void ExceptionThrowed(Exception e) {

            }

        });
    }
}
