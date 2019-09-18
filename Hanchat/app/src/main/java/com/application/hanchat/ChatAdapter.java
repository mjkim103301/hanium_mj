package com.application.hanchat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    // 채팅 기록을 보관하는 배열
    private ArrayList<ChatItem> chat_list;

    class ChatItem {
        int talker;
        String text;

        ChatItem(int _talker, String _text) {
            this.talker = _talker;
            this.text = _text;
        }
    }

    // 어댑터 생성자
    ChatAdapter() {
        chat_list = new ArrayList<>();
    }

    // 어댑터 대화 추가
    void add(int _talker, String _text) {
        chat_list.add(new ChatItem(_talker, _text));
    }

    @Override
    public int getCount() {
        return chat_list.size();
    }

    @Override
    public Object getItem(int i) {
        return chat_list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ChatHolder {
        TextView c_textView;
        LinearLayout layout;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final int idx = i;
        final Context context = parent.getContext();

        ChatHolder chatHolder = null;
        LinearLayout layout = null;
        TextView chattextView = null;

        //  convertView == null : 현재 화면에 보이지 않음
        if (convertView == null) {
            // view == null : chatHolder 가져와 보여줌
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_item, parent, false);

            layout = (LinearLayout) convertView.findViewById(R.id.layout);
            chattextView = (TextView) convertView.findViewById(R.id.chattextView);


            // 홀더 생성 및 Tag로 등록
            chatHolder = new ChatHolder();
            chatHolder.layout = layout;
            chatHolder.c_textView = chattextView;

            convertView.setTag(chatHolder);
        }
        else {
            chatHolder = (ChatHolder) convertView.getTag();
            layout = chatHolder.layout;
            chattextView = chatHolder.c_textView;
        }


        // Text 등록
        chattextView.setText(chat_list.get(i).text);

        if (chat_list.get(i).talker == 0) {
            chattextView.setBackgroundResource(R.drawable.chatbubble_left); // 챗봇이 말하는 상황의 백그라운드 이미지
            layout.setGravity(Gravity.START);
        }
        else if (chat_list.get(i).talker == 1) {
            chattextView.setBackgroundResource(R.drawable.chatbubble_right); // 사용자가 말하는 상황의 백그라운드 이미지
            layout.setGravity(Gravity.END);
        }


        // 리스트 아이템을 터치 했을 때 이벤트 발생
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 클릭 : " + chat_list.get(idx).text, Toast.LENGTH_SHORT).show();
            }
        });


        // 리스트 아이템을 길게 터치 했을때 이벤트 발생
        convertView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 롱 클릭 : " + chat_list.get(idx).text, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return convertView;
    }
}
