package com.example.hanchat.data.calendar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import com.example.hanchat.R;
import com.example.hanchat.databinding.RecyclerRecyclerCalendarBinding;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Month  extends AppCompatActivity  {

    ArrayList<Day> calendarList;
    Calendar now=Calendar.getInstance();//calendar  객체 얻어오기//오늘

    int year=now.get(Calendar.YEAR);//올해
    int month=now.get(Calendar.MONTH);//이번달
    int day=now.get(Calendar.DATE);//오늘
    int dayOfWeek=now.get(Calendar.DAY_OF_WEEK);//오늘이 이번주의 몇번째 날인지 알려줌
    int lastDay=now.getActualMaximum(Calendar.DATE);//해당 월의 마지막 일 반환

    GregorianCalendar cal=new GregorianCalendar(year, month, 1, 0, 0);//gregorianCalendar 생성
    int StartDay=cal.get(cal.DAY_OF_WEEK)-1;



    GridView gridView;//그리드 뷰
    GridAdapter gridAdapter;//그리드 어댑터
   // RecyclerRecyclerCalendarBinding binding;//바인딩

    public ArrayList<Day> getCalendarList(){
        return calendarList;
    }
 /*   public  Month(){
        RecyclerRecyclerCalendarBinding binding= DataBindingUtil.setContentView(this, R.layout.recycler_recycler_calendar);
       binding.setModel(this);
        gridView=(GridView) findViewById(R.id.gridView);
        setCalendarList();//날짜를 세팅해준다.
        gridAdapter=new GridAdapter(getApplicationContext(), calendarList);


        gridView.setAdapter(gridAdapter);
    }*/
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
       setContentView(R.layout.recycler_recycler_calendar);

        gridView=(GridView) findViewById(R.id.gridView);

        setCalendarList();//날짜를 세팅해준다.
        gridAdapter=new GridAdapter(getApplicationContext(), calendarList);

        gridView.setAdapter(gridAdapter);



    }
    public void setCalendarList(){//GregorianCalendar cal){
        calendarList=new ArrayList<>();//일 저장할 리스트
        int sum=StartDay+lastDay;
        for(int i=0;i<StartDay;i++){//1일 전 날짜
            calendarList.add(new Day(0));
        }
        for(int i=1;i<=lastDay;i++){//실제 날짜
            calendarList.add(new Day(i));
        }
        for(int i=sum;i<42;i++){
            calendarList.add(new Day(0));//마지막날 이후
        }

    }
    class GridAdapter extends BaseAdapter{

        private final ArrayList<Day> list;
        private final LayoutInflater inflater;
        public GridAdapter(Context context,ArrayList<Day> dayList){
            this.list=dayList;
            this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            ViewHolder holder=null;

            if(view==null){
                view=inflater.inflate(R.layout.recycler_calendar_item, viewGroup, false);
                holder =new ViewHolder();
                holder.dayItem=(ConstraintLayout)view.findViewById(R.id.dayItem);

                holder.tv_day=(TextView)view.findViewById(R.id.tv_day);

                view.setTag(holder);

            }else{
                holder=(ViewHolder)view.getTag();

            }
            holder.tv_day.setText(getItem(i));
            return view;
        }
        public class ViewHolder{
            ConstraintLayout dayItem;
            TextView tv_day;

        }
    }


}
