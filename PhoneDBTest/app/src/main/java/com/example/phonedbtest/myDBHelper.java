package com.example.phonedbtest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class myDBHelper extends SQLiteOpenHelper{
    SQLiteDatabase sqlDB;
    public myDBHelper(Context applicationContext, String s,SQLiteDatabase.CursorFactory factory, int i){

        super(applicationContext, s, factory, i);
    }
    @Override
    public void onCreate(SQLiteDatabase db){//테이블 새로 생성
        db.execSQL("create table schedule2 ( scTitle text, scCategory text, scStarttime text, scEndtime text, scMemo text, scPlace text);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){//테이블 삭제한 후 다시 생성
        db.execSQL("drop table if exists schedule2");
    }

    public void insert(String title, String category, String starttime, String endtime, String memo, String place){
        sqlDB=getWritableDatabase();
        sqlDB.execSQL("insert into schedule values('"+ title +"','"+category +"','"+starttime +"','"+endtime +"','"+memo +"','"+place +"');");
        sqlDB.close();

    }
    public void update(String title, String category, String starttime, String endtime, String memo, String place){
        sqlDB=getWritableDatabase();
        sqlDB.execSQL("update schedule set'"+ title +"','"+category +"','"+starttime +"','"+endtime +"','"+memo +"','"+place +"');");
        sqlDB.close();

    }
    public void delete(String title, String category, String starttime, String endtime, String memo, String place){
        sqlDB=getWritableDatabase();
        sqlDB.execSQL("delete from schedule where scTitle='"+ title +"',scCategory='"+category +"',scStarttime='"+starttime +"',scEndtime='"+endtime +"',scMemo='"+memo +"',scPlace'"+place +"';");
        sqlDB.close();

    }

    public String getResult(){
        sqlDB=getReadableDatabase();
        String result=" ";
        Cursor cursor;
        cursor=sqlDB.rawQuery("select * from schedule;", null);

        while(cursor.moveToNext()){
            result+=cursor.getString(0)+" "
                    +cursor.getString(1)+" "
                    +cursor.getString(2)+" "
                    +cursor.getString(3)+" "
                    +cursor.getString(4)+" "
                    +cursor.getString(5)+"\n";

        }
        return result;


    }
}
