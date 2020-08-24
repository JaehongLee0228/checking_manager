package com.checking_manager.checking_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Before_enter extends AppCompatActivity {

    private BackPressCloseHandler backPressCloseHandler;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.before_enter);

        Button Groupbt = findViewById(R.id.test_bt);
        Groupbt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext()," ~ 그룹으로 이동합니다",Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference("Members").child("yyynavercom").child("모공 파이팅").setValue("admin");
                startActivity(new Intent(Before_enter.this, Main_sum.class));
            }
        });
    }


    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}