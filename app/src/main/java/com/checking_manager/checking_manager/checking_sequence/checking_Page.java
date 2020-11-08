package com.checking_manager.checking_manager.checking_sequence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.checking_manager.checking_manager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class checking_Page extends AppCompatActivity {

    private String stuff_name, pos, group_name;
    private String index_num;
    private FirebaseDatabase database;
    private DatabaseReference reference;


    public static ArrayList<String> question = new ArrayList<String>();
    private  int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_stuff_detail);//뷰필요없음.

        stuff_name = getIntent().getExtras().getString("stuff_name");
        pos = getIntent().getExtras().getString("pos_name");
        group_name = getIntent().getExtras().getString("group_name");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Groups").child(group_name).child("stuff").child(stuff_name).child("position").child(pos);

        reference.child("contents").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String ques = ds.getValue().toString();

                    question.add(ques);
                    ++size;
                }
                Intent intent = new Intent(getApplicationContext(), checking_page2.class);
                intent.putExtra("size", size);
                intent.putExtra("questionArray", question);
                intent.putExtra("stuff_name", stuff_name);
                intent.putExtra("pos_name", pos);
                intent.putExtra("group_name", group_name);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}