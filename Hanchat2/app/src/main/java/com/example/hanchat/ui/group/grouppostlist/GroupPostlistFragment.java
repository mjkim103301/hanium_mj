package com.example.hanchat.ui.group.grouppostlist;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.hanchat.R;
import com.example.hanchat.data.group.GroupPost;
import com.example.hanchat.module.adapter.RecyclerAdapter;
import com.example.hanchat.module.adapter.RecyclerManager;

/*
    포스트들의 리스트를 보여주는 프래그먼트
    리사이클러뷰에 액션 더함
    1. 글이 길면 줄이기
    2. 터치하면 그 정보들을 args로 그룹 포스트 프래그먼트 전환
    3. 맨 밑으로 가면 다음 정보들 얻어오기

    뷰모델은 RestroeRecycler 뷰모델 상속
 */
public class GroupPostlistFragment extends Fragment {
    // TODO: 2019-11-06 리스트에 그룹 수를 표시하고 그룹 수를 터치하면 그룹의 리스트 출력

    private GroupPostlistViewModel mViewModel;

    public static GroupPostlistFragment newInstance() {
        return new GroupPostlistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_only_rcyclerview, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(GroupPostlistViewModel.class);
        RecyclerView rv = getView().findViewById(R.id.Rview_only_rcyclerview);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerAdapter adapter = new RecyclerAdapter();
        adapter.setItemViewCreateAction(new RecyclerManager.ItemViewCreateAction() {
            @Override
            public void ItemViewCreated(final RecyclerManager.ViewHolder holder, int itemType) {
                if(itemType == RecyclerAdapter.GROUPPOST){
                    TextView tv_content = ((GroupPost)holder.getItem()).getBinding().ContentGroupPostContent;
                    tv_content.setMaxLines(8);
                    tv_content.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            TextView tv = ((GroupPost)holder.getItem()).getBinding().ContentGroupPostContent;
                            if(tv.getLayout() == null) return;
                            int ellipseCount = tv.getLayout().getEllipsisCount(tv.getLineCount() - 1);
                            if(ellipseCount > 0){
                                ((GroupPost)holder.getItem()).getBinding().ContentMore.setVisibility(View.VISIBLE);
                            }
                            else{
                                ((GroupPost)holder.getItem()).getBinding().ContentMore.setVisibility(View.GONE);
                            }
                        }
                    });
                    tv_content.setEllipsize(TextUtils.TruncateAt.END);

//                    tv_content.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                        @Override
//                        public boolean onPreDraw() {
//                            TextView tv = ((GroupPost)holder.getItem()).getBinding().ContentGroupPostContent;
//                            if(tv.getLayout() == null) return false;
//                            int ellipseCount = tv.getLayout().getEllipsisCount(tv.getLineCount() - 1);
//                            if(ellipseCount > 0){
//                                ((GroupPost)holder.getItem()).getBinding().ContentMore.setVisibility(View.VISIBLE);
//                            }
//                            else{
//                                ((GroupPost)holder.getItem()).getBinding().ContentMore.setVisibility(View.GONE);
//                            }
//                            //tv_content.getViewTreeObserver().removeOnPreDrawListener(this);
//                            return true;
//                        }
//                    });
                }
            }
        });
        //getParentFragment()
        adapter.setItemViewBindAction(new RecyclerManager.ItemViewBindAction() {
            @Override
            public void ItemViewBinded(RecyclerManager.ViewHolder holder, final RecyclerManager.RecyclerItem item) {
                if(item.getViewType() == RecyclerAdapter.GROUPPOST){
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            GroupPost post = (GroupPost)item;
                            NavController navController = Navigation.findNavController(getView());
                            GroupPostlistFragmentDirections.ActionSubnavGroupMainToSubnavGroupPost action =
                                    GroupPostlistFragmentDirections.actionSubnavGroupMainToSubnavGroupPost(
                                            post.getGroupName(), post.getWriterName(), post.getContent(), true);
                            navController.navigate(action);
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
        mViewModel.setRecyclerAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.saveState();
    }

    private void addData(){
        mViewModel.addData();
    }



}
