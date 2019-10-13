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
