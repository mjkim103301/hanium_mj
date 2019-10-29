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

    Bundle bundle = null;

    public void save(Bundle bundle){
        this.bundle = bundle;
    }

    public void clear(){
        bundle = null;
    }

    public Bundle Restore(){
        return bundle;
    }

}
