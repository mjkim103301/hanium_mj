package com.example.hanchat.data.calendar;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Month   {

    ArrayList<Day> calendarList;
    Calendar now=Calendar.getInstance();//calendar  객체 얻어오기//오늘

    public int year=now.get(Calendar.YEAR);//올해
    public int month=now.get(Calendar.MONTH);//이번달
    int day=now.get(Calendar.DATE);//오늘
    int dayOfWeek=now.get(Calendar.DAY_OF_WEEK);//오늘이 이번주의 몇번째 날인지 알려줌


    GregorianCalendar cal;
    GregorianCalendar cal2;




    public ArrayList<Day> getCalendarList(){
        return calendarList;
    }

    public void setCalendarList(int real_month){//GregorianCalendar cal){
        calendarList=new ArrayList<>();//일 저장할 리스트
        cal=new GregorianCalendar(year, month+real_month, 1, 0, 0);//gregorianCalendar 생성
        int StartDay=cal.get(cal.DAY_OF_WEEK)-1;
        int lastDay=cal.getActualMaximum(Calendar.DATE);//해당 월의 마지막 일 반환
        int sum=StartDay+lastDay;

        cal2=new GregorianCalendar(year, month+real_month-1, 1, 0, 0);//gregorianCalendar 생성
        int lastDay2=cal2.getActualMaximum(Calendar.DATE)+1;//전 달의 마지막 날 반환
        int j=1;//마지막 날 이후에 사용될 변수

        for(int i=StartDay;i>0;i--){//1일 전 날짜
            calendarList.add(new Day(lastDay2-i));

        }
        for(int i=1;i<=lastDay;i++){//실제 날짜
            calendarList.add(new Day(i));
        }
        for(int i=sum;i<42;i++){
            calendarList.add(new Day(j));//마지막날 이후
            j++;
        }

    }



}