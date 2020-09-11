package com.checking_manager.checking_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class stuff_detail extends AppCompatActivity {

    private String stuff_name, pos, group_name;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private TextView stuffTV;
    private TextView posTV ;
    private TextView groupTV;
    private TextView contentsTV ;
    private TextView periodTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuff_detail);

        stuff_name = getIntent().getExtras().getString("stuff_name");
        pos = getIntent().getExtras().getString("pos_name");
        group_name = getIntent().getExtras().getString("group_name");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Groups").child(group_name);

        stuffTV = (TextView)findViewById(R.id.stuff_detail_group);
        posTV = (TextView)findViewById(R.id.stuff_detail_position);
        groupTV = (TextView)findViewById(R.id.stuff_detail_group);
        contentsTV = (TextView)findViewById(R.id.stuff_detail_contents);
        periodTV = (TextView)findViewById(R.id.stuff_detail_period);

        stuffTV.setText(stuff_name);
        posTV.setText(pos);
        groupTV.setText(group_name);


        reference.child("stuff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /*for(DataSnapshot ds : snapshot.getChildren()){
                    String stuffname = ds.getKey();

                    Log.e("check", String.valueOf(ds));
                }*/
                Log.e("test", "되나");
                String result1 = snapshot.child(stuff_name).child("position").child(pos).child("contents").getValue().toString();
                contentsTV.setText(result1);
                String result2 = snapshot.child(stuff_name).child("position").child(pos).child("period").getValue().toString();
                periodTV.setText(result2);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}