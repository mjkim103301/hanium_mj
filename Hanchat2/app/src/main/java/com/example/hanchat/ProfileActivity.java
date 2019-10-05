package com.example.hanchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hanchat.module.CalendarAPIManager;

import pub.devrel.easypermissions.EasyPermissions;

/*완료*/
public class ProfileActivity extends AppCompatActivity {
    EditText editText_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 길게 클릭 시 아이디 복사
        editText_id = findViewById(R.id.editText_id);
        editText_id.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String user_id = editText_id.getText().toString();
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

}

