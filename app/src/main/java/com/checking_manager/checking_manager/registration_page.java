package com.checking_manager.checking_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class registration_page extends AppCompatActivity implements OnItemClick {

    private Button add_button, complete_button;
    private EditText position_editText, kind_editText, content_editText;
    private ListView content_listView;
    private registrationContentAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private String group_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

        group_name = getIntent().getExtras().getString("group_name");

        add_button = (Button)findViewById(R.id.registe_add_button);
        position_editText = (EditText)findViewById(R.id.registe_stuff_position);
        kind_editText = (EditText)findViewById(R.id.registe_kind_of_stuff);
        content_editText = (EditText)findViewById(R.id.registe_check_content_editText);
        complete_button = (Button)findViewById(R.id.registe_complete_button);

        adapter = new registrationContentAdapter(this);
        content_listView = (ListView)findViewById(R.id.registe_listView);
        content_listView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Groups").child(group_name);

        add_button.setOnClickListener(add_onClickListener);
        complete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String stuff = kind_editText.getText().toString();
                final String position = position_editText.getText().toString();
                if(stuff.equals("")) {
                    Toast.makeText(registration_page.this, "물품 종류를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(position.equals("")) {
                    Toast.makeText(registration_page.this, "물품 위치를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(adapter.getCount() == 0) {
                    Toast.makeText(registration_page.this, "점검해야할 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                reference.child("stuff").child(stuff).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final ProgressDialog dialog = new ProgressDialog(registration_page.this);
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.setMessage("작업 중");
                        dialog.show();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String existed_position = ds.getKey();
                            if(position.equals(existed_position)) {
                                Toast.makeText(registration_page.this, "이미 등록하신 물품의 위치입니다.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                return;
                            }
                        }

                        for(int i = 0; i < adapter.getCount(); i++)
                            reference.child("stuff").child(stuff).child(position).child(i + "").setValue(adapter.getItem(i).getCheck_content());

                        Handler mHandler = new Handler();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                dialog.dismiss();
                                startActivity(new Intent(registration_page.this, Main_sum.class));
                            }
                        }, 1000);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    Button.OnClickListener add_onClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            String content = content_editText.getText().toString();

            adapter.addItem(content);
            adapter.notifyDataSetChanged();
            content_editText.setText("");
        }
    };

    @Override
    public void onClick(String value, String position) {
        if(value.equals("delete"))
            delete_content(Integer.parseInt(position));
    }

    public void delete_content(int position) {
        adapter.deleteItem(position);
        adapter.notifyDataSetChanged();
    }

    public void showProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("작업 중");
        dialog.show();
    }
}