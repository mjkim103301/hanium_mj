package com.example.hanchat;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import com.example.hanchat.data.Chatting;
import com.example.hanchat.data.OtherChatting;
import com.example.hanchat.data.UserChatting;
import com.example.hanchat.module.ChatBotConnecter;
import com.example.hanchat.module.ImageManagement_mj;
import com.example.hanchat.module.RecyclerAdapter;
import com.example.hanchat.module.RecyclerManager;
import com.google.android.material.navigation.NavigationView;

import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/*완료*/
public class MainActivity extends NavActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageManagement_mj imageManagement;


    Button bt_go_cal;
    EditText et_chat;
    Button bt_chat;
    Button bt_image;

    chatt<Chatting> adapter;

    Intent intent;
    Intent intent_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 화면 전환
        intent = new Intent(MainActivity.this, GroupMainActivity.class);
        intent_profile = new Intent(MainActivity.this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        //네비게이션 바의 헤더 얻어옴
        navigationView = findViewById(R.id.nav_view);
        headerview = navigationView.getHeaderView(0);


        //버튼 연결
        bt_go_cal =  (Button) findViewById(R.id.bt_go_cal);
        et_chat = findViewById(R.id.et_chat);
        bt_chat = findViewById(R.id.bt_chat);
        bt_image = findViewById(R.id.bt_image);

        NavSetting();
        testAdapterSetting();
        ButtonSetting();

        //서버 연결 테스트
        et_chat.setText("안녕");
        bt_chat.callOnClick();
    }

    private void testAdapterSetting() {
        RecyclerView chating_list = findViewById(R.id.chating_list);
        adapter = new chatt<>();
        chating_list.setLayoutManager(new LinearLayoutManager(this));
        chating_list.setAdapter(adapter);

        adapter.addItem(new OtherChatting("안녕하세요 HANCHAT 임시UI입니다!"));
        adapter.addItem(new UserChatting("내일 7시에 은행동에서 친구랑 만나!"));
        adapter.addItem(new OtherChatting("이제 시작해볼까요?"));

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
        bt_chat.setOnClickListener(new ChatBotConnecter(this, et_chat, adapter));
        imageManagement=new ImageManagement_mj(this, adapter);
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

class chatt<T extends RecyclerManager.RecyclerItem> extends RecyclerAdapter<T>{

    @Override
    public void addItem(T item) {
        super.addItem(item);
        ((LinearLayoutManager) parentView.getLayoutManager()).scrollToPosition(this.getItemCount() - 1);
    }
}