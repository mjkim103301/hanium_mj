package com.example.hanchat.ui.group;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.example.hanchat.data.group.GroupPost;
import com.example.hanchat.ui.group.grouppost.GroupPostFragment;
import com.example.hanchat.ui.group.grouppost.GroupPostFragmentArgs;

public class GroupMainViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    NavDestination dest = null;
    GroupPost post = null;
    Bundle bundle = null;

    public void save(View view){
         NavController nc = Navigation.findNavController(view);
         dest = nc.getCurrentDestination();
    }

    public void save(GroupPost post){
        this.post = post;
    }

    public void save(Bundle bundle){
        this.bundle = bundle;
    }

    public void clear(){
        bundle = null;
    }

    public GroupPost Restore(View view){
        if(post != null){
            return post;
        }
        return null;
            //Navigation.findNavController(view).navigate(dest.getId());
    }

    public Bundle Restore(){
        return bundle;
    }

}
