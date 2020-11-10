package com.checking_manager.checking_manager.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.checking_manager.checking_manager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sign_up_activity extends AppCompatActivity {

    private EditText signUp_ID;
    private EditText signUp_PW;
    private EditText signUp_PW_check;
    private Button signUp_complete;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        signUp_ID = (EditText)findViewById(R.id.signUp_ID_editText);
        signUp_PW = (EditText)findViewById(R.id.signUp_PW_editText);
        signUp_PW_check = (EditText)findViewById(R.id.signUp_PW_check_editText);
        signUp_complete = (Button)findViewById(R.id.signUp_completeButton);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        signUp_PW_check.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        // 검색 동작

                        break;
                    default:
                        signUp_complete.performClick();
                        return false;
                }
                return true;
            }
        });

        signUp_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email_input = signUp_ID.getText().toString();
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
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        reference.child("Verification").child(StringReplace(email_input)).setValue("true");
                                        Toast.makeText(sign_up_activity.this, "이메일 인증을 진행해주세요.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(sign_up_activity.this,"유효하지 않은 이메일입니다.",Toast.LENGTH_SHORT).show();
                                    }
                                });
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

    private ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
            .setUrl("https://checking-manager.firebaseapp.com/__/auth/action?mode=action&oobCode=code")
            .setHandleCodeInApp(true)
            .setAndroidPackageName("com.checking_manager.android", true, "1")
            .build();
}
