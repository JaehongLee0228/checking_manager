package com.checking_manager.checking_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class memberInGroupListAdapter extends BaseAdapter {

    private ArrayList<memberInGroupList> arrayList = new ArrayList<memberInGroupList>();

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

        memberInGroupList item = arrayList.get(position);

        member_ID.setText(item.getMember_ID());

        return convertView;
    }

    public void addItem(String member_ID) {
        memberInGroupList item = new memberInGroupList(member_ID);
        arrayList.add(item);
    }
}
