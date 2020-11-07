package com.checking_manager.checking_manager.stuff_positions_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.checking_manager.checking_manager.OnItemClick;
import com.checking_manager.checking_manager.R;
import com.checking_manager.checking_manager.RecyclerViewOnItemClickListener;
import com.checking_manager.checking_manager.checking_table_page;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class stuff_position_page extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Intent intent;
    private String group_name = "", group_status = "", kind_of_stuff = "";
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private positionRvAdapter adapter;
    private LinearLayoutManager manager;
    private SwipeRefreshLayout refreshLayout;
    private String stuffName = "";
    private ItemTouchHelper itemTouchHelper;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stuff_position_page);

        intent = getIntent();
        group_name = intent.getExtras().getString("group_name");
        group_status = intent.getExtras().getString("group_status");
        kind_of_stuff = intent.getExtras().getString("kind_of_stuff");
        stuffName = kind_of_stuff;

        view = (View) findViewById(R.id.stuff_position_page_view);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.stuff_position_swipeLayout);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.position_recyclerView);
        recyclerView.setLayoutManager(manager);
        adapter = new positionRvAdapter(stuff_position_page.this);
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);

        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Groups").child(group_name).child("stuff");

        reference.child(kind_of_stuff).child("position").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String position = ds.getKey();
                    Object checked_index_object = ds.child("checked_index").getValue();

                    String last_checked_string = "";
                    if (checked_index_object != null)
                        last_checked_string = ds.child("checked").child(checked_index_object.toString()).getValue().toString();
                    else last_checked_string = "점검된 기록 없음";
                    adapter.addItem(position, last_checked_string);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerViewOnItemClickListener(this, recyclerView,
                new RecyclerViewOnItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        String stuff_position = adapter.get_position(position);
                        intent = new Intent(stuff_position_page.this, checking_table_page.class);
                        intent.putExtra("group_name", group_name);
                        intent.putExtra("group_status", group_status);
                        intent.putExtra("position_string", stuff_position);
                        intent.putExtra("stuff_name", stuffName);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View v, final int position) {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(stuff_position_page.this);
                        dlg.setMessage("이 위치를 삭제하시겠습니까?");
                        dlg.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final Object[] checked_index_object = {null};
                                final int[] checked_value = {0};
                                final String stuff_position = adapter.get_position(position);
                                final int[] total = {0};
                                reference.child(kind_of_stuff).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        checked_value[0] = Integer.parseInt(snapshot.child("checked").getValue().toString());
                                        total[0] = Integer.parseInt(snapshot.child("total").getValue().toString());
                                        Log.d("total_value", total[0] + "");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                reference.child(kind_of_stuff).child("position").child(stuff_position).child("checked_index").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        checked_index_object[0] = snapshot.getValue();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (checked_index_object[0] != null)
                                            reference.child(kind_of_stuff).child("checked").setValue(checked_value[0] - 1);
                                        reference.child(kind_of_stuff).child("total").setValue(total[0] - 1);
                                        reference.child(kind_of_stuff).child("position").child(stuff_position).removeValue();
                                        adapter.remove(position);
                                        adapter.notifyDataSetChanged();
                                    }
                                }, 1000);

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
    }

    @Override
    public void onRefresh() {
        adapter.clear();

        reference.child(kind_of_stuff).child("position").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String position = ds.getKey();
                    Object checked_index_object = ds.child("checked_index").getValue();

                    String last_checked_string = "";
                    if (checked_index_object != null)
                        last_checked_string = ds.child("checked").child(checked_index_object.toString()).getValue().toString();
                    else last_checked_string = "점검된 기록 없음";
                    adapter.addItem(position, last_checked_string);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refreshLayout.setRefreshing(false);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            adapter.notifyDataSetChanged();

            Snackbar.make(view, "삭제하시겠습니까", Snackbar.LENGTH_LONG).setAction("삭제", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final boolean[] minus = {false};

                    reference.child(kind_of_stuff).child("position").child(adapter.get_position(position)).child("checked_index").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child("checked_index").getValue() == null);
                            else minus[0] = true;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    reference.child(kind_of_stuff).child("position").child(adapter.get_position(position)).removeValue();

                    reference.child(kind_of_stuff).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int total = Integer.parseInt(snapshot.child("total").getValue().toString());
                            reference.child(kind_of_stuff).child("total").setValue((total - 1) + "");
                            int checked = Integer.parseInt(snapshot.child("checked").getValue().toString());
                            if(minus[0])
                                reference.child(kind_of_stuff).child("checked").setValue((checked - 1) + "");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    adapter.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }).show();
        }
    };
}