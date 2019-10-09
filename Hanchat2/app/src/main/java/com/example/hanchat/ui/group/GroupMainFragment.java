package com.example.hanchat.ui.group;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.hanchat.GroupMainActivity;
import com.example.hanchat.GroupPostActivity;
import com.example.hanchat.R;
import com.example.hanchat.data.group.GroupPost;
import com.example.hanchat.module.RecyclerAdapter;
import com.example.hanchat.module.RecyclerManager;

import java.util.ArrayList;

public class GroupMainFragment extends Fragment {

    private GroupMainViewModel mViewModel;
    private RecyclerAdapter<GroupPost> adapter;

    public static GroupMainFragment newInstance() {
        return new GroupMainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_group_main, container, false);
        RecyclerView rv = view.findViewById(R.id.Rview_Groupmain);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerAdapter<>();
        adapter.setItemViewCreateAction(new RecyclerManager.ItemViewCreateAction() {
            @Override
            public void ItemViewCreated(final RecyclerManager.ViewHolder holder, int itemType) {
                if(itemType == RecyclerAdapter.GROUPPOST){
                    TextView tv_content = ((GroupPost)holder.getItem()).getBinding().ContentGroupPostContent;
                    tv_content.setMaxLines(8);
                    tv_content.setEllipsize(TextUtils.TruncateAt.END);
                    tv_content.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            TextView tv = ((GroupPost)holder.getItem()).getBinding().ContentGroupPostContent;
                            if(tv.getLayout() == null) return false;
                            int ellipseCount = tv.getLayout().getEllipsisCount(tv.getLineCount() - 1);
                            if(ellipseCount > 0){
                                ((GroupPost)holder.getItem()).getBinding().ContentMore.setVisibility(View.VISIBLE);
                            }
                            else{
                                ((GroupPost)holder.getItem()).getBinding().ContentMore.setVisibility(View.GONE);
                            }
                            //tv_content.getViewTreeObserver().removeOnPreDrawListener(this);
                            return true;
                        }
                    });
                }
            }
        });

        adapter.setItemViewBindAction(new RecyclerManager.ItemViewBindAction() {
            @Override
            public void ItemViewBinded(RecyclerManager.ViewHolder holder, final RecyclerManager.RecyclerItem item) {
                if(item.getViewType() == RecyclerAdapter.GROUPPOST){
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), GroupPostActivity.class);
                            GroupPost post = (GroupPost)item;
                            intent.putExtra("GroupName", post.getGroupName());
                            intent.putExtra("WriterName", post.getWriterName());
                            intent.putExtra("Content", post.getContent());

                            startActivity(intent);
                        }
                    });
                }
            }
        });

        adapter.setLastPositionAction(new RecyclerManager.LastPositionAction() {
            @Override
            public void lastPositionFunc(RecyclerManager adapter) {
                addData();
            }
        });

        rv.setAdapter(adapter);
        addData();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(GroupMainViewModel.class);
        // TODO: Use the ViewModel
    }



    private void addData(){

        ArrayList<GroupPost> list = new ArrayList<>();

        for(int i = 0;i < 20; i++){
            GroupPost gp = new GroupPost(getContext());
            gp.set(String.format("Group %d", 20 - i), String.format("Writer %d", i),
                    //String.format("Content %d", i * 10));
                    "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh" +
                            "hehehhehehehehehehehehehehehehehehheehehehheehehhehehhehehehehehehehehehehehehehehheehehehheeheh");
            list.add(gp);
            if(i % 3 == 0){
                gp.set(String.format("Group %d", 20 - i), String.format("Writer %d", i),
                        String.format("Content %d", i * 10));
            }
        }
        adapter.addItemwithNotify(list);
    }
}
