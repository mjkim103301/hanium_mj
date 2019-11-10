package com.example.hanchat.ui.more;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hanchat.R;
import com.example.hanchat.module.AccountManager;
import com.example.hanchat.module.CalendarAPIManager;
import com.google.android.gms.common.SignInButton;

import pub.devrel.easypermissions.EasyPermissions;

/*
    로그인, 설정 등을 하는 프래그먼트
    로그인 부분을 액티비티로 빼는게 나을듯
    자기 프로필 설정을 할수 있게 바꿀것
 */
public class MoreFragment extends Fragment {

    private MoreViewModel mViewModel;

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }
    CalendarAPIManager calendarAPIManager;
    EditText et_id;
    EditText et_username;
    SignInButton signInButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        et_id = view.findViewById(R.id.et_id);
        et_username = view.findViewById(R.id.et_username);
        signInButton = view.findViewById(R.id.sign_in_button);
        Long pid = AccountManager.getInstance().getPid();
        ((TextView)view.findViewById(R.id.et_pid)).setText(String.valueOf(pid));
        //GetData();
        ButtonSetting();
        return view;
    }


    private void ButtonSetting() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetData();
            }
        });
        // 길게 클릭 시 아이디 복사
        et_id.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String user_id = et_id.getText().toString();
                setOnClipBoard(user_id);
                Toast.makeText(getContext(), "Long click to copy ID", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    private void GetData() {
        calendarAPIManager.setmIDButton(1);

        String name = calendarAPIManager.getUserName();
        String email = calendarAPIManager.getUserID();
        et_username.setText(name);
        et_id.setText(email);
    }

    // 클립보드에 아이디 복사
    public void setOnClipBoard(String user_id) {
        ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Clip Data", user_id);
        clipboardManager.setPrimaryClip(clipData);
    }

    /* CalendarAPIManager 사용하는 액티비티에서 이 코드 써야함 */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
