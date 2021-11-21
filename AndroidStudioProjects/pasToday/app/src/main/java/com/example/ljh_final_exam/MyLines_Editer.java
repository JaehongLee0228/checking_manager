package com.example.ljh_final_exam;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyLines_Editer extends AppCompatActivity {

    Button button;
    EditText editText, titlebox;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_lines__editer);
        button=(Button)findViewById(R.id.CompleteButton);
        editText=(EditText)findViewById(R.id.editText);
        titlebox=(EditText)findViewById(R.id.editText2);
        Toolbar toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        database= FirebaseDatabase.getInstance();
        reference=database.getReference();

        Intent intent = getIntent();
        DiaryDTO dto=(DiaryDTO)intent.getSerializableExtra("dto");
        final String title=dto.getSentence();

        reference.child("MyLines").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String data = snapshot.getKey();
                    if(data.equals(title)) {
                        titlebox.setText(snapshot.child("title").getValue().toString());
                        editText.setText(snapshot.child("sentence").getValue().toString());
                        break;
                    }
                }
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
                FirebaseDatabase.getInstance().getReference().child("MyLines").child(title).child("title").setValue(titleEdited);
                FirebaseDatabase.getInstance().getReference().child("MyLines").child(title).child("sentence").setValue(sentence);
                Toast.makeText(MyLines_Editer.this,"Editing Completed",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MyLines_Editer.this,MyLines.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
