package com.example.phonedbtest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText editName, editNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        editName=(EditText)findViewById(R.id.edit_name);
        editNumber=(EditText)findViewById(R.id.edit_number);
    }
    public class myDBHelper extends SQLiteOpenHelper{
        public myDBHelper(Context context){
            super(context, "groupDB", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db){

        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        }
    }
}
