package com.checking_manager.checking_manager.My_Groups;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.checking_manager.checking_manager.R;

public class my_groups_viewHolder extends RecyclerView.ViewHolder {

    private TextView group_name;
    private TextView group_status;

    public my_groups_viewHolder(@NonNull View itemView) {
        super(itemView);

        group_name = itemView.findViewById(R.id.users_group_name);
        group_status = itemView.findViewById(R.id.users_group_status);
    }

    public void group_name_setText(String s) {
        group_name.setText(s);
    }

    public void group_status_setText(String s) {
        group_status.setText(s);
    }
}
