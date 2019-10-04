package com.example.mycalendar;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private List<Object> mCalendarList;
    private final int EMPTY_TYPE=1;
    private final int DAY_TYPE=2;

   public CalendarAdapter(List<Object> CalendarList){

        mCalendarList=CalendarList;
    }
    public void setcalendarList(List<Object> CalendarList){
        mCalendarList=CalendarList;
    }

    @Override
    public int getItemViewType(int position) { //뷰타입 나누기
        Object item = mCalendarList.get(position);
         if (item instanceof String) {
            return EMPTY_TYPE; // 비어있는 일자 타입
        } else {
            return DAY_TYPE; // 일자 타입

        }
    }


    @NonNull
    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       else if (viewType == EMPTY_TYPE) { //비어있는 일자 타입
            EmptyDayBinding binding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_day_empty, parent, false);
            return new EmptyViewHolder(binding);
        }
        DayItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_day, parent, false);// 일자 타입
        return new DayViewHolder(binding);

    }
    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.ViewHolder viewHolder, int i) {
        int viewType

    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView1;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            //textView1=itemView.findViewById(R.id.text1);
        }
    }
}