package com.checking_manager.checking_manager.registerd_stuffs_list;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.checking_manager.checking_manager.R;
import com.checking_manager.checking_manager.RecyclerViewOnItemClickListener;
import com.checking_manager.checking_manager.register_new_stuff.registration_page;
import com.checking_manager.checking_manager.stuff_positions_list.stuff_position_page;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

public class frag_mygroup extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private FloatingActionButton register_button;
    private String group_name = "", group_status = "", user_ID = "";
    private FirebaseDatabase database;
    private DatabaseReference reference, stuff_reference;
    private stuffRvAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private ProgressDialog dialog;
    private SwipeRefreshLayout refreshLayout;

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

        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.stuffs_swipeLayout);
        register_button = (FloatingActionButton)view.findViewById(R.id.register_floatingActionButton);
        recyclerView = (RecyclerView)view.findViewById(R.id.stuffs_recyclerView);
        dialog = new ProgressDialog(getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        refreshLayout.setOnRefreshListener(this);
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new stuffRvAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        if(bundle != null) {
            group_name = bundle.getString("group_name");
            register_button.setVisibility(View.GONE);
        }

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Groups").child(group_name).child("members");
        stuff_reference = database.getReference("Groups").child(group_name).child("stuff");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.setMessage("데이터 불러오는 중");
                dialog.show();
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
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        InitializeData();
        register_button.setOnClickListener(register_button_onClickListener);

        recyclerView.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getActivity(), recyclerView,
                new RecyclerViewOnItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        String stuff_name = adapter.get_stuff_name(position);
                        Intent intent = new Intent(getActivity(), stuff_position_page.class);
                        intent.putExtra("group_name", group_name);
                        intent.putExtra("group_status", group_status);
                        intent.putExtra("kind_of_stuff", stuff_name);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View v, final int position) {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                        dlg.setMessage("이 장비를 삭제하시겠습니까?");
                        dlg.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String stuff_name = adapter.get_stuff_name(position);
                                stuff_reference.child(stuff_name).removeValue();
                                adapter.removeItem(position);
                                adapter.notifyDataSetChanged();
                            }
                        });
                        dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dlg.show();
                    }
                }));

        return view;
    }

    public void InitializeData() {
        stuff_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.setMessage("데이터 불러오는 중");
                dialog.show();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String stuff_name = ds.getKey();
                    int total_number_of = Integer.parseInt(ds.child("total").getValue().toString());
                    int checked_number_of = Integer.parseInt(ds.child("checked").getValue().toString());

                    adapter.addItem(stuff_name, total_number_of, checked_number_of);
                    adapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    FloatingActionButton.OnClickListener register_button_onClickListener = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), registration_page.class);
            intent.putExtra("group_name", group_name);
            intent.putExtra("group_status", group_status);
            startActivity(intent);
            getActivity().finish();
        }
    };

    @Override
    public void onRefresh() {
        adapter.clear();
        InitializeData();
        refreshLayout.setRefreshing(false);
    }


}
