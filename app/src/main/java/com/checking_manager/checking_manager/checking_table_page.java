package com.checking_manager.checking_manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class checking_table_page extends AppCompatActivity {

    private Button qrBtn;
    private String stuff_name;
    private String pos;
    private String group_name;


    ListView listView;
    List arrayList = new ArrayList();
    ArrayAdapter arrayAdapter;
    FirebaseDatabase databaseReference;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checking_table_page);


        group_name = getIntent().getExtras().getString("group_name");
        pos = getIntent().getExtras().getString("position_string");
        stuff_name = getIntent().getExtras().getString("stuff_name");

        qrBtn = (Button)findViewById(R.id.newQRButton);
        Toast.makeText(checking_table_page.this, group_name + " " + stuff_name + " " + pos, Toast.LENGTH_SHORT).show();

        qrBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(checking_table_page.this, reQRPage.class);
                intent.putExtra("stuff_name", stuff_name);
                intent.putExtra("pos", pos);
                intent.putExtra("group_name", group_name);
                startActivity(intent);
            }
        });


        databaseReference = FirebaseDatabase.getInstance();
        reference = databaseReference.getReference().child("Groups").child(group_name).child("stuff").child(stuff_name).child("position").child(pos).child("checked");

        listView=(ListView)findViewById(R.id.checkedListView);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.checkedlistview_adapterlayout, arrayList);

        listView.setAdapter(arrayAdapter);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(int i = (int) snapshot.getChildrenCount() - 1; i >= 0; --i){
                    arrayList.add(snapshot.child(Integer.toString(i)).getValue().toString());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}