package com.example.hanchat.module.connecter;

import com.example.hanchat.R;
import com.example.hanchat.module.connecter.AuthHttpConnecter;
import com.example.hanchat.module.connecter.HttpConnecter;

import org.json.JSONObject;


/*
    챗봇과 관련된 모든 일을 처리하는 클래스
 */
public class ChatbotConnecter {
    AuthHttpConnecter authHttpConnecter;

    public interface ChatbotCallback extends HttpConnecter.ResponseRecivedCallback{
        //여기 상수들은 챗봇의 인텐트 모음
        String KEY_INTENT = "intent";
        String KEY_ANSWER = "answer";
        String KEY_PARAMETER = "params";
        String INTENT_SCHEDULEFINDER = "ScheduleFinder";

    }

    public ChatbotConnecter() {
        authHttpConnecter = new AuthHttpConnecter(HttpConnecter.getinstance(R.string.route_host, R.string.route_port));

    }

    public void chatbot(String text, ChatbotCallback callback){
        JSONObject data = new JSONObject();
        try{
            data.put("text", text);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        authHttpConnecter.Post(R.string.route_chatbot, data, callback);
    }
}
