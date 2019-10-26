package com.example.hanchat.module.account;

import android.content.Context;

import com.example.hanchat.R;
import com.example.hanchat.module.HTTPConnecter;

public class CreateAccountProcess {
    public static void CreateAccount(Context context){
        HTTPConnecter httpConnecter = HTTPConnecter.getinstance(R.string.server_ip, R.string.server_port, context);

        httpConnecter.Post("/createuser", null, new HTTPConnecter.Callback() {
            @Override
            public Object DataReceived(String ReceiveString) {
                return null;
            }

            @Override
            public void HandlerMethod(Object obj) {

            }
        });
    }
}
