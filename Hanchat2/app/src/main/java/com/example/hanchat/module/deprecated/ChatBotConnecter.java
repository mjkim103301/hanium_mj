package com.example.hanchat.module.deprecated;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.hanchat.R;
import com.example.hanchat.data.chatting.OtherChatting;
import com.example.hanchat.data.chatting.UserChatting;
import com.example.hanchat.module.AccountManager;
import com.example.hanchat.module.adapter.RecyclerAdapter;
import com.example.hanchat.module.connecter.HttpConnecter;

import org.json.JSONObject;

/*완료*/
@Deprecated
public class ChatBotConnecter implements View.OnClickListener {
    HttpConnecter connecter;
    //AppCompatActivity Activity;
    Fragment fragment;
    EditText et_chat;
    RecyclerAdapter chatAdapter;
    AccountManager account;

    public ChatBotConnecter(Fragment fragment, EditText et, RecyclerAdapter chatAdapter) {
        this.connecter = HttpConnecter.getinstance(R.string.server_ip, R.string.server_port);
        this.et_chat = et;
        this.fragment = fragment;
        this.chatAdapter = chatAdapter;
        account = AccountManager.getInstance();
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
            JSONObject data = new JSONObject();

            //텍스트 처리는 text, 이미지 처리는 image
            data.put("pid", account.getPid());
            data.put("logintoken", account.getLoginToken());
            data.put("text", des);


            connecter.Post(R.string.route_chatbot, data, new HttpConnecter.ResponseRecivedCallback() {
                @Override
                public void DataReceived(JSONObject data) {

                }

                @Override
                public void DataInvoked(JSONObject data) {
                    try {
                        if (data.getBoolean("result")) {
                            chatAdapter.addItemwithNotify(new OtherChatting(data.getString("answer")));
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void ConnectionFailed(Exception e) {
                    Toast.makeText(fragment.getContext(), "서버에 연결할 수 없습니다", Toast.LENGTH_LONG).show();
                }

            });

        }
        catch (Exception e){
            //예외처리
            e.printStackTrace();
        }

    }
}
