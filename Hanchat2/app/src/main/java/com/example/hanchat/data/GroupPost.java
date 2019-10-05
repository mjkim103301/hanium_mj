package com.example.hanchat.data;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hanchat.R;
import com.example.hanchat.module.RecyclerAdapter;

import org.w3c.dom.Text;

public class GroupPost implements RecyclerAdapter.Recycleritem {
    Uri groupProfileImage = null;
    String groupName;
    String writerName;
    String content;

    Context context;

    final int maxLInes = 5;

    public GroupPost(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void setRecyclerContent(View itemView) {
        if(groupProfileImage != null){
            ((ImageView)itemView.findViewById(R.id.Content_GroupPost_GroupImage)).setImageURI(groupProfileImage);
        }
        ((TextView)itemView.findViewById(R.id.Content_GroupPost_WriterName)).setText(writerName);
        ((TextView)itemView.findViewById(R.id.Content_GroupPost_GroupName)).setText(groupName);
        TextView view_Content = (TextView)itemView.findViewById(R.id.Content_GroupPost_Content);
        view_Content.setMaxLines(maxLInes);
        view_Content.setEllipsize(TextUtils.TruncateAt.END);
        view_Content.setText(content);
        if(view_Content.getLineCount() > maxLInes){
            TextView tv_more = new TextView(context);

        }
    }

    public void setGroupProfileImage(Uri imagepath){
        groupProfileImage = imagepath;
    }

    public void set(String groupName, String writerName, String content){
        this.groupName = groupName;
        this.writerName = writerName;
        this.content = content;
    }
}
