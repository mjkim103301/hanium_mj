package com.example.hanchat.module.account;

import android.content.Context;

import com.example.hanchat.R;
import com.example.hanchat.module.AccountManager;
import com.example.hanchat.module.connecter.HttpConnecter;
import com.example.hanchat.module.Tools;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpProcess {
    public static void signUp(String id, String password, Context context, final AccountManager.Callback callback){
        String[] salt = new String[5];
        for (int i = 0; i < 5; i++) {
            salt[i] = Tools.getRandomString(32);
        }

        password = Tools.Encrypt(password, salt);

        Map<String, String> data = new HashMap<>();
        data.put("id", id);
        data.put("password", password);
        for(int i = 0 ;i < 5; i++){
            data.put("salt" + i , salt[i]);
        }
        try {
            HttpConnecter.getinstance(R.string.server_ip, R.string.server_port, context)
                    .Post("/apptest/account/signup", data, new HttpConnecter.Callback() {
                        @Override
                        public Object DataReceived(String ReceiveString) {
                            try {
                                JSONObject json = new JSONObject(ReceiveString);
                                return json;
                            }
                            catch (Exception e){

                            }
                            return null;
                        }

                        @Override
                        public void HandlerMethod(Object obj) {
                            if(obj == null) return;
                            JSONObject json = (JSONObject) obj;
                            try {
                                if (json.getBoolean("result"))
                                    callback.setAccount(json, AccountManager.ACCOUNT_SIGNUP_SUCCESS);
                                else{
                                    callback.setAccount(json, AccountManager.ACCOUNT_SIGNUP_FAILED);
                                }
                            }
                            catch (Exception e){

                            }
                        }
                    });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
