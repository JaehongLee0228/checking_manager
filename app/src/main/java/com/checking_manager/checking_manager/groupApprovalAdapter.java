package com.checking_manager.checking_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class groupApprovalAdapter extends BaseAdapter {

    private ArrayList<groupApprovalList> arrayList = new ArrayList<groupApprovalList>();

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

        TextView user_ID = (TextView)convertView.findViewById(R.id.group_approval_list);

        groupApprovalList item = arrayList.get(position);

        user_ID.setText(item.getUser_ID());

        return convertView;
    }

    public void addItem(String user_ID) {
        groupApprovalList item = new groupApprovalList(user_ID);
        arrayList.add(item);
    }

}
