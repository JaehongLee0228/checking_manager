package com.checking_manager.checking_manager.group_information;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.checking_manager.checking_manager.OnItemClick;
import com.checking_manager.checking_manager.R;

import java.util.ArrayList;

public class memberInGroupListAdapter extends BaseAdapter {

    private OnItemClick mCallback;
    private ArrayList<memberInGroupList> arrayList = new ArrayList<memberInGroupList>();
    private Button promotion_button, kickOut_button;
    private TextView user_ID_textView;
    private String user_ID = "";

    public memberInGroupListAdapter(OnItemClick listener) {
        this.mCallback = listener;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public memberInGroupList getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.member_in_group_lists, parent, false);
        }

        TextView member_ID = (TextView)convertView.findViewById(R.id.member_in_group_list);
        kickOut_button = (Button)convertView.findViewById(R.id.member_in_group_kickOut);
        promotion_button = (Button)convertView.findViewById(R.id.member_in_group_promotion);
        user_ID_textView = (TextView)convertView.findViewById(R.id.member_in_group_list);

        memberInGroupList item = arrayList.get(position);
        user_ID = user_ID_textView.getText().toString();

        member_ID.setText(item.getMember_ID());

        promotion_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClick("promotion", user_ID);
            }
        });

        kickOut_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClick("kick_out", user_ID);
            }
        });

        return convertView;
    }

    public void addItem(String member_ID) {
        memberInGroupList item = new memberInGroupList(member_ID);
        arrayList.add(item);
    }
}
