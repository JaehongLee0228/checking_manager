package com.checking_manager.checking_manager.checking_sequence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.checking_manager.checking_manager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class checking_page2 extends AppCompatActivity {

    public static String [] statusResult;
    public static String [] specialNote;

    int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking_page2);

        statusResult = new String [100];
        specialNote = new String [100];

        Intent intent = getIntent();
        ArrayList<String> questionBox  = intent.getExtras().getStringArrayList("questionArray");
        size = intent.getExtras().getInt("size");
        ViewPager vp = (ViewPager)findViewById(R.id.checkingPageViewPager);
        checking_page_adapter adapterA = new checking_page_adapter(getSupportFragmentManager(), size, questionBox);
        vp.setAdapter(adapterA);

        FloatingActionButton fab = findViewById(R.id.checkingPageFloatingActionButton);
        fab.setOnClickListener(new checking_page2.FABClickListener());

    }

    class FABClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            for(int i = 0; i < size; ++i){
                if(statusResult[i] == ""){
                    //돌아가서 수정
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), checking_page_showresult.class);
                    intent.putExtra("statusResult", statusResult);
                    intent.putExtra("specialNote", specialNote);
                    startActivity(intent);
                }
            }
        }
    }
}