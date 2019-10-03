package com.example.hanchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.navigation.NavigationView;

/*완료*/
public class CalendarActivity extends NavActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button bt_go_chat;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        intent = new Intent(CalendarActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        // 우측 상단 버튼 (캘린더 화면으로 이동)
        bt_go_chat = findViewById(R.id.bt_go_chat);

        NavSetting();
        ButtonSetting();
    }

    //버튼 세팅들은 여기에
    private void ButtonSetting(){
        bt_go_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                // FLAG_ACTIVITY_CLEAR_TOP : 동일한 activity가 stack에 연속적으로 쌓였을때 activity를 재사용하는 Flag 0-A-B-B 일때 0-A-B
                // Main이 쌓이길래 새 태스크를 만드는 플래그 (FLAG_ACTIVITY_NEW_TASK)로 해결
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        });

    }

    //이미지파일을 보내려면 /apptest/test로 sendImage
}