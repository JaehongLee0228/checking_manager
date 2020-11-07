package com.checking_manager.checking_manager.checking_sequence;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.checking_manager.checking_manager.R;

public class checking_page_showresult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking_page_showresult);

        Intent intent = getIntent();
        String[] statusResult = intent.getExtras().getStringArray("statusResult");
        String[] specialNote = intent.getExtras().getStringArray("specialNote");

        int a = 1;


    }
}