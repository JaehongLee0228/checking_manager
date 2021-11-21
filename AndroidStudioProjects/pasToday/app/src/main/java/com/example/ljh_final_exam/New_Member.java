package com.example.ljh_final_exam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class New_Member extends AppCompatActivity {

//    //define view objects
//    EditText editTextEmail;
//    Button buttonSignup;
//    ProgressDialog progressDialog;
//    //define firebase object
//    FirebaseAuth firebaseAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        firebaseAuth = FirebaseAuth.getInstance();
////        if (firebaseAuth.getCurrentUser() != null) {
////            finish();
////            startActivity(new Intent(getApplicationContext(), MainActivity.class));
////        }
//
//
//        editTextEmail = (EditText) findViewById(R.id.IdEditText);
//        buttonSignup = (Button) findViewById(R.id.JoinButton);
//        progressDialog = new ProgressDialog(this);
//
//        buttonSignup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                registerUser();
//            }
//        });
//    }
//
//    private void registerUser() {
//        int valid = 1;
//        String email = editTextEmail.getText().toString().trim();
//        String password = "12345678";
//
//        if (TextUtils.isEmpty(email)) {
//            Toast.makeText(this, "웹메일 아이디를 입력하세요", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        final String realId = email;
//        email = email += "@knu.ac.kr";
//
//        //creating a new user
//        final String finalEmail = email;
//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            UserModel userModel = new UserModel();
//                            userModel.userName = realId;
//                            finish();
//
//                            String uid = task.getResult().getUser().getUid();
//                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);
//                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//
//                        } else {
//                            Toast.makeText(New_Member.this, "등록 에러", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        progressDialog.dismiss();
//
//                    }
//                });
//
//        progressDialog.setMessage("웹메일을 전송하는 중입니다");
//        progressDialog.show();
//
//
//        firebaseAuth.sendPasswordResetEmail(email)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            // Toast.makeText(MainActivity.this, "웹메일 전송 완료", Toast.LENGTH_LONG).show();
//                        } else {
//                            // Toast.makeText(MainActivity.this, "유효한 이메일이 아닙니다", Toast.LENGTH_LONG).show();
//                        }
//                        progressDialog.dismiss();
//                    }
//                });
//
//
//    }
//}

    EditText IdEditText, PWEditText, PWCHKEditText;
    Button JOINButton;

    private FirebaseAuth firebaseAuth;
    private String email = "";
    private String password = "";

    //비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new__member);


        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        IdEditText = (EditText) findViewById(R.id.IdEditText);
        PWEditText = (EditText) findViewById(R.id.PWEditText);
        PWCHKEditText = (EditText) findViewById(R.id.PWCHKEditText);
        JOINButton = (Button) findViewById(R.id.JoinButton);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void signUp(View view) {
        PWEditText = (EditText) findViewById(R.id.PWEditText);
        PWCHKEditText = (EditText) findViewById(R.id.PWCHKEditText);

        email = IdEditText.getText().toString();
        password = PWEditText.getText().toString();

        String PW = PWEditText.getText().toString().trim();
        String PWCHK = PWCHKEditText.getText().toString().trim();

        if (PW.equals(PWCHK)) {
            Intent intent = new Intent(New_Member.this, Log_In.class);
            if (isValidEmail() && isValidPassword()) {
                createUser(email, password);
            }
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(New_Member.this, "Please check your password again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void signIn(View view) {
        email = IdEditText.getText().toString();
        password = PWEditText.getText().toString();
        if (isValidEmail() && isValidPassword()) {
            loginUser(email, password);
        }
    }

    //이메일 유효성 검사사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        } else {
            return true;
        }
    }

    //비밀번호 유효성 검사
    private boolean isValidPassword() {
        if (password.isEmpty()) {
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return false;
        } else {
            return true;
        }
    }

    //회원가입
    private void createUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(New_Member.this, R.string.SuccessfulJoining, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(New_Member.this, R.string.UnsuccessfulJoining, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //로그인
    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(New_Member.this, R.string.Successful_Login, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(New_Member.this, R.string.Unsuccessful_Login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

