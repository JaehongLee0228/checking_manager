package com.checking_manager.checking_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class stuff_position_page extends AppCompatActivity implements OnItemClick{

    private Intent intent;
    private String group_name = "", group_status = "", kind_of_stuff = "";
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private positionRvAdapter adapter;
    private LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stuff_position_page);

        intent = getIntent();
        group_name = intent.getExtras().getString("group_name");
        group_status = intent.getExtras().getString("group_status");
        kind_of_stuff = intent.getExtras().getString("kind_of_stuff");

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView)findViewById(R.id.position_recyclerView);
        recyclerView.setLayoutManager(manager);
        adapter = new positionRvAdapter(stuff_position_page.this, this);
        recyclerView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Groups").child(group_name).child("stuff");

        reference.child(kind_of_stuff).child("position").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String position = ds.getKey();
                    Object checked_index_object = ds.child("checked_index").getValue();

                    String last_checked_string = "";
                    if(checked_index_object != null)
                        last_checked_string = ds.child("checked").child(checked_index_object.toString()).getValue().toString();
                    else last_checked_string = "점검된 기록 없음";
                    adapter.addItem(position, last_checked_string);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(String value, String position_string) {
        Intent intent;
        if(value.equals("to_checkList_page")) {
            intent = new Intent(stuff_position_page.this, checking_table_page.class);
            intent.putExtra("group_name", group_name);
            intent.putExtra("group_status", group_status);
            intent.putExtra("position_string", position_string);
            startActivity(intent);
        }
    }
}