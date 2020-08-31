package com.checking_manager.checking_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class log_in extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private BackPressCloseHandler backPressCloseHandler;

    private EditText log_in_ID;
    private EditText log_in_PW;
    private Button log_in_signIn;
    private Button log_in_signUp;
    private CheckBox log_in_autoLogIn;
    private CheckBox log_in_autoID_fill;

    int autologin = 1;
    int autoIDfill = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        final SharedPreferences LogInAuto = getSharedPreferences("AutoLogIn_SAVE",MODE_PRIVATE);
        final SharedPreferences.Editor Auto_editor = LogInAuto.edit();
        int logInAuto = LogInAuto.getInt("logInAuto",0);
        int logIn_ID_Auto = LogInAuto.getInt("logIn_ID_Auto",0);
        String IdAuto = LogInAuto.getString("ID",null);
        String PWAuto = LogInAuto.getString("PW",null);

        log_in_ID = (EditText)findViewById(R.id.log_in_ID_editText);
        log_in_PW = (EditText)findViewById(R.id.log_in_PW_editText);
        log_in_signUp = (Button)findViewById(R.id.log_in_signUp_button);
        log_in_signIn = (Button)findViewById(R.id.log_in_signIn_button);
        log_in_autoLogIn = (CheckBox)findViewById(R.id.log_in_autoLogIn_checkBox);
        log_in_autoID_fill = (CheckBox)findViewById(R.id.log_in_autoID_checkBox);

        firebaseAuth = FirebaseAuth.getInstance();
        backPressCloseHandler = new BackPressCloseHandler(this);

        if(logInAuto > 0) {
            Intent intent = new Intent(log_in.this, Main_sum.class);
            loginUser(IdAuto,PWAuto);
            finish();
            startActivity(intent);
        }

        if(logIn_ID_Auto > 0) {
            String ID = LogInAuto.getString("ID",null);
            log_in_ID.setText(ID);
            log_in_autoID_fill.setChecked(true);
        }

        log_in_autoLogIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    log_in_autoLogIn.setText("자동 로그인 사용 O");
                    autologin = 0;
                }
                else {
                    log_in_autoLogIn.setText("자동로그인 사용 X");
                    autologin = 1;
                }
            }
        });

        log_in_autoID_fill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    log_in_autoID_fill.setText("ID 저장 사용 O");
                    autoIDfill = 0;
                } else {
                    log_in_autoID_fill.setText("ID 저장 사용 X");
                    autoIDfill = 1;
                }
            }
        });

        log_in_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = log_in_ID.getText().toString().trim();
                String password = log_in_PW.getText().toString().trim();

                if (email.equals(null) || password.equals(null) || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(log_in.this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (autologin == 0) {
                    Auto_editor.putInt("logInAuto", 1);
                    Auto_editor.commit();
                } else if (autologin == 1) {
                    Auto_editor.putInt("logInAuto", 0);
                    Auto_editor.commit();
                }

                if(autoIDfill == 0) {
                    Auto_editor.putInt("logIn_ID_Auto", 1);
                    Auto_editor.commit();
                } else if(autoIDfill == 1) {
                    Auto_editor.putInt("logIn_ID_Auto", 0);
                    Auto_editor.commit();
                }

                Auto_editor.putString("ID", email);
                Auto_editor.putString("PW", password);
                Auto_editor.commit();

                loginUser(email, password);
            }
        });

        log_in_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(log_in.this,sign_up_activity.class));
            }
        });


    }

    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(log_in.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            startActivity(new Intent(log_in.this, Before_enter.class));
                            Toast.makeText(log_in.this,"로그인 성공",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else Toast.makeText(log_in.this,"로그인 오류",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

}