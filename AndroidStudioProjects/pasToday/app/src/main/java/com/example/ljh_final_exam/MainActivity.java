package com.example.ljh_final_exam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton PostingButton;
        Toolbar toolbar=(Toolbar)findViewById(R.id.appbar);
        PostingButton=(FloatingActionButton)findViewById(R.id.floatingActionButton);
        setSupportActionBar(toolbar);


        PostingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Writing.class);
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
        SharedPreferences LogInAuto=getSharedPreferences("AutoLogIn_SAVE",MODE_PRIVATE);
        SharedPreferences.Editor Auto_editor = LogInAuto.edit();
        int logInAuto=LogInAuto.getInt("logInAuto",0);

        switch (item.getItemId()) {
            case R.id.my_line :
                Toast.makeText(getApplicationContext(),"My Lines",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MyLines.class));
                break;
            case R.id.lines_shared :
                Toast.makeText(getApplicationContext(),"All Lines",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),AllLines.class));
                break;
            case R.id.Log_Out :
                Intent intent=new Intent(this,Log_In.class);
                Auto_editor.putInt("logInAuto", 0);
                Auto_editor.putString("ID", null);
                Auto_editor.putString("PW", null);
                Auto_editor.commit();
                finish();
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
