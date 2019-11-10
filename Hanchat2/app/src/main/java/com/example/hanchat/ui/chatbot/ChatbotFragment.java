package com.example.hanchat.ui.chatbot;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hanchat.R;
import com.example.hanchat.data.chatting.Chatting;
import com.example.hanchat.data.chatting.OtherChatting;
import com.example.hanchat.data.chatting.UserChatting;
import com.example.hanchat.module.connecter.ChatbotConnecter;
import com.example.hanchat.module.ImageManagement;
import com.example.hanchat.module.adapter.RecyclerAdapter;
import com.example.hanchat.module.adapter.RecyclerManager;
import com.example.hanchat.ui.RestoreRecyclerFragment;

import org.json.JSONException;
import org.json.JSONObject;

/*
    챗봇 프래그먼트
    채팅과 사진이 가능하게
    뷰모델에는 채팅 리스트 저장하기
 */
public class ChatbotFragment extends RestoreRecyclerFragment {

    private ChatbotViewModel mViewModel;

    public static ChatbotFragment newInstance() {
        return new ChatbotFragment();
    }

    ImageManagement imageManagement;
    RecyclerAdapter<Chatting> adapter;
    ChatbotConnecter chatbotConnecter;
    Intent intent;

    Button bt_go_cal;
    EditText et_chat;
    Button bt_chat;
    Button bt_image;
    LinearLayout linearLayout;

    boolean isDown;

    String TAG="@@@@ ";

    View view;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.fragment_chatbot, container, false);

        et_chat = view.findViewById(R.id.et_chat);
        bt_chat = view.findViewById(R.id.bt_chat);
        bt_image = view.findViewById(R.id.bt_image);

        linearLayout = view.findViewById(R.id.linearLayout);

        //공지
        isDown = false;

        chatbotConnecter = new ChatbotConnecter();

        /*NavSetting();
        IntentProfileSetting(context);*/
        ButtonSetting();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ChatbotViewModel.class);

        RecyclerView chating_list = view.findViewById(R.id.chating_list);
        adapter = new RecyclerAdapter<Chatting>(){
            @Override
            public void addItemwithNotify(RecyclerItem item) {
                super.addItemwithNotify(item);
                //((LinearLayoutManager) parentView.getLayoutManager()).scrollToPosition(this.getItemCount() - 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        parentView.scrollToPosition(adapter.getItemCount() - 1);
                    }
                },200);
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

        mViewModel.setRecyclerAdapterWithParentView(this, adapter);
    }


    //버튼 세팅들은 여기에
    private void ButtonSetting(){

        // 채팅 전송
//        bt_chat.setOnClickListener(new ChatBotConnecter(this, et_chat, adapter));
        bt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = et_chat.getText().toString();
                adapter.addItemwithNotify(new UserChatting(text));
                et_chat.setText(null);
                chatbotConnecter.chatbot(text, new ChatbotConnecter.ChatbotCallback() {
                    @Override
                    public void DataReceived(JSONObject data) throws JSONException {
                        //여기서 일정 만들기
                    }

                    @Override
                    public void DataInvoked(JSONObject data) throws JSONException {
                        if(data.getBoolean(KEY_RESULT)){
                            String ans = data.getString(KEY_ANSWER);
                            adapter.addItemwithNotify(new OtherChatting(ans));
                        }
                    }

                    @Override
                    public void ConnectionFailed(Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "서버와 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        imageManagement=new ImageManagement(this, adapter);

        //엔터 키입력
        et_chat.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    bt_chat.callOnClick();
                    return true;
                }
                return false;
            }
        });


        bt_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImagefromGallery(view);
            }
        });
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
}

