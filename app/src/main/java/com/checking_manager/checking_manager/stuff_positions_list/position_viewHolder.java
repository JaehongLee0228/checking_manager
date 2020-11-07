package com.checking_manager.checking_manager.stuff_positions_list;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.checking_manager.checking_manager.R;

public class position_viewHolder extends RecyclerView.ViewHolder {

    private TextView position;
    private TextView checked;

    public position_viewHolder(@NonNull View itemView) {
        super(itemView);

        position = itemView.findViewById(R.id.position_lists_textView);
        checked = itemView.findViewById(R.id.position_lists_checked_complete);
    }

    public void setPosition(String s) {
        position.setText(s);
    }

    public void setChecked(String s) {
        checked.setText(s);
    }
}
