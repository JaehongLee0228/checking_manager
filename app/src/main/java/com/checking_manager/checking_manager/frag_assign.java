package com.checking_manager.checking_manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class frag_assign  extends Fragment {

    private View view;
    private TextView my_group_name_textView;
    private Button withdrawl_button;
    private ListView admin_listView, member_listView, approval_listView;
    private ArrayList<String> admin_list;
    private ArrayAdapter<String> admin_adapter;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private memberInGroupListAdapter member_adapter;
    private groupApprovalAdapter approval_adapter;

    private int admin_count = 0;
    private String my_status;

    public static frag_assign newinstance(){
        frag_assign fragtrd = new frag_assign();
        return fragtrd;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_trd,container, false);

        Context context = getActivity();
        final SharedPreferences LogInAuto = context.getSharedPreferences("AutoLogIn_SAVE", Context.MODE_PRIVATE);
        final String IdAuto = LogInAuto.getString("ID",null);

        approval_listView = (ListView)view.findViewById(R.id.group_approval_listView);
        member_listView = (ListView)view.findViewById(R.id.member_listView);
        admin_listView = (ListView)view.findViewById(R.id.admin_listView);
        withdrawl_button = (Button)view.findViewById(R.id.withdrawl_Button);
        my_group_name_textView = (TextView)view.findViewById(R.id.tv_groupname);

        final String group_name = getActivity().getIntent().getExtras().getString("group_name");
        my_group_name_textView.setText(group_name);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference().child("Groups").child(group_name);

        admin_list = new ArrayList<String>();
        admin_adapter = new ArrayAdapter<String>
                (getActivity(), R.layout.admin_in_group_lists, R.id.admin_in_group_list, admin_list);
        admin_listView.setAdapter(admin_adapter);

        member_adapter = new memberInGroupListAdapter();
        member_listView.setAdapter(member_adapter);

        approval_adapter = new groupApprovalAdapter();
        approval_listView.setAdapter(approval_adapter);

        reference.child("members").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String status = ds.child("status").getValue().toString();
                    String email = ds.child("email").getValue().toString();
                    if(status.equals("member")) {
                        if(email.equals(IdAuto))
                            my_status = "member";
                        member_adapter.addItem(email);
                        member_adapter.notifyDataSetChanged();
                        member_listView.setAdapter(member_adapter);
                    } else if(status.equals("admin")) {
                        if(email.equals(IdAuto))
                            my_status = "admin";
                        admin_count++;
                        admin_list.add(email);
                        admin_adapter.notifyDataSetChanged();
                        admin_listView.setAdapter(admin_adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("approval").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String email = ds.getValue().toString();
                    approval_adapter.addItem(email);
                    approval_adapter.notifyDataSetChanged();
                    approval_listView.setAdapter(approval_adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        withdrawl_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(my_status.equals("admin")) {
                    if(admin_count == 1) {
                        Toast.makeText(getActivity(), "관리자가 회원님 뿐입니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //탈퇴하기 기능 넣어야한다..
                } else if(my_status.equals("member")) {
                    //탈퇴하기 기능 넣어야한다..
                }
            }
        });

        return view;
    }
}
