package com.example.hanchat.ui.chatbot;

import com.example.hanchat.data.chatting.OtherChatting;
import com.example.hanchat.data.chatting.UserChatting;
import com.example.hanchat.module.adapter.RecyclerAdapter;
import com.example.hanchat.ui.RestoreRecyclerFragment;
import com.example.hanchat.ui.RestoreRecyclerViewModel;

public class ChatbotViewModel extends RestoreRecyclerViewModel {
    // TODO: Implement the ViewModel
    public ChatbotViewModel(){
        recyclerList.add(new OtherChatting("안녕하세요 HANCHAT 임시UI입니다!"));
        recyclerList.add(new UserChatting("내일 7시에 은행동에서 친구랑 만나!"));
        recyclerList.add(new OtherChatting("이제 시작해볼까요?"));
    }

    @Override
    public void setRecyclerAdapterWithParentView(RestoreRecyclerFragment fragment, RecyclerAdapter recyclerAdapter) {
        super.setRecyclerAdapterWithParentView(fragment, recyclerAdapter);

    }

}
