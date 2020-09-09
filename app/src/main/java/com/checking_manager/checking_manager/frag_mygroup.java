package com.checking_manager.checking_manager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

public class frag_mygroup extends Fragment {

    private View view;
    private FloatingActionButton register_button;
    private String group_name = "", group_status = "", user_ID = "";
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private int count = 0;

    public static frag_mygroup newinstance(){
        frag_mygroup fragfirst = new frag_mygroup();
        return fragfirst;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_first,container, false);
        Bundle bundle = getActivity().getIntent().getExtras();

        SharedPreferences LogInAuto= getActivity().getSharedPreferences("AutoLogIn_SAVE",MODE_PRIVATE);
        user_ID = LogInAuto.getString("ID",null);

        register_button = (FloatingActionButton)view.findViewById(R.id.register_floatingActionButton);

        if(bundle != null) {
            group_name = bundle.getString("group_name");
            register_button.setVisibility(View.GONE);
        }

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Groups").child(group_name).child("members");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String email = ds.child("email").getValue().toString();
                    Log.d("mygroup_email", email);
                    if(email.equals(user_ID)) {
                        group_status = ds.child("status").getValue().toString();
                        if(group_status.equals("admin"))
                            register_button.setVisibility(View.VISIBLE);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        register_button.setOnClickListener(register_button_onClickListener);

        return view;
    }

    FloatingActionButton.OnClickListener register_button_onClickListener = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), registration_page.class);
            intent.putExtra("group_name", group_name);
            intent.putExtra("group_status", group_status);
            startActivity(intent);
        }
    };
}
