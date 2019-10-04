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
    String TAG = "@@@@ ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        intent = new Intent(CalendarActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        // 우측 상단 버튼 (캘린더 화면으로 이동)
        bt_go_chat = findViewById(R.id.bt_go_chat);

        NavSetting();
        IntentProfileSetting(CalendarActivity.this);
        ButtonSetting();
    }

    //버튼 세팅들은 여기에
    private void ButtonSetting() {
        bt_go_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

    }
}