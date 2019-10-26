package com.example.hanchat.module;

import android.content.Context;

import com.example.hanchat.R;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class loginProcess {

    private loginProcess instance = null;
    private String myPid = "0";

    public String getPid(){
        return myPid;
    }

    private loginProcess() {
    }

    public loginProcess getInstance(){
        if(instance == null)
            instance = new loginProcess();
        return instance;
    }

    public void login(String id, String password, Context context){
        HTTPConnecter httpConnecter = HTTPConnecter.getinstance(R.string.server_ip, R.string.server_port, context);
        Map<String, String> data = new HashMap<>();
        data.put("id", id);
        String pwd;
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            pwd = bytesToHex(digest.digest(password.getBytes()));
            data.put("password", pwd);
            httpConnecter.Get("/apptest/login", data, new HTTPConnecter.Callback() {
                @Override
                public Object DataReceived(String ReceiveString) {
                    try{
                        JSONObject json = new JSONObject(ReceiveString);
                        myPid = (String)json.get("pid");
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
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b: bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

}
