package com.checking_manager.checking_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class group_making extends AppCompatActivity {

    private Button member_add, make_group_complete, group_name_repeat;
    private EditText group_name_input, member_email_adding;
    private RadioButton admin, member;
    private FirebaseDatabase databse;
    private DatabaseReference reference, reference2;

    private ListView listView;
    private myGroups_listView_adapter adapter;

    private String users_ID;

    private boolean ready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_making);

        SharedPreferences LogInAuto=getSharedPreferences("AutoLogIn_SAVE",MODE_PRIVATE);

        users_ID = LogInAuto.getString("ID",null);

        member_add = (Button)findViewById(R.id.group_make_member_add_button);
        make_group_complete = (Button)findViewById(R.id.group_make_complete_button);
        group_name_input = (EditText)findViewById(R.id.group_name_EditText);
        member_email_adding = (EditText)findViewById(R.id.group_make_memberName_EditText);
        admin = (RadioButton)findViewById(R.id.group_make_admin_radioButton);
        member = (RadioButton)findViewById(R.id.group_make_member_radioButton);
        group_name_repeat = (Button)findViewById(R.id.group_make_groupName_repeat_button);

        adapter = new myGroups_listView_adapter();
        listView = (ListView)findViewById(R.id.group_make_addedMember_listView);

        databse = FirebaseDatabase.getInstance();
        reference = databse.getReference("Groups");
        reference2 = databse.getReference();

        listView.setAdapter(adapter);
        member_add.setOnClickListener(member_add_onClickListener);
        group_name_repeat.setOnClickListener(group_name_repeat_onClickListener);

        make_group_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ready) {
                    Toast.makeText(getApplicationContext(), "그룹 이름 중복 검사를 해주세요.", Toast.LENGTH_SHORT).cancel();
                    return;
                }

                showProgressDialog();

                final String group_name = group_name_input.getText().toString();
                reference.child(group_name).child("group_name").setValue(group_name);

                final int[] index = {0};
                for (; index[0] < adapter.getCount(); index[0]++) {
                    final String email = adapter.getItem(index[0]).getGroupName();
                    final String status = adapter.getItem(index[0]).getGroupStatus();

                    reference.child(group_name).child("members").child(index[0] + "").child("email").setValue(email);
                    reference.child(group_name).child("members").child(index[0] + "").child("status").setValue(status);

                    reference2.child("Members").child(stringReplace(email)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int count = 0;
                            for (DataSnapshot ds : snapshot.getChildren())
                                count++;

                            reference2.child("Members").child(stringReplace(email)).child(count + "").child("group_name").setValue(group_name);
                            reference2.child("Members").child(stringReplace(email)).child(count + "").child("group_status").setValue(status);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                reference.child(group_name).child("members").child(index[0] + "").child("email").setValue(users_ID);
                reference.child(group_name).child("members").child(index[0] + "").child("status").setValue("admin");

                reference2.child("Members").child(stringReplace(users_ID)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int count = 0;
                        for (DataSnapshot ds : snapshot.getChildren())
                            count++;

                        reference2.child("Members").child(stringReplace(users_ID)).child(count + "").child("group_name").setValue(group_name);
                        reference2.child("Members").child(stringReplace(users_ID)).child(count + "").child("group_status").setValue("admin");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        Intent intent = new Intent(group_making.this, Before_enter.class);
                        startActivity(intent);
                    }
                }, 1000);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(group_making.this);
                dlg.setMessage("이 멤버를 삭제하시겠습니까?");
                dlg.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int checked = listView.getCheckedItemPosition();
                        if(checked > -1 && checked < adapter.getCount()) {
                            adapter.removeItem(position);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dlg.show();
            }
        });
    }

    Button.OnClickListener group_name_repeat_onClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String group_name = group_name_input.getText().toString();
            if(group_name_input.getText() == null) {
                Toast.makeText(group_making.this,"그룹 이름을 입력해주세요.",  Toast.LENGTH_SHORT).cancel();
                return;
            }
            if(!keyValue_check(group_name)) {
                Toast.makeText(group_making.this, "그룹 이름에 '.', '#', '$', '[', or ']' 값을 사용하실 수 없습니다.", Toast.LENGTH_SHORT).cancel();
                return;
            }

            reference.child(group_name).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int judge = 0;
                    Object result = snapshot.child("group_name").getValue();
                    if(result != null) {
                        Toast.makeText(group_making.this,"이미 존재하는 그룹 이름입니다.",Toast.LENGTH_SHORT).show();
                        judge = 1;
                        return;
                    }
                    if(judge == 0) {
                        Toast.makeText(group_making.this, "사용할 수 있는 그룹 이름입니다.", Toast.LENGTH_SHORT).cancel();
                        ready = true;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    };


    Button.OnClickListener member_add_onClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = member_email_adding.getText().toString();
            if(!checkEmail(email)) {
                Toast.makeText(group_making.this,"올바른 이메일을 입력해주세요.",Toast.LENGTH_SHORT).cancel();
                return;
            }

            if(email.equals(users_ID)) {
                Toast.makeText(group_making.this,"회원님의 아이디입니다.", Toast.LENGTH_SHORT).cancel();
                return;
            }

            for(int i = 0; i < adapter.getCount(); i++) {
                String temp = adapter.getItem(i).getGroupName();
                if(temp.equals(email)) {
                    Toast.makeText(group_making.this,"이미 추가한 멤버입니다.", Toast.LENGTH_SHORT).cancel();
                    return;
                }
            }

            String status = "";
            if(admin.isChecked())
                status = "admin";
            else if(member.isChecked())
                status = "member";
            else {
                Toast.makeText(group_making.this, "관리자인지 일반멤버인지 선택해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            adapter.addItem(email, status);
            adapter.notifyDataSetChanged();

            member_email_adding.setText("");
        }
    };

    public Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static String stringReplace(String str){
        String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z]";
        str =str.replaceAll(match, "");
        return str;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(group_making.this, Before_enter.class));
        finish();
    }

    public boolean keyValue_check(String group_name) {
        for(int i = 0; i < group_name.length(); i++) {
            char temp = group_name.charAt(i);
            if (temp == '.' || temp == '#' || temp == '$' || temp == '[' || temp == ']')
                return false;
        }
        return true;
    }

    public void showProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("작업 중");
        dialog.show();
    }
}