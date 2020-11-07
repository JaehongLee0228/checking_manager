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

public class groupApprovalAdapter extends BaseAdapter {

    private OnItemClick mCallback;
    private ArrayList<groupApprovalList> arrayList = new ArrayList<groupApprovalList>();
    private Button accept_button, decline_button;
    private TextView user_ID_textView;
    private String user_ID = "";

    public groupApprovalAdapter(OnItemClick listener) {
        this.mCallback = listener;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public groupApprovalList getItem(int position) {
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
            convertView = inflater.inflate(R.layout.group_approval_lists, parent, false);
        }

        user_ID_textView = (TextView)convertView.findViewById(R.id.group_approval_list);
        accept_button = (Button)convertView.findViewById(R.id.group_approval_accept_Button);
        decline_button = (Button)convertView.findViewById(R.id.group_approval_decline_Button);

        groupApprovalList item = arrayList.get(position);
        user_ID = item.getUser_ID();

        user_ID_textView.setText(user_ID);

        accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClick("accept", user_ID);
            }
        });
        decline_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClick("decline", user_ID);
            }
        });

        return convertView;
    }

    public void addItem(String user_ID) {
        groupApprovalList item = new groupApprovalList(user_ID);
        arrayList.add(item);
    }
}
