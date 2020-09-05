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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        signUp_ID = (EditText)findViewById(R.id.signUp_ID_editText);
        signUp_PW = (EditText)findViewById(R.id.signUp_PW_editText);
        signUp_PW_check = (EditText)findViewById(R.id.signUp_PW_check_editText);
        signUp_complete = (Button)findViewById(R.id.signUp_completeButton);

        firebaseAuth = FirebaseAuth.getInstance();

        signUp_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_input = signUp_ID.getText().toString();
                if(!isValidEmail(email_input)) {
                    Toast.makeText(sign_up_activity.this, "유효한 이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(signUp_PW.getText().length() < 8)
                    Toast.makeText(sign_up_activity.this,"비밀번호는 8글자 이상이어야 합니다.",Toast.LENGTH_SHORT).show();
                else if(signUp_PW.getText().toString().equals(signUp_PW_check.getText().toString())) {
                    final String email = signUp_ID.getText().toString().trim();
                    String password = signUp_PW.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(sign_up_activity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(sign_up_activity.this,"회원가입 완료",Toast.LENGTH_SHORT).show();
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

    public static String StringReplace(String str){
        String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z]";
        str =str.replaceAll(match, "");
        return str;
    }

    public static boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches())
            err = true;
        return err;
    }
}
