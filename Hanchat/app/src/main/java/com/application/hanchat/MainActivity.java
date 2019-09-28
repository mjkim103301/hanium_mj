package com.application.hanchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends NavActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    HTTPConnecter connecter;
    ImageManagement_mj imageManagement;

    final String IP = "18.219.204.210";

    Button bt_go_cal;
    EditText et_chat;
    Button bt_chat;
    Button bt_image;

    ChatAdapter chatAdapter;
    ListView chating_list;

    Intent intent;
    Intent intent_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(MainActivity.this, CalendarActivity.class);
        intent_profile = new Intent(MainActivity.this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        navigationView = findViewById(R.id.nav_view);
        headerview = navigationView.getHeaderView(0);

        connecter = HTTPConnecter.getinstance(IP, 55252);
        bt_go_cal =  (Button) findViewById(R.id.bt_go_cal);
        et_chat = findViewById(R.id.et_chat);
        bt_chat = findViewById(R.id.bt_chat);
        bt_image = findViewById(R.id.bt_image);

        NavSetting();
        ChatAdapterSetting();
        ButtonSetting();
        et_chat.setText("안녕");
        bt_chat.callOnClick();
    }


    private void ChatAdapterSetting(){

        // 채팅 리스트 관리하는 어댑터 객체 생성
        chatAdapter =  new ChatAdapter();
        chating_list = (ListView) findViewById(R.id.chating_list);
        chating_list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL); //스크롤을 늘 리스트뷰의 제일 마지막으로
        chating_list.setStackFromBottom(true);  //아래로 계속 생성되도록 함
        chating_list.setAdapter(chatAdapter);

        // 임시 코드
        chatAdapter.add(0, "안녕하세요 HANCHAT 임시UI입니다!");
        chatAdapter.add(1,"내일 7시에 은행동에서 친구랑 만나!");
        chatAdapter.add(0, "이제 시작해볼까요?");
        chatAdapter.notifyDataSetChanged();
    }

    //버튼 세팅들은 여기에
    private void ButtonSetting(){
        // 네비게이션 헤더 클릭시 프로필로 이동
        headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent_profile);
            }
        });
        // 이미지는 연결 안됨
        //img_header.setOnClickListener(profileClickListener);

        // 우측 상단 버튼 (캘린더 화면으로 이동)
        bt_go_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent);
            }
        });

        // 채팅 전송
        bt_chat.setOnClickListener(new ButtonAction(this, connecter, et_chat, chatAdapter));
        imageManagement=new ImageManagement_mj(this, connecter, chatAdapter);
    }

    // + 버튼 눌렀을때 실행됨(나 다른방법 써서 버튼 세팅 안할듯)
    public void loadImagefromGallery(View view) {

        imageManagement.tedPermission();
        imageManagement.loadImage();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageManagement.onActivityResult(requestCode, resultCode, data);
    }


}
