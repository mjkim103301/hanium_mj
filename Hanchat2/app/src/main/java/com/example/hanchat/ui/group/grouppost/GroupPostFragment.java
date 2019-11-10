package com.example.hanchat.ui.group.grouppost;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hanchat.R;
import com.example.hanchat.module.adapter.RecyclerAdapter;

/*
    그룹 포스트와 그 댓글을 보여주는 프래그먼트
    뷰모델은 리사이클러뷰 데이터 저장
 */
public class GroupPostFragment extends Fragment {

    private GroupPostViewModel mViewModel;

    public static GroupPostFragment newInstance() {
        return new GroupPostFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_only_rcyclerview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //보류
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
         mViewModel.saveState();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(GroupPostViewModel.class);

        GroupPostFragmentArgs args = GroupPostFragmentArgs.fromBundle(getArguments());
        if(args.getIsNewPost()){
            mViewModel.clear();
            mViewModel.setValue(args.getGroupName(), args.getWriterName(), args.getContent());
        }
        else{

        }
        RecyclerView rv = getView().findViewById(R.id.Rview_only_rcyclerview);
        RecyclerAdapter adapter = new RecyclerAdapter();
        rv.setAdapter(adapter);

        mViewModel.setRecyclerAdapter(adapter);

    }

}
