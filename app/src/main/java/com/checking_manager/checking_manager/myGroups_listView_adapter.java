package com.checking_manager.checking_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class myGroups_listView_adapter extends BaseAdapter {

    private ArrayList<usersGroupsList> listViewGroupList = new ArrayList<usersGroupsList>();

    @Override
    public int getCount() {
        return listViewGroupList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewGroupList.get(position);
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
            convertView = inflater.inflate(R.layout.users_groups_list, parent, false);
        }

        TextView my_group_name = (TextView)convertView.findViewById(R.id.users_group_name);
        TextView my_group_status = (TextView)convertView.findViewById(R.id.users_group_status);

        usersGroupsList users_groups_list = listViewGroupList.get(position);

        my_group_name.setText(users_groups_list.getGroupName());
        my_group_status.setText(users_groups_list.getGroupStatus());

        return convertView;
    }

    public void addItem(String group_name, String group_status) {
        usersGroupsList item = new usersGroupsList(group_name, group_status);
        listViewGroupList.add(item);
    }
}
