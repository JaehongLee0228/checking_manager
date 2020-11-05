package com.checking_manager.checking_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.checking_manager.checking_manager.login.log_in;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Before_enter extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private FirebaseDatabase databse;
    private DatabaseReference reference;
    private BackPressCloseHandler backPressCloseHandler;
    private Button logout, search_group;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private myGroupRvAdapter adapter;
    private ProgressDialog dialog;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton make_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.before_enter);

        SharedPreferences LogInAuto=getSharedPreferences("AutoLogIn_SAVE",MODE_PRIVATE);
        final SharedPreferences.Editor Auto_editor = LogInAuto.edit();
        int logInAuto=LogInAuto.getInt("logInAuto",0);

        String users_ID = LogInAuto.getString("ID",null);
        users_ID = stringReplace(users_ID);

        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.before_enter_swipeLayout);
        logout = (Button)findViewById(R.id.logout_Button);
        make_group = (FloatingActionButton) findViewById(R.id.group_make_button);
        search_group = (Button)findViewById(R.id.group_search_button);
        recyclerView = (RecyclerView)findViewById(R.id.my_group_recyclerView);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new myGroupRvAdapter(this);
        recyclerView.setAdapter(adapter);

        backPressCloseHandler = new BackPressCloseHandler(this);
        refreshLayout.setOnRefreshListener(this);

        databse = FirebaseDatabase.getInstance();
        reference = databse.getReference("Members").child(users_ID);

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("데이터 불러오는 중");
        dialog.show();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String group_name;
                String group_status;
                for(DataSnapshot ds : snapshot.getChildren()) {
                    group_name = ds.child("group_name").getValue().toString();
                    group_status = ds.child("group_status").getValue().toString();

                    adapter.addItem(group_name, group_status);
                    adapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Before_enter.this, log_in.class);
                Auto_editor.putInt("logInAuto", 0);
                Auto_editor.putString("ID", null);
                Auto_editor.putString("PW", null);
                Auto_editor.commit();
                finish();
                startActivity(intent);
            }
        });

        make_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Before_enter.this, group_making.class));
                finish();
            }
        });

        search_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Before_enter.this, group_searching.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    public static String stringReplace(String str){
        String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z]";
        str =str.replaceAll(match, "");
        return str;
    }

    @Override
    public void onRefresh() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("데이터 불러오는 중");
        dialog.show();

        adapter.clear();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String group_name;
                String group_status;
                for(DataSnapshot ds : snapshot.getChildren()) {
                    group_name = ds.child("group_name").getValue().toString();
                    group_status = ds.child("group_status").getValue().toString();

                    adapter.addItem(group_name, group_status);
                    adapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refreshLayout.setRefreshing(false);
    }
}