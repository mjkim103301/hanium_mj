package com.example.hanchat.ui.chatbot;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.hanchat.CalendarActivity;
import com.example.hanchat.MainActivity;
import com.example.hanchat.R;
import com.example.hanchat.data.EmptyData;
import com.example.hanchat.data.chatting.Chatting;
import com.example.hanchat.data.chatting.OtherChatting;
import com.example.hanchat.data.chatting.UserChatting;
import com.example.hanchat.module.ChatBotConnecter;
import com.example.hanchat.module.ImageManagement_mj;
import com.example.hanchat.module.RecyclerAdapter;
import com.example.hanchat.module.RecyclerManager;

public class ChatbotFragment extends Fragment {

    private ChatbotViewModel mViewModel;

    public static ChatbotFragment newInstance() {
        return new ChatbotFragment();
    }

    ImageManagement_mj imageManagement;

    View view;
    Button bt_go_cal;
    EditText et_chat;
    Button bt_chat;
    Button bt_image;

    RecyclerAdapter<Chatting> adapter;

    Intent intent;

    String TAG="@@@@ ";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.fragment_chatbot, container, false);

        // 화면 전환
        intent = new Intent(getContext(), CalendarActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        //버튼 연결
        bt_go_cal =  (Button) view.findViewById(R.id.bt_go_cal);
        et_chat = view.findViewById(R.id.et_chat);
        bt_chat = view.findViewById(R.id.bt_chat);
        bt_image = view.findViewById(R.id.bt_image);

        /*NavSetting();
        IntentProfileSetting(context);*/
        chatAdapterSetting();
        ButtonSetting();

        //서버 연결 테스트
        et_chat.setText("안녕");
//        bt_chat.callOnClick();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChatbotViewModel.class);
        // TODO: Use the ViewModel
    }


    private void chatAdapterSetting() {
        RecyclerView chating_list = view.findViewById(R.id.chating_list);
        adapter = new RecyclerAdapter<Chatting>(){
            @Override
            public void addItemwithNotify(RecyclerItem item) {
                super.addItemwithNotify(item);
                //((LinearLayoutManager) parentView.getLayoutManager()).scrollToPosition(this.getItemCount() - 1);
               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        parentView.scrollToPosition(adapter.getItemCount() - 1);
                    }
                },50);*/
            }
        };
        chating_list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setItemViewCreateAction(new RecyclerManager.ItemViewCreateAction() {
            @Override
            public void ItemViewCreated(final RecyclerManager.ViewHolder holder, int itemType) {
                if(itemType == RecyclerAdapter.OTHERCHATTING || itemType == RecyclerAdapter.USERCHATTING){
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((Chatting)holder.getItem()).setVisible(false);
                            adapter.notifyItemChanged(holder.getAdapterPosition());
                            //adapter.notifyItemMoved(0, adapter.getItemCount() - 1);
                        }
                    });
                }
            }
        });
        chating_list.setAdapter(adapter);

        adapter.addItem(new OtherChatting("안녕하세요 HANCHAT 임시UI입니다!"));
        adapter.addItem(new UserChatting("내일 7시에 은행동에서 친구랑 만나!"));
        adapter.addItem(new OtherChatting("이제 시작해볼까요?"));
//        adapter.addItem(new EmptyData());
        adapter.notifyDataSetChanged();
    }


    //버튼 세팅들은 여기에
    private void ButtonSetting(){
        // 우측 상단 버튼 (캘린더 화면으로 이동)
//        bt_go_cal.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////                startActivity(intent);
////            }
////        });

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageManagement.onActivityResult(requestCode, resultCode, data);
    }
}
