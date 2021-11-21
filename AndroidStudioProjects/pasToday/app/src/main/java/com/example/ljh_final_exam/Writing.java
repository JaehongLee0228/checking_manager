package com.example.ljh_final_exam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Writing extends AppCompatActivity {

    CheckBox SharingCheck;
    Button CompleteButton;
    EditText sentence,TitleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writing);

        Toolbar toolbar=(Toolbar)findViewById(R.id.appbar);
        SharingCheck=(CheckBox)findViewById(R.id.SharingBox);
        CompleteButton=(Button)findViewById(R.id.CompleteButton);
        sentence=(EditText)findViewById(R.id.sentence);
        TitleText=(EditText)findViewById(R.id.titleText);

        final SharedPreferences keySeq=getSharedPreferences("keySeq_Save",MODE_PRIVATE);
        final SharedPreferences.Editor editor=keySeq.edit();
        final int key=keySeq.getInt("key",0);
        final SharedPreferences LogInAuto=getSharedPreferences("AutoLogIn_SAVE",MODE_PRIVATE);
        final SharedPreferences.Editor Auto_editor = LogInAuto.edit();
        final String IdAuto=LogInAuto.getString("ID",null);

        setSupportActionBar(toolbar);
        final long timeNow=System.currentTimeMillis();
        final Date timeData=new Date(timeNow);
        final SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd");
        final String timeForEdittext=time.format(timeData);
        TitleText.setText(timeForEdittext);
        SharingCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) SharingCheck.setText("Share");
                else SharingCheck.setText("Don't Share");
            }
        });
        CompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null;
                String SentenceWritten=sentence.getText().toString();
                String titleName=TitleText.getText().toString();
                if(SharingCheck.isChecked()) {
                    Log.d("Writing", "Button - onClickListener");
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String getTime = sdf.format(date);
                    FirebaseDatabase.getInstance().getReference("AllLines").child(String.valueOf(key)).child("title").setValue(titleName);
                    FirebaseDatabase.getInstance().getReference("AllLines").child(String.valueOf(key)).child("sentence").setValue(SentenceWritten);
                    FirebaseDatabase.getInstance().getReference("AllLines").child(String.valueOf(key)).child("time").setValue(getTime);
                    FirebaseDatabase.getInstance().getReference("AllLines").child(String.valueOf(key)).child("ID").setValue(IdAuto);
                    editor.putInt("key",key+1);
                    editor.commit();
                    intent=new Intent(Writing.this,AllLines.class); }
                else {
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String getTime = sdf.format(date);
                    intent=new Intent(Writing.this,MyLines.class);
                    FirebaseDatabase.getInstance().getReference("MyLines").child(getTime).child("sentence").setValue(SentenceWritten);
                    FirebaseDatabase.getInstance().getReference("MyLines").child(getTime).child("title").setValue(titleName);
                }

                startActivity(intent);
                finish();
                Toast.makeText(Writing.this, "Completed.", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(getApplicationContext(),MyLines.class));
                break;
            case R.id.lines_shared :
                Toast.makeText(getApplicationContext(),"All Lines",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),AllLines.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
