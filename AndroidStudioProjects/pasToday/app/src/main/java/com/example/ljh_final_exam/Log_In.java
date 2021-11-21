package com.example.ljh_final_exam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
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

import java.util.regex.Pattern;

public class Log_In extends AppCompatActivity {

    EditText IdEditText,PWEditText,PWCHKEditText;
    Button JOINButton, LogInButton;
    CheckBox autoLogIn;

    private String email="";
    private String password="";
    int autologin=1;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log__in);

        IdEditText=(EditText)findViewById(R.id.IdEditText);
        PWEditText=(EditText)findViewById(R.id.PWEditText);
        PWCHKEditText=(EditText)findViewById(R.id.PWCHKEditText);
        JOINButton=(Button)findViewById(R.id.JoinButton);
        LogInButton=(Button)findViewById(R.id.LogInButton);
        autoLogIn=(CheckBox)findViewById(R.id.checkBox);

        Toolbar toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        //자동로그인 에 대한 SharedPreferences
        final SharedPreferences LogInAuto=getSharedPreferences("AutoLogIn_SAVE",MODE_PRIVATE);
        final SharedPreferences.Editor Auto_editor = LogInAuto.edit();
        final int logInAuto=LogInAuto.getInt("logInAuto",0);
        //ID 에 대한 SharedPreferences
        final String IdAuto=LogInAuto.getString("ID",null);
        //PW 에 대한 SharedPreferences
        final String PWAuto=LogInAuto.getString("PW",null);

        firebaseAuth=FirebaseAuth.getInstance();

        if(logInAuto>0) {  //자동로그인 시켜야할 때
            Intent intent=new Intent(Log_In.this,MainActivity.class);
            loginUser(IdAuto,PWAuto);
            finish();
            startActivity(intent);
        }
        autoLogIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    autoLogIn.setText("Use Auto LogIn");
                    autologin=0;
                }
                else {
                    autoLogIn.setText("Don't use Auto LogIn");
                    autologin=1;
                }
            }
        });

        JOINButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Log_In.this,New_Member.class);
                startActivity(intent);
            }
        });

        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=IdEditText.getText().toString();
                password=PWEditText.getText().toString();
                if(isValidEmail()&&isValidPassword()) {
                    Intent intent = new Intent(Log_In.this, MainActivity.class);
                    if (autologin == 0) { //자동로그인 체크 O
                        //자동로그인값 저장
                        Auto_editor.putInt("logInAuto", 1);
                        Auto_editor.commit();
                    } else if (autologin == 1) { //자동로그인 체크 X
                        //자동로그인값 저장
                        Auto_editor.putInt("logInAuto", 0);
                        Auto_editor.commit();
                    }
                    Auto_editor.putString("ID", email);
                    Auto_editor.putString("PW", password);
                    Auto_editor.commit();
                    loginUser(email,password);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(Log_In.this,"Enter valid E-mail and Password",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void signIn(View view) {
        email=IdEditText.getText().toString();
        password=PWEditText.getText().toString();

        if(isValidEmail()&&isValidPassword()) {
            loginUser(email,password);
        }
    }
    public void signUp(View view) {
        email=IdEditText.getText().toString();
        password=PWEditText.getText().toString();

        if(isValidEmail()&&isValidPassword()) {
            createUser(email,password);
        }
    }


    //이메일 유효성 검사사
    private boolean isValidEmail() {
        if(email.isEmpty()) {
            return false; }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false; }
        else {
            return true; }
    }

    //비밀번호 유효성 검사
    private boolean isValidPassword() {
        if(password.isEmpty()) {
            return false; }
        else if(!PASSWORD_PATTERN.matcher(password).matches()) {
            return false; }
        else {
            return true; }
    }

    //회원가입
    private void createUser(String email,String password) {
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Log_In.this,R.string.SuccessfulJoining,Toast.LENGTH_SHORT).show(); }
                        else {
                            Toast.makeText(Log_In.this,R.string.UnsuccessfulJoining,Toast.LENGTH_SHORT).show(); }
                    }
                });
    }

    //로그인
    private void loginUser(String email,String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Log_In.this,R.string.Successful_Login,Toast.LENGTH_SHORT).show(); }
                        else {
                            Toast.makeText(Log_In.this,R.string.Unsuccessful_Login,Toast.LENGTH_SHORT).show(); }
                    }
                });
    }
}
