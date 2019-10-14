package com.example.hanchat.ui.group.grouppostlist;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.hanchat.R;
import com.example.hanchat.data.group.GroupPost;
import com.example.hanchat.module.RecyclerAdapter;
import com.example.hanchat.module.RecyclerManager;
import com.example.hanchat.ui.group.GroupMainFragment;

public class GroupPostlistFragment extends Fragment {

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
