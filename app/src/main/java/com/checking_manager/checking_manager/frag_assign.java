package com.checking_manager.checking_manager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.checking_manager.checking_manager.My_Groups.Before_enter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class frag_assign  extends Fragment implements OnItemClick{

    private View view;
    private TextView my_group_name_textView, group_approval_textView;
    private Button withdrawl_button;
    private ListView admin_listView, member_listView, approval_listView;
    private ArrayList<String> admin_list;
    private ArrayAdapter<String> admin_adapter;
    private FirebaseDatabase database;
    private DatabaseReference reference, accept_reference;
    private memberInGroupListAdapter member_adapter;
    private groupApprovalAdapter approval_adapter;
    private String group_name = "";
    private String IdAuto = "";

    private int admin_count = 0;
    private String my_status = "";

    public static frag_assign newinstance(){
        frag_assign fragtrd = new frag_assign();
        return fragtrd;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_trd, container, false);

        Context context = getActivity();
        final SharedPreferences LogInAuto = context.getSharedPreferences("AutoLogIn_SAVE", Context.MODE_PRIVATE);
        IdAuto = LogInAuto.getString("ID", null);

        group_approval_textView = (TextView) view.findViewById(R.id.group_approval_TextView);
        approval_listView = (ListView) view.findViewById(R.id.group_approval_listView);
        member_listView = (ListView) view.findViewById(R.id.member_listView);
        admin_listView = (ListView) view.findViewById(R.id.admin_listView);
        withdrawl_button = (Button) view.findViewById(R.id.withdrawl_Button);
        my_group_name_textView = (TextView) view.findViewById(R.id.tv_groupname);

        group_name = getActivity().getIntent().getExtras().getString("group_name");
        my_group_name_textView.setText(group_name);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Groups").child(group_name);
        accept_reference = database.getReference();

        admin_list = new ArrayList<String>();
        admin_adapter = new ArrayAdapter<String>
                (getActivity(), R.layout.admin_in_group_lists, R.id.admin_in_group_list, admin_list);
        admin_listView.setAdapter(admin_adapter);

        member_adapter = new memberInGroupListAdapter(this);
        member_listView.setAdapter(member_adapter);


        approval_adapter = new groupApprovalAdapter(this);
        approval_listView.setAdapter(approval_adapter);

        group_approval_textView.setVisibility(View.GONE);
        approval_listView.setVisibility(View.GONE);

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
                if(my_status == "admin") {
                    approval_listView.setVisibility(View.VISIBLE);
                    group_approval_textView.setVisibility(View.VISIBLE);
                }
                setListViewHeightBasedOnChildren(admin_listView);
                setListViewHeightBasedOnChildren(member_listView);
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
                setListViewHeightBasedOnChildren(approval_listView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        withdrawl_button.setOnClickListener(withdrawl_onClickListener);

        approval_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return view;
    }

    Button.OnClickListener withdrawl_onClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(my_status.equals("admin")) {
                if(admin_count == 1) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                    dlg.setMessage("관리자가 회원님 뿐입니다. 그룹을 삭제하시겠습니까?");
                    dlg.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            withdrawl();
                        }
                    });
                    dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dlg.show();
                    return;
                }
            }
            AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
            dlg.setMessage("그룹을 탈퇴하시겠습니까?");
            dlg.setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    withdrawl();
                }
            });
            dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dlg.show();
        }
    };

    private void promotion(final String ID) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("작업 중");
        dialog.show();

        reference.child("members").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String email = ds.child("email").getValue().toString();
                    if(email.equals(ID)) {
                        String temp_key = ds.getKey();
                        reference.child("members").child(temp_key).child("status").setValue("admin");

                        accept_reference.child("Members").child(stringReplace(ID)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    String temp_group_name = ds.child("group_name").getValue().toString();
                                    if(temp_group_name.equals(group_name)) {
                                        String temp_key2 = ds.getKey();
                                        accept_reference.child("Members").child(stringReplace(ID)).child(temp_key2).child("group_status").setValue("admin");
                                        return;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frag_assign.this).attach(frag_assign.this).commit();
                dialog.dismiss();
            }
        }, 1000);
    }

    private void kick_out(final String ID) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("작업 중");
        dialog.show();

        reference.child("members").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String email = ds.child("email").getValue().toString();
                    if(email.equals(ID)) {
                        String temp_key = ds.getKey();
                        reference.child("members").child(temp_key).removeValue();

                        accept_reference.child("Members").child(stringReplace(ID)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    String temp_group_name = ds.child("group_name").getValue().toString();
                                    if(temp_group_name.equals(group_name)) {
                                        String temp_key2 = ds.getKey();
                                        accept_reference.child("Members").child(stringReplace(ID)).child(temp_key2).removeValue();
                                        return;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frag_assign.this).attach(frag_assign.this).commit();
                dialog.dismiss();
            }
        }, 1000);
    }

    private void accept(final String ID) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("작업 중");
        dialog.show();
        reference.child("approval").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    final String requester = ds.getValue().toString();
                    if(requester.equals(ID)) {
                        String temp_key = ds.getKey();
                        reference.child("approval").child(temp_key).removeValue();
                        reference.child("members").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int count = 0;
                                for(DataSnapshot ds : snapshot.getChildren())
                                    count++;
                                reference.child("members").child(count + "").child("email").setValue(ID);
                                reference.child("members").child(count + "").child("status").setValue("member");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        accept_reference.child("Members").child(stringReplace(ID)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int count = 0;
                                for (DataSnapshot ds : snapshot.getChildren())
                                    count++;

                                accept_reference.child("Members").child(stringReplace(ID)).child(count + "").child("group_name").setValue(group_name);
                                accept_reference.child("Members").child(stringReplace(ID)).child(count + "").child("group_status").setValue("member");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frag_assign.this).attach(frag_assign.this).commit();
                dialog.dismiss();
            }
        }, 1000);
    }

    private void decline(final String ID) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("작업 중");
        dialog.show();
        reference.child("approval").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String requester = ds.getValue().toString();
                    if(requester.equals(ID)) {
                        String temp_key = ds.getKey();
                        reference.child("approval").child(temp_key).removeValue();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frag_assign.this).attach(frag_assign.this).commit();
                dialog.dismiss();
            }
        }, 1000);
    }

    private void withdrawl() {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("작업 중");
        dialog.show();

        reference.child("members").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String temp_email = ds.child("email").getValue().toString();
                    if(temp_email.equals(IdAuto)) {
                        String temp_key = ds.getKey();
                        reference.child("members").child(temp_key).removeValue();
                        break;
                    }
                }

                final DatabaseReference temp_reference = database.getReference().child("Members").child(stringReplace(IdAuto));
                temp_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            String temp_group_name = ds.child("group_name").getValue().toString();
                            if(temp_group_name.equals(group_name)) {
                                String temp_key = ds.getKey();
                                temp_reference.child(temp_key).removeValue();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().finish();
                startActivity(new Intent(getActivity(), Before_enter.class));
            }
        }, 1000);
    }

    public String stringReplace(String str){
        String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z]";
        str =str.replaceAll(match, "");
        return str;
    }

    @Override
    public void onClick(String value, String ID) {
        if(value.equals("accept"))
            accept(ID);
        else if(value.equals("decline"))
            decline(ID);
        else if(value.equals("promotion")) {
            if(my_status.equals("member"))
                Toast.makeText(getActivity(), "관리자만 사용할 수 있는 기능입니다.", Toast.LENGTH_SHORT).show();
            else if(my_status.equals("admin"))
                promotion(ID);
            else Toast.makeText(getActivity(), "잠시후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
        else if(value.equals("kick_out")) {
            if(my_status.equals("member"))
                Toast.makeText(getActivity(), "관리자만 사용할 수 있는 기능입니다.", Toast.LENGTH_SHORT).show();
            else if(my_status.equals("admin"))
                kick_out(ID);
            else Toast.makeText(getActivity(), "잠시후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            //listItem.measure(0, 0);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight;
        listView.setLayoutParams(params);

        listView.requestLayout();
    }
}
