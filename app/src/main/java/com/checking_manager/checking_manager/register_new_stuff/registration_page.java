package com.checking_manager.checking_manager.register_new_stuff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.checking_manager.checking_manager.Main_sum;
import com.checking_manager.checking_manager.OnItemClick;
import com.checking_manager.checking_manager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class registration_page extends AppCompatActivity implements OnItemClick {

    private Button add_button, complete_button;
    private EditText position_editText, kind_editText, content_editText;
    private ListView content_listView;
    private registrationContentAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private String group_name, period = "", group_status;
    private RadioButton week_radioButton, month_radioButton;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

        group_name = getIntent().getExtras().getString("group_name");
        group_status = getIntent().getExtras().getString("group_status");

        add_button = (Button)findViewById(R.id.registe_add_button);
        position_editText = (EditText)findViewById(R.id.registe_stuff_position);
        kind_editText = (EditText)findViewById(R.id.registe_kind_of_stuff);
        content_editText = (EditText)findViewById(R.id.registe_check_content_editText);
        complete_button = (Button)findViewById(R.id.registe_complete_button);
        week_radioButton = (RadioButton)findViewById(R.id.register_week_radioButton);
        month_radioButton = (RadioButton)findViewById(R.id.register_month_radioButton);

        adapter = new registrationContentAdapter(this);
        content_listView = (ListView)findViewById(R.id.registe_listView);
        content_listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(content_listView);
        context = this;

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Groups").child(group_name);

        add_button.setOnClickListener(add_onClickListener);
        complete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String stuff = kind_editText.getText().toString();
                final String position = position_editText.getText().toString();
                if(stuff.equals("")) {
                    Toast.makeText(registration_page.this, "물품 종류를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(position.equals("")) {
                    Toast.makeText(registration_page.this, "물품 위치를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(adapter.getCount() == 0) {
                    Toast.makeText(registration_page.this, "점검해야할 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(week_radioButton.isChecked())
                    period = "week";
                else if(month_radioButton.isChecked())
                    period = "month";
                else {
                    Toast.makeText(registration_page.this, "점검주기를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                reference.child("stuff").child(stuff).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final ProgressDialog dialog = new ProgressDialog(registration_page.this);
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.setMessage("작업 중");
                        dialog.show();
                        for (DataSnapshot ds : snapshot.child("position").getChildren()) {
                            String existed_position = ds.getKey();
                            if(position.equals(existed_position)) {
                                Toast.makeText(registration_page.this, "이미 등록하신 물품의 위치입니다.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                return;
                            }
                        }

                        for(int i = 0; i < adapter.getCount(); i++)
                            reference.child("stuff").child(stuff).child("position").child(position).child("contents").child(i + "").setValue(adapter.getItem(i).getCheck_content());
                        reference.child("stuff").child(stuff).child("position").child(position).child("period").setValue(period);
                        Object howmany_object = snapshot.child("total").getValue();
                        Object checked_index_object = snapshot.child("position").child(position).child("checked_index").getValue();
                        if(!(howmany_object == null)) {
                            int howmany = Integer.parseInt(howmany_object.toString());
                            reference.child("stuff").child(stuff).child("total").setValue(++howmany);
                        }
                        else {
                            reference.child("stuff").child(stuff).child("total").setValue(1);
                            reference.child("stuff").child(stuff).child("checked").setValue(0);
                        }

                        Handler mHandler = new Handler();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                dialog.dismiss();
                                Intent intent = new Intent(context, Main_sum.class);
                                intent.putExtra("group_name", group_name);
                                intent.putExtra("group_status", group_status);
                                startActivity(intent);
                            }
                        }, 1000);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    Button.OnClickListener add_onClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            String content = content_editText.getText().toString();
            if(content.equals("")){
                Toast.makeText(registration_page.this, "점검 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }else {
                adapter.addItem(content);
                adapter.notifyDataSetChanged();
                content_editText.setText("");
                setListViewHeightBasedOnChildren(content_listView);
            }
        }
    };

    @Override
    public void onClick(String value, String position) {
        if(value.equals("delete"))
            delete_content(Integer.parseInt(position));
    }

    public void delete_content(int position) {
        adapter.deleteItem(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, Main_sum.class);
        intent.putExtra("group_name", group_name);
        intent.putExtra("group_status", group_status);
        startActivity(intent);
        finish();
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