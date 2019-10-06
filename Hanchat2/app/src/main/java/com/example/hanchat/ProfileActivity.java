package com.example.hanchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hanchat.module.CalendarAPIManager;
import com.google.android.gms.common.SignInButton;

import pub.devrel.easypermissions.EasyPermissions;

/*완료*/
public class ProfileActivity extends AppCompatActivity {
    CalendarAPIManager calendarAPIManager;
    EditText et_id;
    EditText et_username;
    SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        calendarAPIManager = new CalendarAPIManager(ProfileActivity.this);

        et_id = findViewById(R.id.et_id);
        et_username = findViewById(R.id.et_username);
        signInButton = findViewById(R.id.sign_in_button);

        //if(true){ //API 로그인 여부 확인 해서 true 이면 프로필받아옴 네비게이션에서도 값 바뀌어야함
            String name = calendarAPIManager.getUserName();
            String email = calendarAPIManager.getUserID();
            et_username.setText(name);
            et_id.setText(email);
        //}

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarAPIManager.setmIDButton(1);
            }
        });

        // 길게 클릭 시 아이디 복사
        et_id.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String user_id = et_id.getText().toString();
                setOnClipBoard(user_id);
                Toast.makeText(getApplicationContext(), "Long click to copy ID", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    // 클립보드에 아이디 복사
    public void setOnClipBoard(String user_id) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Clip Data", user_id);
        clipboardManager.setPrimaryClip(clipData);
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

