package com.example.hanchat.module;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.hanchat.R;
import com.example.hanchat.data.calendar.Day;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private final ArrayList<Day> list;
    private final LayoutInflater inflater;
    ViewHolder holder = null;

    public GridAdapter(Context context, ArrayList<Day> dayList) {
        this.list = dayList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public Day getToday(int i){
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int i) {//position
        return list.get(i).getDay();
    }

    @Override
    public long getItemId(int i) {//position
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        if (view == null) {
            view = inflater.inflate(R.layout.recycler_calendar_item, viewGroup, false);
            holder = new ViewHolder();
            holder.dayItem = (ConstraintLayout) view.findViewById(R.id.dayItem);

            holder.tv_day = (TextView) view.findViewById(R.id.tv_day);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();

        }
        holder.tv_day.setText(getItem(i));
        list.get(i).h(holder);

        return view;
    }



    public class ViewHolder {
        public ConstraintLayout dayItem;
        TextView tv_day;



    }
}