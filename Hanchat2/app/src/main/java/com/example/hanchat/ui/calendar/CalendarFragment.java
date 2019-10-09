package com.example.hanchat.ui.calendar;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hanchat.CalendarActivity;
import com.example.hanchat.MainActivity;
import com.example.hanchat.R;
import com.example.hanchat.module.CalendarAPIManager;

import pub.devrel.easypermissions.EasyPermissions;

public class CalendarFragment extends Fragment {

    private CalendarViewModel mViewModel;
    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    CalendarAPIManager calendarAPIManager;
    Button bt_go_chat;
    Intent intent;
    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
//        calendarAPIManager = new CalendarAPIManager(CalendarActivity.this);

//
//        intent = new Intent(getContext(), MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//
//        // 우측 상단 버튼 (캘린더 화면으로 이동)
//        bt_go_chat = view.findViewById(R.id.bt_go_chat);
//
//        /*NavSetting();
//        IntentProfileSetting(CalendarActivity.this);*/
//        ButtonSetting();


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        // TODO: Use the ViewModel
    }


    //버튼 세팅들은 여기에
    private void ButtonSetting() {
//        bt_go_chat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(intent);
//            }
//        });

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
