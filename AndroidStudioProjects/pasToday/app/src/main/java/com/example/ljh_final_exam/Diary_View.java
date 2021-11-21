package com.example.ljh_final_exam;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Diary_View extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;

    TextView textView, textView2, timebox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary__view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.linebar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView8);
        timebox=(TextView)findViewById(R.id.timeBox);

        Intent intent = getIntent();
        DiaryDTO dto = (DiaryDTO) intent.getSerializableExtra("dto");
        final String DataKey = dto.getSentence();

        reference.child("AllLines").child(DataKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textView2.setText(dataSnapshot.child("title").getValue().toString());
                textView.setText("\n"+dataSnapshot.child("sentence").getValue().toString());
                timebox.setText(dataSnapshot.child("time").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.line_viewer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.writing:
                finish();
                startActivity(new Intent(getApplicationContext(), Writing.class));
                break;
            case R.id.my_line:
                Toast.makeText(getApplicationContext(), "My Lines", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MyLines.class));
                finish();
                break;
            case R.id.lines_shared:
                Toast.makeText(getApplicationContext(), "All Lines", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), AllLines.class));
                finish();
                break;
            case R.id.Editing:
                Toast.makeText(Diary_View.this, "Editing", Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                DiaryDTO dto = (DiaryDTO) intent.getSerializableExtra("dto");
                String keyValue = dto.getSentence();
                intent = new Intent(Diary_View.this, Editing_Sentences.class);
                dto.setSentence(keyValue);
                intent.putExtra("dto", dto);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
