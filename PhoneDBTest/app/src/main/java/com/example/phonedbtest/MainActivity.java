package com.example.phonedbtest;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText edit_titie, edit_category, edit_starttime, edit_endtime, edit_memo, edit_place;
    Button btn_insert, btn_update, btn_delete, btn_show;
    TextView tv_result;

    myDBHelper myHelper;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("일정관리 DB 테스트");
        edit_titie = (EditText) findViewById(R.id.edit_title);
        edit_category = (EditText) findViewById(R.id.edit_category);
        edit_starttime = (EditText) findViewById(R.id.edit_starttime);
        edit_endtime = (EditText) findViewById(R.id.edit_endtime);
        edit_memo = (EditText) findViewById(R.id.edit_memo);
        edit_place = (EditText) findViewById(R.id.edit_place);

        btn_insert = (Button) findViewById(R.id.btn_insert);
        btn_update = (Button) findViewById(R.id.btn_update);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_show = (Button) findViewById(R.id.btn_show);

        tv_result=(TextView)findViewById(R.id.tv_result);

        myHelper = new myDBHelper(getApplicationContext(), "test.db", null, 1);
        btn_insert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String title = edit_titie.getText().toString();
                String category = edit_category.getText().toString();
                String starttime = edit_starttime.getText().toString();
                String endtime = edit_endtime.getText().toString();
                String memo = edit_memo.getText().toString();
                String place = edit_place.getText().toString();

                myHelper.insert(title, category, starttime, endtime, memo, place);
                Toast.makeText(getApplicationContext(), "입력됨", Toast.LENGTH_LONG).show();

            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edit_titie.getText().toString();
                String category = edit_category.getText().toString();
                String starttime = edit_starttime.getText().toString();
                String endtime = edit_endtime.getText().toString();
                String memo = edit_memo.getText().toString();
                String place = edit_place.getText().toString();

                myHelper.update(title, category, starttime, endtime, memo, place);
                Toast.makeText(getApplicationContext(), "수정됨", Toast.LENGTH_LONG).show();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edit_titie.getText().toString();
                String category = edit_category.getText().toString();
                String starttime = edit_starttime.getText().toString();
                String endtime = edit_endtime.getText().toString();
                String memo = edit_memo.getText().toString();
                String place = edit_place.getText().toString();

                myHelper.delete(title, category, starttime, endtime, memo, place);
                Toast.makeText(getApplicationContext(), "삭제됨", Toast.LENGTH_LONG).show();
            }
        });

        btn_show.setOnClickListener(new View.OnClickListener() {//조회 버튼 클릭
            @Override
            public void onClick(View v) {
                tv_result.setText(myHelper.getResult());
            }
        });
    }
//    public class myDBHelper extends SQLiteOpenHelper{
//        public myDBHelper(Context context){
//
//            super(context, "test", null, 1);
//        }
//        @Override
//        public void onCreate(SQLiteDatabase db){//테이블 새로 생성
//           db.execSQL("create table id ( userID char(20) primary key);");
//        }
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){//테이블 삭제한 후 다시 생성
//            db.execSQL("drop table if exists id");
//        }
//    }




}

