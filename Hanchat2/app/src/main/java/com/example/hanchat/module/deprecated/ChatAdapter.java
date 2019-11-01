package com.example.hanchat.module.deprecated;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanchat.R;

import java.util.ArrayList;

@Deprecated
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
    public ChatAdapter() {
        chat_list = new ArrayList<>();
    }

    // 어댑터 대화 추가
    public void add(int _talker, String _text) {
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
        LinearLayout linearLayout;
        ImageView hanrang;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final int idx = i;
        final Context context = parent.getContext();

        ChatHolder chatHolder = null;
        ImageView hanrang = null;
        LinearLayout linearLayout = null;
        TextView chattextView = null;

        //  convertView == null : 현재 화면에 보이지 않음
        if (convertView == null) {
            // view == null : chatHolder 가져와 보여줌
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_item, parent, false);

            linearLayout = (LinearLayout) convertView.findViewById(R.id.linearLayout1);
            chattextView = (TextView) convertView.findViewById(R.id.chattextView);
            hanrang = convertView.findViewById(R.id.hanrang);


            // 홀더 생성 및 Tag로 등록
            chatHolder = new ChatHolder();
            chatHolder.linearLayout = linearLayout;
            chatHolder.c_textView = chattextView;
            chatHolder.hanrang = hanrang;

            convertView.setTag(chatHolder);
        }
        else {
            chatHolder = (ChatHolder) convertView.getTag();
            linearLayout = chatHolder.linearLayout;
            chattextView = chatHolder.c_textView;
            hanrang = chatHolder.hanrang;
        }


        // Text 등록
        chattextView.setText(chat_list.get(i).text);

        if (chat_list.get(i).talker == 0) {
            hanrang.setVisibility(View.VISIBLE);
            chattextView.setBackgroundResource(R.drawable.chatbubble_left); // 챗봇이 말하는 상황의 백그라운드 이미지
            linearLayout.setGravity(Gravity.START);
        }
        else if (chat_list.get(i).talker == 1) {

            hanrang.setVisibility(View.GONE);
            chattextView.setBackgroundResource(R.drawable.chatbubble_right); // 사용자가 말하는 상황의 백그라운드 이미지
            linearLayout.setGravity(Gravity.END);
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
