package com.checking_manager.checking_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Before_enter extends AppCompatActivity {

    private FirebaseDatabase databse;
    private DatabaseReference reference;
    private BackPressCloseHandler backPressCloseHandler;
    private Button logout, make_group, search_group;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private myGroupRvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.before_enter);

        SharedPreferences LogInAuto=getSharedPreferences("AutoLogIn_SAVE",MODE_PRIVATE);
        final SharedPreferences.Editor Auto_editor = LogInAuto.edit();
        int logInAuto=LogInAuto.getInt("logInAuto",0);

        String users_ID = LogInAuto.getString("ID",null);
        users_ID = stringReplace(users_ID);

        logout = (Button)findViewById(R.id.logout_Button);
        make_group = (Button)findViewById(R.id.group_make_button);
        search_group = (Button)findViewById(R.id.group_search_button);

        recyclerView = (RecyclerView)findViewById(R.id.my_group_recyclerView);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new myGroupRvAdapter(this);
        recyclerView.setAdapter(adapter);

        backPressCloseHandler = new BackPressCloseHandler(this);

        databse = FirebaseDatabase.getInstance();
        reference = databse.getReference("Members").child(users_ID);

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                usersGroupsList item = (usersGroupsList)parent.getItemAtPosition(position);

                String group_name = item.getGroupName();
                String group_status = item.getGroupStatus();

                Intent intent = null;
                intent = new Intent(Before_enter.this, Main_sum.class);
                Log.d("fragment_group_name_input", group_name);
                intent.putExtra("group_name", group_name);
                startActivity(intent);
            }
        });*/

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

}