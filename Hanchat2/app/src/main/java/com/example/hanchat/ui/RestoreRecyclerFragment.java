package com.example.hanchat.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class RestoreRecyclerFragment extends Fragment {
    interface RestoreRecycler{
        void onDestroyView();
    }

    RestoreRecycler restoreRecycler = null;

    public void setRestoreRecycler(RestoreRecycler restoreRecycler){
        this.restoreRecycler = restoreRecycler;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        restoreRecycler.onDestroyView();
    }

}
