package com.example.ljh_final_exam;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyLines extends AppCompatActivity {

    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_lines);
        Toolbar toolbar=(Toolbar)findViewById(R.id.linebar);
        setSupportActionBar(toolbar);
        listView=(ListView)findViewById(R.id.listview);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference();

        list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,R.layout.user_sentences,R.id.user_sentence,list);
        listView.setAdapter(adapter);


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String sentence;
                for(DataSnapshot ds:dataSnapshot.child("MyLines").getChildren()) {
                    sentence=ds.child("title").getValue().toString();
                    list.add(sentence);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //한번 누를시 글 불러오기
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String data = list.get(position); // 2019-06-01 02:22
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sentence,bridge;
                        for(DataSnapshot ds:dataSnapshot.child("MyLines").getChildren()) {
                            bridge=ds.getKey();
                            sentence=ds.child("title").getValue().toString();
                            if(sentence.equals(data)) { //data==title
                                Intent intent=new Intent(MyLines.this,MyLines_viewer.class);
                                DiaryDTO dto=new DiaryDTO();
                                dto.setSentence(bridge);
                                intent.putExtra("dto",dto);
                                startActivity(intent);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        //아이템 꾹 누를 시 대화상자 생성해서 삭제 or 수정 여부 물어봐
        //수정 > 액티비티 이동해서 자료 그대로 불러와
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dlg=new AlertDialog.Builder(MyLines.this);
                dlg.setMessage("Do you want to edit or delete your sentence?");
                dlg.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MyLines.this,"Edit",Toast.LENGTH_SHORT).show();
                        final String data = list.get(position); // 2019-06-01 02:22
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String sentence,bridge;
                                for(DataSnapshot ds:dataSnapshot.child("MyLines").getChildren()) {
                                    bridge=ds.getKey();
                                    sentence=ds.child("title").getValue().toString();
                                    if(sentence.equals(data)) { //data==title
                                        Intent intent=new Intent(MyLines.this,MyLines_Editer.class);
                                        DiaryDTO dto=new DiaryDTO();
                                        dto.setSentence(bridge);
                                        intent.putExtra("dto",dto);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                dlg.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String removedata = list.get(position); // 2019-06-01 02:22
                        database.getReference().child("MyLines").child(removedata).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent=new Intent(MyLines.this,MainActivity.class);
                                Toast.makeText(MyLines.this,"Delete Completed",Toast.LENGTH_SHORT).show();
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String sentence,bridge;
                                        for(DataSnapshot ds:dataSnapshot.child("MyLines").getChildren()) {
                                            bridge=ds.getKey();
                                            sentence=ds.child("title").getValue().toString();
                                            if(sentence.equals(removedata)) { //data==title
                                                database.getReference().child("MyLines").child(bridge).removeValue();
                                                break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                finish();
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MyLines.this,"Delete Failed",Toast.LENGTH_SHORT).show();
                            }
                        });
                        adapter = null;
                        adapter = new ArrayAdapter<String>(MyLines.this,R.layout.user_sentences,R.id.user_sentence,list);
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                    }
                });
                dlg.show();
                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.all_lines_and_my_lines_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.writing :
                finish();
                startActivity(new Intent(getApplicationContext(),Writing.class));
                break;
            case R.id.my_line :
                Toast.makeText(getApplicationContext(),"My Lines",Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(),MyLines.class));
                break;
            case R.id.lines_shared :
                Toast.makeText(getApplicationContext(),"All Lines",Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(),AllLines.class));
                break;
            case R.id.clearing :
                database.getReference().child("MyLines").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MyLines.this,"Delete Completed",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MyLines.this,"Delete Failed",Toast.LENGTH_SHORT).show();
                    }
                });
                Intent intent=new Intent(MyLines.this,MyLines.class);
                adapter.notifyDataSetChanged();
                finish();
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context,"groupDB",null,1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE groupTBL (gName CHAR(20) PRIMARY KEY, gNumber INTEGER);");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS groupTBL");
            onCreate(db);
        }
    }
}
