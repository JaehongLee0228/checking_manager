package com.example.ljh_final_exam;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyLines_viewer extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference reference;

    TextView textView, titlebox, timebox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_lines_viewer);
        Toolbar toolbar=(Toolbar)findViewById(R.id.linebar);
        setSupportActionBar(toolbar);

        textView=(TextView)findViewById(R.id.textView);
        titlebox=(TextView)findViewById(R.id.textView8);
        timebox=(TextView)findViewById(R.id.timeBox);

        database= FirebaseDatabase.getInstance();
        reference=database.getReference();

        Intent intent=getIntent();
        DiaryDTO dto=(DiaryDTO)intent.getSerializableExtra("dto");
        final String time=dto.getSentence();

        reference.child("MyLines").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String data=snapshot.getKey();
                    if(data.equals(time)) {
                        titlebox.setText(snapshot.child("title").getValue().toString());
                        timebox.setText(time);
                        textView.setText("\n"+snapshot.child("sentence").getValue().toString());
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.line_viewer_menu,menu);
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
                startActivity(new Intent(getApplicationContext(),AllLines.class));
                finish();
                break;
            case R.id.Editing :
                Toast.makeText(MyLines_viewer.this,"Editing",Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                DiaryDTO dto=(DiaryDTO)intent.getSerializableExtra("dto");
                String title=dto.getSentence();
                intent=new Intent(MyLines_viewer.this,MyLines_Editer.class);
                dto.setSentence(title);
                intent.putExtra("dto",dto);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
