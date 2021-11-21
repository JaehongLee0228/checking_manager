package com.example.ljh_final_exam;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllLines extends AppCompatActivity {

    ListView listView;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_lines);

        Toolbar toolbar=(Toolbar)findViewById(R.id.linebar);

        final SharedPreferences LogInAuto=getSharedPreferences("AutoLogIn_SAVE",MODE_PRIVATE);
        final SharedPreferences.Editor Auto_editor = LogInAuto.edit();
        final String IdAuto=LogInAuto.getString("ID",null);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference();

        //sentence=new String();
        listView=(ListView)findViewById(R.id.listview);

        list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,R.layout.user_sentences,R.id.user_sentence,list);
        listView.setAdapter(adapter);

        setSupportActionBar(toolbar);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String sentence;
                for(DataSnapshot ds:dataSnapshot.child("AllLines").getChildren()) {
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
        //아이템 1번 클릭 시 다이어리 액티비티로 이동해서 내용 불러와
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String data = list.get(position); // 2019-06-01 02:22
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sentence,bridge;
                        for(DataSnapshot ds:dataSnapshot.child("AllLines").getChildren()) {
                            bridge=ds.getKey();
                            sentence=ds.child("title").getValue().toString();
                            if(sentence.equals(data)) { //data==title
                                Intent intent=new Intent(AllLines.this,Diary_View.class);
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
                AlertDialog.Builder dlg=new AlertDialog.Builder(AllLines.this);
                dlg.setMessage("Do you want to edit or delete your sentence?");
                dlg.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AllLines.this,"Edit",Toast.LENGTH_SHORT).show();
                        final String data = list.get(position); // 2019-06-01 02:22
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String sentence,bridge;
                                for(DataSnapshot ds:dataSnapshot.child("AllLines").getChildren()) {
                                    bridge=ds.getKey();
                                    sentence=ds.child("title").getValue().toString();
                                    if(sentence.equals(data)) { //data==title
                                        if(IdAuto.equals(ds.child("ID").getValue().toString())) {
                                            Intent intent=new Intent(AllLines.this,Editing_Sentences.class);
                                            DiaryDTO dto=new DiaryDTO();
                                            dto.setSentence(bridge);
                                            intent.putExtra("dto",dto);
                                            startActivity(intent);
                                            finish();
                                            break;
                                        }
                                        else {
                                            Toast.makeText(AllLines.this,"This is not your sentence",Toast.LENGTH_LONG).show();
                                            break;
                                        }
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
                        database.getReference().child("AllLines").child(removedata).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent=new Intent(AllLines.this,MainActivity.class);
                                Toast.makeText(AllLines.this,"Delete Completed",Toast.LENGTH_SHORT).show();
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String sentence,bridge;
                                        for(DataSnapshot ds:dataSnapshot.child("AllLines").getChildren()) {
                                            bridge=ds.getKey();
                                            sentence=ds.child("title").getValue().toString();
                                            if(sentence.equals(removedata)) { //data==title
                                                if(IdAuto.equals(ds.child("ID").getValue().toString())) {
                                                    database.getReference().child("AllLines").child(bridge).removeValue();
                                                    break;
                                                }
                                                else {
                                                    Toast.makeText(AllLines.this,"This is not your sentence",Toast.LENGTH_LONG).show();
                                                    break;
                                                }
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
                                Toast.makeText(AllLines.this,"Delete Failed",Toast.LENGTH_SHORT).show();
                            }
                        });
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
        inflater.inflate(R.menu.line_menu,menu);
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
                startActivity(new Intent(getApplicationContext(),MyLines.class));
                finish();
                break;
            case R.id.lines_shared :
                Toast.makeText(getApplicationContext(),"All Lines",Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(),AllLines.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
