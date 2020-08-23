package com.checking_manager.checking_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class sign_up_activity extends AppCompatActivity {

    private EditText signUp_ID;
    private EditText signUp_PW;
    private EditText signUp_PW_check;
    private Button signUp_complete;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        final SharedPreferences keySeq = getSharedPreferences("keySeq_Save", MODE_PRIVATE);
        final SharedPreferences.Editor editor = keySeq.edit();
        final int key = keySeq.getInt("key", 0);


        signUp_ID = (EditText)findViewById(R.id.signUp_ID_editText);
        signUp_PW = (EditText)findViewById(R.id.signUp_PW_editText);
        signUp_PW_check = (EditText)findViewById(R.id.signUp_PW_check_editText);
        signUp_complete = (Button)findViewById(R.id.signUp_completeButton);

        firebaseAuth = FirebaseAuth.getInstance();

        signUp_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(signUp_PW.getText().length() < 8)
                    Toast.makeText(sign_up_activity.this,"비밀번호는 8글자 이상이어야 합니다.",Toast.LENGTH_SHORT).show();
                else if(signUp_PW.getText().toString().equals(signUp_PW_check.getText().toString())) {
                    String email = signUp_ID.getText().toString().trim();
                    String password = signUp_PW.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(sign_up_activity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                FirebaseDatabase.getInstance().getReference("Members").child(signUp_ID.getText().toString());
                                finish();
                            } else {
                                Toast.makeText(sign_up_activity.this,"이미 가입된 이메일입니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                }
                else Toast.makeText(sign_up_activity.this,"비밀번호를 다시 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
