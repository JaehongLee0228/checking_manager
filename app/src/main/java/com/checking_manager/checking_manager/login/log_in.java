package com.checking_manager.checking_manager.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.checking_manager.checking_manager.BackPressCloseHandler;
import com.checking_manager.checking_manager.My_Groups.Before_enter;
import com.checking_manager.checking_manager.R;
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
    private TextView log_in_signUp;
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
        log_in_signUp = (TextView) findViewById(R.id.log_in_signUp_button);
        log_in_signIn = (Button)findViewById(R.id.log_in_signIn_button);
        log_in_autoLogIn = (CheckBox)findViewById(R.id.log_in_autoLogIn_checkBox);
        log_in_autoID_fill = (CheckBox)findViewById(R.id.log_in_autoID_checkBox);

        firebaseAuth = FirebaseAuth.getInstance();
        backPressCloseHandler = new BackPressCloseHandler(this);
        log_in_signUp.setPaintFlags(log_in_signUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);



        if(logInAuto > 0)
            auto_logIn_function(IdAuto, PWAuto);

        if(logIn_ID_Auto > 0) {
            String ID = LogInAuto.getString("ID",null);
            log_in_ID.setText(ID);
            log_in_autoID_fill.setChecked(true);
        }

        log_in_autoLogIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    autologin = 0;
                }
                else {
                    autologin = 1;
                }
            }
        });

        log_in_autoID_fill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    autoIDfill = 0;
                } else {
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
                startActivity(new Intent(log_in.this, sign_up_activity.class));
            }
        });


    }

    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(log_in.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                                startActivity(new Intent(log_in.this, Before_enter.class));
                                Toast.makeText(log_in.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else Toast.makeText(log_in.this,"이메일 인증을 진행해주세요.",Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(log_in.this,"로그인 오류",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void auto_logIn_function(final String ID, final String PW) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("로그인 중");
        dialog.show();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(log_in.this, Before_enter.class);
                loginUser(ID, PW);
                finish();
                startActivity(intent);
                dialog.dismiss();
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

}