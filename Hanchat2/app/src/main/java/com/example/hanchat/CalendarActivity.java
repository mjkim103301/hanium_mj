package com.example.hanchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.hanchat.module.CalendarAPIManager;
import com.google.android.material.navigation.NavigationView;

import pub.devrel.easypermissions.EasyPermissions;


/*완료*/
public class CalendarActivity extends NavActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    CalendarAPIManager calendarAPIManager;
    Button bt_go_chat;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarAPIManager = new CalendarAPIManager(CalendarActivity.this);

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

    /* CalendarAPIManager 사용하는 액티비티에서 이 코드 써야함 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        calendarAPIManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    /* CalendarAPIManager 사용하는 액티비티에서 이 코드 써야함 끝 */
}