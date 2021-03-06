package com.checking_manager.checking_manager.checking_sequence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.checking_manager.checking_manager.Main_sum;
import com.checking_manager.checking_manager.My_Groups.Before_enter;
import com.checking_manager.checking_manager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class checking_page_showresult extends AppCompatActivity {

    private String stuff_name, pos, group_name;
    private int index_num = 0;

    private FirebaseDatabase database;
    private DatabaseReference reference, reference2;
    String[] statusResult;
    String[] specialNote;
    ArrayList<String> questionBox;

    String inDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking_page_showresult);

        Intent intent = getIntent();
        stuff_name = intent.getExtras().getString("stuff_name");
        pos = intent.getExtras().getString("pos_name");
        group_name = intent.getExtras().getString("group_name");
        statusResult = intent.getExtras().getStringArray("statusResult");
        specialNote = intent.getExtras().getStringArray("specialNote");
        questionBox = intent.getExtras().getStringArrayList("questionBox");

        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);

        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
        inDate = mFormat.format(mDate);


        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Checked_Table").child(group_name).child(stuff_name).child(pos);
        reference2 = database.getReference("Groups").child(group_name).child("stuff").child(stuff_name).child("position").child(pos);

        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.getKey().toString().equals("checked_index")){
                        index_num = Integer.parseInt( ds.getValue().toString());
                        ++index_num;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button checkingFinishButton = (Button)findViewById(R.id.checkingFinishButton);

        checkingFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, resultTable> resultBox = new HashMap<>();
                for(int i = 0; i < questionBox.size(); ++i){
                    if(specialNote[i] == null || specialNote[i].equals("")){
                        specialNote[i] = "없음";
                    }
                    resultBox.put(questionBox.get(i), new resultTable(statusResult[i], specialNote[i]));
                }

                reference.child(String.valueOf(index_num)).child("contents").setValue(resultBox);
                reference.child(String.valueOf(index_num)).child("date").setValue(inDate);
                indexUpdate(index_num, inDate);

                Intent intent = new Intent(getApplicationContext(), Main_sum.returnClass);
                intent.putExtra("group_name", group_name);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

    }

    void indexUpdate(int updateValue, String nDate){
        reference2.child("checked_index").setValue(updateValue);
        reference2.child("checked").child(String.valueOf(updateValue)).setValue(nDate);
    }
}