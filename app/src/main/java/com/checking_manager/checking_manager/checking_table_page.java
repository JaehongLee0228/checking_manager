package com.checking_manager.checking_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.checking_manager.checking_manager.checked_list.checked_table;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class checking_table_page extends AppCompatActivity {

    private Button qrBtn;
    private String stuff_name;
    private String pos;
    private String group_name;

    private Intent intent;

    private int index;
    private int pagelength = 2;

    private ListView listView;
    private ArrayList arrayList = new ArrayList();
    private ArrayAdapter arrayAdapter;
    private FirebaseDatabase databaseReference;
    private DatabaseReference reference, date_reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checking_table_page);

        intent = getIntent();
        group_name = intent.getExtras().getString("group_name");
        pos = intent.getExtras().getString("position_string");
        stuff_name = intent.getExtras().getString("stuff_name");

        qrBtn = (Button)findViewById(R.id.newQRButton);
        //Toast.makeText(checking_table_page.this, group_name + " " + stuff_name + " " + pos, Toast.LENGTH_SHORT).show();

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
        reference = databaseReference.getReference().child("Groups").child(group_name).child("stuff").child(stuff_name).child("position").child(pos);

        listView=(ListView)findViewById(R.id.checkedListView);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.checkedlistview_adapterlayout, arrayList);

        listView.setAdapter(arrayAdapter);


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Object checked_index_object = snapshot.child("checked_index").getValue();
                if(checked_index_object == null) {
                    arrayList.add("점검기록이 없습니다.");
                    arrayAdapter.notifyDataSetChanged();
                    return;
                }
                index =  Integer.parseInt(checked_index_object.toString());
                pagelength = index;

                for(int i = 1;i <= pagelength; ++i){
                    if(index >= 0) {
                        arrayList.add(snapshot.child("checked").child(Integer.toString(index)).getValue().toString());
                    }
                    --index;
                    if(index < 0){
                        break;
                    }
                }
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean lastItemVisibleFlag =false;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
                    //TODO 화면이 바닥에 닿을때 처리
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(int i = 1;i <= pagelength; ++i){
                                if(index >= 0) {
                                    arrayList.add(snapshot.child("checked").child(Integer.toString(index)).getValue().toString());
                                }
                                --index;
                                if(index < 0){
                                    break;
                                }
                            }
                            arrayAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemVisibleFlag = (totalItemCount >0) && (firstVisibleItem + visibleItemCount) >= totalItemCount;
            }
        });

        listView.setOnItemClickListener(date_click_listener);
    }

    ListView.OnItemClickListener date_click_listener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final String date = arrayList.get(position).toString();
            date_reference = databaseReference.getReference("Checked_Table").child(group_name).child(stuff_name).child(pos);

            date_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String index = "";
                    boolean judge = false;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        index = ds.child("date").getValue().toString();
                        if (index.equals(date)) {
                            index = ds.getKey();
                            judge = true;
                            break;
                        }
                    }

                    if (judge) {
                        Intent m_intent = new Intent(checking_table_page.this, checked_table.class);
                        m_intent.putExtra("index", index);
                        m_intent.putExtra("group_name", group_name);
                        m_intent.putExtra("pos", pos);
                        m_intent.putExtra("stuff_name", stuff_name);
                        startActivity(m_intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    };


}