package com.checking_manager.checking_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Before_enter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.before_enter);

        Button Groupbt = findViewById(R.id.test_bt);
        Groupbt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext()," ~ 그룹으로 이동합니다",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Before_enter.this, Main_sum.class));
            }
        });
    }
}