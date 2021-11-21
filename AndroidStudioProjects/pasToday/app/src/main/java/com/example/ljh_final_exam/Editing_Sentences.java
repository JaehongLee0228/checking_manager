package com.example.ljh_final_exam;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Editing_Sentences extends AppCompatActivity {

    Button button;
    EditText editText,titlebox;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editing__sentences);
        editText=(EditText)findViewById(R.id.editText);
        button=(Button)findViewById(R.id.CompleteButton);
        titlebox=(EditText)findViewById(R.id.editText2);
        Toolbar toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        database= FirebaseDatabase.getInstance();
        reference=database.getReference();

        Intent intent = getIntent();
        DiaryDTO dto=(DiaryDTO)intent.getSerializableExtra("dto");
        final String key=dto.getSentence();

        reference.child("AllLines").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                editText.setText(dataSnapshot.child("sentence").getValue().toString());
                titlebox.setText(dataSnapshot.child("title").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sentence=editText.getText().toString();
                String titleEdited=titlebox.getText().toString();
                FirebaseDatabase.getInstance().getReference().child("AllLines").child(key).child("title").setValue(titleEdited);
                FirebaseDatabase.getInstance().getReference().child("AllLines").child(key).child("sentence").setValue(sentence);
                Toast.makeText(Editing_Sentences.this,"Editing Completed",Toast.LENGTH_SHORT).show();
                finish();
                Intent intent=new Intent(Editing_Sentences.this,AllLines.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
        }
        return super.onOptionsItemSelected(item);
    }
}
