package com.example.hanchat.ui.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.example.hanchat.R;
import com.example.hanchat.data.group.GroupPost;
import com.example.hanchat.ui.group.grouppost.GroupPostFragmentArgs;
import com.example.hanchat.ui.group.grouppostlist.GroupPostlistFragmentDirections;

/*
    그룹 프래그먼트 최상위
    이 안에 그룹 포스트 프래그먼트 / 그룹 포스트 리스트 프래그먼트 포함
    만약 그룹포스트를 선택하여 들어가게 되었으면 그 정보를 뷰모델에 저장
    그룹포스트에서 나오면 뷰모델에 저장된 정보 클리어
    onCreate에서 뷰모델에 정보가 저장되었는지 확인
    -> 저장되어 있으면 포스트 프래그먼트에 args 전달하여 띄움
    -> 없으면 리스트 프래그먼트 띄움
 */
public class GroupMainFragment extends Fragment {

    private static GroupMainViewModel mViewModel;
    private static View view;

    public static GroupMainFragment newInstance() {
        return new GroupMainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(GroupMainViewModel.class);

        NavController navController = Navigation.findNavController(getView().findViewById(R.id.subfragment_group));

        Bundle bundle = mViewModel.Restore();
        if(bundle != null){
            GroupPostFragmentArgs args = GroupPostFragmentArgs.fromBundle(bundle);
            GroupPostlistFragmentDirections.ActionSubnavGroupMainToSubnavGroupPost action =
                GroupPostlistFragmentDirections.actionSubnavGroupMainToSubnavGroupPost(
                        args.getGroupName(), args.getWriterName(), args.getContent(), false);
            navController.navigate(action);
        }
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.subnav_group_post){
                    mViewModel.save(arguments);
                }
                else{
                    mViewModel.clear();
                }
            }
        });
    }

}
