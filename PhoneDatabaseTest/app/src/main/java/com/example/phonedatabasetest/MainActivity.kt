package com.example.phonedatabasetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    EditText editName, editNumber;
    Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainactivity)
        editName=(EditText)findViewById(R.id.edit_name);

    }
}
