package com.example.hanchat.ui.more;

import androidx.lifecycle.ViewModelProviders;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hanchat.R;

public class MoreFragment extends Fragment {

    private MoreViewModel mViewModel;

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    View view;
    EditText editText_id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_more, container, false);

        // 길게 클릭 시 아이디 복사
        editText_id = view.findViewById(R.id.editText_id);
        editText_id.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String user_id = editText_id.getText().toString();
                setOnClipBoard(user_id);
                Toast.makeText(getContext(), "Long click to copy ID", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MoreViewModel.class);
        // TODO: Use the ViewModel
    }



    // 클립보드에 아이디 복사
    public void setOnClipBoard(String user_id) {
        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Clip Data", user_id);
        clipboardManager.setPrimaryClip(clipData);
    }
}
