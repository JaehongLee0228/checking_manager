package com.checking_manager.checking_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class group_searching extends AppCompatActivity {

    private Button search_button, join_request;
    private EditText search_editText;
    private TextView search_result;

    private FirebaseDatabase databse;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_searching);

        search_button = (Button)findViewById(R.id.group_name_search_button);
        search_editText = (EditText)findViewById(R.id.group_name_search_EditText);
        search_result = (TextView)findViewById(R.id.group_search_result_TextView);
        join_request = (Button)findViewById(R.id.searching_group_join_button);

        databse = FirebaseDatabase.getInstance();
        reference = databse.getReference("Groups");

        join_request.setVisibility(View.GONE);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String search_group_name = search_editText.getText().toString();

                if(search_editText.getText() == null) {
                    Log.d("searching_group_name_input", "null");
                    Toast.makeText(group_searching.this,"그룹 이름을 입력해주세요.",  Toast.LENGTH_SHORT).cancel();
                    return;
                }
                if(!keyValue_check(search_group_name)) {
                    Toast.makeText(group_searching.this, "그룹 이름에 '.', '#', '$', '[', or ']' 값을 사용하실 수 없습니다.", Toast.LENGTH_SHORT).cancel();
                    return;
                }
                Log.d("searching_group_name_input", search_group_name);

                reference.child(search_group_name).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Object result = snapshot.child("group_name").getValue();
                        if(result == null) {
                            join_request.setVisibility(View.GONE);
                            Log.d("searching_group_result", "null");
                            search_result.setText("해당하는 그룹은 존재하지 않습니다.");
                        } else if(result.toString().equals(search_group_name)) {
                            Log.d("searching_group_result", "success");
                            join_request.setVisibility(View.VISIBLE);
                            search_result.setText("해당하는 결과를 찾았습니다.\n" + result.toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        join_request.setOnClickListener(join_onClickListener);
    }

    Button.OnClickListener join_onClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public boolean keyValue_check(String group_name) {
        for(int i = 0; i < group_name.length(); i++) {
            char temp = group_name.charAt(i);
            if (temp == '.' || temp == '#' || temp == '$' || temp == '[' || temp == ']')
                return false;
        }
        return true;
    }

}