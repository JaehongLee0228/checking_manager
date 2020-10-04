package com.checking_manager.checking_manager;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class contents_lists_viewHolder extends RecyclerView.ViewHolder {

    private TextView special, okay, content;

    public contents_lists_viewHolder(@NonNull View itemView) {
        super(itemView);

        special = itemView.findViewById(R.id.contents_lists_special);
        okay = itemView.findViewById(R.id.contents_lists_okay);
        content = itemView.findViewById(R.id.kind_of_contents);
    }

    public void contents_lists_setContent(String s) {
        content.setText(s);
    }

    public void contents_lists_setSpecial(String s) {
        special.setText(s);
    }

    public void contents_lists_setOkay(String s) {
        okay.setText(s);
    }
}
