package com.example.hanchat.module.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanchat.data.EmptyData;

import java.util.ArrayList;

import static com.example.hanchat.module.adapter.RecyclerAdapter.EMPTY;


//T에 Recycleritem을 상속받는 데이터 클래스
public abstract class RecyclerManager extends RecyclerView.Adapter {

    //이 어댑터에 사용할 아이템에 상속받아야 할 인터페이스
    public interface RecyclerItem {
        int getViewType();

        void setRecyclerContent(View itemView);
    }

    //아이템뷰에 추가 액션 한번만 붙이기
    public interface ItemViewCreateAction {
        void ItemViewCreated(RecyclerManager.ViewHolder holder, int itemType);
    }

    //아이템뷰에 추가 액션 붙이기
    public interface ItemViewBindAction {
        void ItemViewBinded(RecyclerManager.ViewHolder holder, RecyclerItem item);
    }

    //스크롤이 마지막으로 갔을때의 액션
    public interface LastPositionAction {
        void lastPositionFunc(RecyclerManager adapter);
    }

    //뷰홀더
    public class ViewHolder extends RecyclerView.ViewHolder {
        boolean isSetted = false;
        ItemViewCreateAction createAction;
        RecyclerItem item = null;

        public ViewHolder(@NonNull View itemView, ItemViewCreateAction createAction) {
            super(itemView);
            this.createAction = createAction;
        }

        public void setItem(RecyclerItem item) {
            item.setRecyclerContent(itemView);
            this.item = item;
            if(!isSetted){
                isSetted = true;
                if(createAction != null)
                    createAction.ItemViewCreated(this, item.getViewType());
            }
        }

        public RecyclerItem getItem(){
            return item;
        }

    }


    private ArrayList<RecyclerItem> items;
    private ItemViewCreateAction itemViewCreateFunc = null;
    private ItemViewBindAction itemViewBindFunc = null;
    private LastPositionAction lastPositionFunc = null;
    protected RecyclerView parentView = null;


    public RecyclerManager() {
        super();
        items = new ArrayList<>();
    }

    public RecyclerManager(ArrayList<RecyclerItem> list) {
        super();
        addItemwithNotify(list);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @LayoutRes int LayoutResId = getLayoutRes(viewType);
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(LayoutResId, parent, false);
        RecyclerManager.ViewHolder vh = new RecyclerManager.ViewHolder(view, itemViewCreateFunc);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecyclerItem content;
        content = items.get(position);
        ((RecyclerManager.ViewHolder) holder).setItem(content);
        if (itemViewBindFunc != null && getItemViewType(position) != EMPTY) {
            itemViewBindFunc.ItemViewBinded((RecyclerManager.ViewHolder) holder, content);
        }

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.parentView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        if(lastPositionFunc != null){
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                boolean isworking = false;
                @Override
                public void onScrolled(@NonNull final RecyclerView recyclerView, int dx, int dy) {
                    if(!isworking){
                        final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        final int currentPosition = manager.findLastCompletelyVisibleItemPosition();
                        if (currentPosition > getItemCount() - 2){
                            isworking = true;
                            //RecyclerManager.this.addItemwithNotify(new EmptyData());
                            isworking = !lastPositionScrolled();
                            if(isworking)
                                recyclerView.removeOnScrollListener(this);
                        }
                    }
                    super.onScrolled(recyclerView, dx, dy);

                }
            });
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.parentView = null;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    public void setItemViewCreateAction(ItemViewCreateAction func){
        this.itemViewCreateFunc = func;
    }

    public void setItemViewBindAction(ItemViewBindAction func) {
        this.itemViewBindFunc = func;
    }

    public void setLastPositionAction(LastPositionAction func) {
        this.lastPositionFunc = func;
    }

    private boolean lastPositionScrolled() {
        if (lastPositionFunc != null) {
            lastPositionFunc.lastPositionFunc(this);
            return true;
        }
        return false;
    }

    public void itemChanged(final int position){
        if(parentView != null){
            parentView.post(new Runnable() {
                public void run() {
                    RecyclerManager.this.notifyItemInserted(position);
                }
            });
        }
        else
            notifyDataSetChanged();
    }

    public void setList(ArrayList<RecyclerItem> list){
        items = list;
    }

    public void addItem(ArrayList<RecyclerItem> list){
        items.addAll(list);
    }
    public void addItem(RecyclerItem item){
        items.add(item);
    }

    public void addItemwithNotify(ArrayList<RecyclerItem> list) {
        int insertposition = items.size();
        items.addAll(list);
        itemChanged(insertposition);
    }

    public void addItemwithNotify(RecyclerItem item) {
        int insertposition = items.size();
        items.add(item);
        itemChanged(insertposition);
    }

    @LayoutRes
    protected abstract int getLayoutRes(int viewType);
    public RecyclerView getParentView(){
        return parentView;
    }
}
