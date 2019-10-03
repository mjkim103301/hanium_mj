package com.example.mycalendar;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private List<Object> mCalendarList;
    private final int Empty_type=1;
    private final int Day_type=2;
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView1;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            //textView1=itemView.findViewById(R.id.text1);
        }
    }
   public CalendarAdapter(List<Object> CalendarList){
        mCalendarList=CalendarList;
    }
    public void setcalendarList(List<Object> calendarList){

    }
    @NonNull
    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context=viewGroup.getContext();
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.recyclerview_item, viewGroup, false);
        CalendarAdapter.ViewHolder vh=new CalendarAdapter.ViewHolder(view);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.ViewHolder viewHolder, int i) {
        int text=mDate.get(i);
        viewHolder.textView1.setText(text);

    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }
}