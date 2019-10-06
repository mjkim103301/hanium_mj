package com.example.hanchat;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import com.example.hanchat.data.chatting.Chatting;
import com.example.hanchat.data.chatting.OtherChatting;
import com.example.hanchat.data.chatting.UserChatting;
import com.example.hanchat.module.ChatBotConnecter;
import com.example.hanchat.module.ImageManagement_mj;
import com.example.hanchat.module.RecyclerAdapter;
import com.example.hanchat.module.RecyclerManager;
import com.google.android.material.navigation.NavigationView;

import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/*완료*/
public class MainActivity extends NavActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ImageManagement_mj imageManagement;
    RecyclerAdapter<Chatting> adapter;
    Intent intent;

    //final String IP = "18.219.204.210";

    Button bt_go_cal;
    EditText et_chat;
    Button bt_chat;
    Button bt_image;
    LinearLayout linearLayout;

    View v_notice;
    Button bt_slide;
    boolean isDown;

    String TAG="@@@@ ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 화면 전환
        intent = new Intent(MainActivity.this, CalendarActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        //버튼 연결
        bt_go_cal =  (Button) findViewById(R.id.bt_go_cal);
        et_chat = findViewById(R.id.et_chat);
        bt_chat = findViewById(R.id.bt_chat);
        bt_image = findViewById(R.id.bt_image);
        bt_slide = findViewById(R.id.bt_slide);

        linearLayout = findViewById(R.id.linearLayout);

        //공지
        v_notice = findViewById(R.id.v_notice);

        v_notice.setVisibility(View.GONE);
        bt_slide.setText("Down");
        isDown = false;

        NavSetting();
        IntentProfileSetting(MainActivity.this);
        chatAdapterSetting();
        ButtonSetting();

        //서버 연결 테스트
        et_chat.setText("안녕");
        bt_chat.callOnClick();
    }

    private void chatAdapterSetting() {
        RecyclerView chating_list = findViewById(R.id.chating_list);
        adapter = new RecyclerAdapter(){
            @Override
            public void addItem(RecyclerItem item) {
                super.addItem(item);
                ((LinearLayoutManager) parentView.getLayoutManager()).scrollToPosition(this.getItemCount() - 1);
            }
        };
        chating_list.setLayoutManager(new LinearLayoutManager(this));
        chating_list.setAdapter(adapter);

        adapter.addItem(new OtherChatting("안녕하세요 HANCHAT 임시UI입니다!"));
        adapter.addItem(new UserChatting("내일 7시에 은행동에서 친구랑 만나!"));
        adapter.addItem(new OtherChatting("이제 시작해볼까요?"));

        adapter.setItemViewAction(new RecyclerManager.ItemViewAction() {
            @Override
            public void setItemView(final RecyclerManager.ViewHolder holder,final RecyclerManager.RecyclerItem item, int viewType) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((Chatting)item).setVisible(false);
                        adapter.notifyItemChanged(holder.getAdapterPosition());
                        //adapter.notifyItemMoved(0, adapter.getItemCount() - 1);
                    }
                });
            }
        });
    }


    //버튼 세팅들은 여기에
    private void ButtonSetting(){


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

    //공지사항 슬라이드 애니메이션
    public void slideUp(View view){
        v_notice.setVisibility(View.GONE);
        TranslateAnimation animate = new TranslateAnimation(
                0,            // xStart
                0,              // xEnd
                0 ,         // yStart
                -view.getHeight());             // yEnd
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void slideDown(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,            // xStart
                0,              // xEnd
                -view.getHeight(),         // yStart
                0);             // yEnd
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void onSlideViewButtonClick(View view) {
        if (isDown) {
            slideUp(v_notice);
            bt_slide.setText("Down");
        } else {
            slideDown(v_notice);
            bt_slide.setText("Up");
        }
        isDown = !isDown;
    }
}

class chatt<T extends RecyclerManager.RecyclerItem> extends RecyclerAdapter<T>{

    @Override
    public void addItem(T item) {
        super.addItem(item);
        ((LinearLayoutManager) parentView.getLayoutManager()).scrollToPosition(this.getItemCount() - 1);
    }
}
