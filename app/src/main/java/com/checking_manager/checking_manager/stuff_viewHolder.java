package com.checking_manager.checking_manager;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class stuff_viewHolder extends RecyclerView.ViewHolder {

    TextView kind_of_stuff;
    TextView the_number_of_total;
    TextView the_number_of_checked;

    public stuff_viewHolder(@NonNull View itemView) {
        super(itemView);

        kind_of_stuff = itemView.findViewById(R.id.kind_of_stuff);
        the_number_of_total = itemView.findViewById(R.id.the_number_of_total);
        the_number_of_checked = itemView.findViewById(R.id.the_number_of_checked);
    }
}
