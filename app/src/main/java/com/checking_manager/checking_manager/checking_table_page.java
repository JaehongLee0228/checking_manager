package com.checking_manager.checking_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class checking_table_page extends AppCompatActivity {

    private Button qrBtn;
    private String stuff_name;
    private String pos;
    private String group_name;

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

    }
}