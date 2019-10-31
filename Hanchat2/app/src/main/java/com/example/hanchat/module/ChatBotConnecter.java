package com.example.hanchat.module;

import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.hanchat.R;
import com.example.hanchat.data.chatting.OtherChatting;
import com.example.hanchat.data.chatting.UserChatting;
import com.example.hanchat.module.adapter.RecyclerAdapter;
import com.example.hanchat.module.connecter.HttpConnecter;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*완료*/
public class ChatBotConnecter implements View.OnClickListener {
    HttpConnecter connecter;
    //AppCompatActivity Activity;
    Fragment fragment;
    EditText et_chat;
    RecyclerAdapter chatAdapter;
    AccountManager account;

    public ChatBotConnecter(Fragment fragment, EditText et, RecyclerAdapter chatAdapter) {
        this.connecter = HttpConnecter.getinstance(R.string.server_ip, R.string.server_port, fragment.getContext());
        this.et_chat = et;
        this.fragment = fragment;
        this.chatAdapter = chatAdapter;
        account = AccountManager.getInstance(fragment.getContext());
    }

    @Override
    public void onClick(View v) {
        String des = et_chat.getText().toString();
        chatAdapter.addItemwithNotify(new UserChatting(des));
        //chatAdapter.add(1, des);    // 0은 챗봇, 1은 사용자
        et_chat.setText(null);
        //chatAdapter.notifyDataSetChanged(); // 데이터 변화 시 갱신해 줌

        try{
            //서버로 보낼 내용 : des
            //먼저 Json 형식으로 변환
            Map<String, Object> data = new HashMap<>();

            //텍스트 처리는 text, 이미지 처리는 image
            data.put("pid", account.getPid());
            data.put("logintoken", account.getLoginToken());
            data.put("text", des);


            //커넥터를 이용해 Post
            //Pathname - 텍스트 처리는 "/apptest/chatbot", 이미지 처리는 "/apptest/image"
            //Jsondata - json형식을 String으로 바꾼 데이터
            //new 어쩌고 - 데이터 통신 이후에 실행될 콜백 함수들을 정의
            connecter.Post("/apptest/chatbot", data, new HttpConnecter.Callback() {
                //String str;                   //

                //여기는 데이터를 받아서 가공하는 곳
                @Override
                public Object DataReceived(String ReceiveString) {
                    try{
                        JSONObject json = new JSONObject(ReceiveString);
                        return json;
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    return null;

                    //일단 여기서는 텍스트를 받기만 하므로 그대로 리턴
                }


                //여기는 가공한 데이터를 받아서 화면에 보여주는 곳
                @Override
                public void HandlerMethod(Object obj) {
                    //위의 함수에서 받은 내용을 토스트메시지로 출력
                    try{
                        JSONObject json = (JSONObject) obj;
                        if(json.getBoolean("result")){
                            chatAdapter.addItemwithNotify(new OtherChatting(json.getString("answer")));

                        }
                    }
                    catch (Exception e){

                    }

                    //chatAdapter.add(0, answer);
                    //chatAdapter.notifyDataSetChanged(); // 데이터 변화 시 갱신해 줌
                    //view.append((String) obj);
                }
            });

        }
        catch (Exception e){
            //예외처리
            e.printStackTrace();
        }

    }
}
