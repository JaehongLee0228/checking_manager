package com.checking_manager.checking_manager.checked_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;

import com.checking_manager.checking_manager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class checked_table extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private String pos, group_name, stuff_name, index;
    private Intent intent;


    private contentsRvAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private SwipeRefreshLayout refreshLayout;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checked_table);

        intent = getIntent();
        pos = intent.getExtras().getString("pos");
        group_name = intent.getExtras().getString("group_name");
        stuff_name = intent.getExtras().getString("stuff_name");
        index = intent.getExtras().getString("index");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Checked_Table").child(group_name).child(stuff_name).child(pos).child(index);

        recyclerView = (RecyclerView)findViewById(R.id.contents_recyclerView);
        manager = new LinearLayoutManager(checked_table.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.contents_swipeLayout);
        refreshLayout.setOnRefreshListener(this);
        adapter = new contentsRvAdapter(this);
        recyclerView.setAdapter(adapter);

        initializeData();
    }

    public void initializeData() {
        reference.child("contents").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String content, okay, special;

                for(DataSnapshot ds : snapshot.getChildren()) {
                    content = ds.getKey();
                    okay = ds.child("okay").getValue().toString();
                    special = ds.child("special").getValue().toString();

                    adapter.addItem(content, okay, special);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        initializeData();
        refreshLayout.setRefreshing(false);
    }
}