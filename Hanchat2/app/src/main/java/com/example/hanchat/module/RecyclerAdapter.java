package com.example.hanchat.module;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanchat.R;

import java.util.ArrayList;


//T에 Recycleritem을 상속받는 데이터 클래스
public class RecyclerAdapter<T extends RecyclerAdapter.RecyclerItem> extends RecyclerView.Adapter {

    public static final int GROUPPOST = 0;
    public static final int POSTCOMMENT = 1;


    //이 어댑터에 사용할 아이템에 상속받아야 할 인터페이스
    public interface RecyclerItem {
        int getViewType();

        void setRecyclerContent(final View itemView);
    }

    //아이템뷰에 추가 액션 붙이기
    public interface ItemViewAction {
        void setItemView(final View itemView, final RecyclerItem item, final int viewType);
    }

    //스크롤이 마지막으로 갔을때의 액션
    public interface LastPositionAction {
        void lastPositionFunc(final RecyclerAdapter adapter);
    }

    //뷰홀더
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setContent(T content) {
            content.setRecyclerContent(itemView);
        }
    }


    private ArrayList<T> items = new ArrayList<>();
    ItemViewAction itemViewFunc = null;
    LastPositionAction lastPositionFunc = null;


    public RecyclerAdapter() {
        super();
    }
    public RecyclerAdapter(ArrayList<T> list) {
        super();
        addContent(list);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @LayoutRes int LayoutResId = 0;
        switch (viewType) {
            case GROUPPOST:
                LayoutResId = R.layout.content_grouppost;
                break;
            case POSTCOMMENT:
                LayoutResId = R.layout.content_postcomment;
        }
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(LayoutResId, parent, false);


        RecyclerAdapter.ViewHolder vh = new RecyclerAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        T content = items.get(position);
        ((RecyclerAdapter.ViewHolder) holder).setContent(content);
        if (itemViewFunc != null) {
            itemViewFunc.setItemView(((ViewHolder) holder).itemView, content, getItemViewType(position));
        }

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int currentPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastCompletelyVisibleItemPosition();
                if (currentPosition == getItemCount() - 3)
                    lastPositionScrolled();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }


    public void setItemViewAction(ItemViewAction func) {
        this.itemViewFunc = func;
    }
    public void setLastPositionAction(LastPositionAction func) {
        this.lastPositionFunc = func;
    }

    private void lastPositionScrolled() {
        if (lastPositionFunc != null) {
            lastPositionFunc.lastPositionFunc(this);
        }
    }


    public void addContent(ArrayList<T> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }
    public void addContent(T item){
        items.add(item);
        notifyDataSetChanged();
    }

}
