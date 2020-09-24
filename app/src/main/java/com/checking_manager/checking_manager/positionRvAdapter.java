package com.checking_manager.checking_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class positionRvAdapter extends RecyclerView.Adapter<position_viewHolder> {

    private ArrayList<Position> mDataList = new ArrayList<Position>();
    private Context context;
    private OnItemClick mCallback;

    public positionRvAdapter(Context context, OnItemClick listener) {
        this.context = context;
        this.mCallback = listener;
    }

    public positionRvAdapter(ArrayList<Position> mDataList) {
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public position_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.position_lists, parent, false);
        position_viewHolder viewHolder = new position_viewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull position_viewHolder holder, int position) {
        final String position_string = mDataList.get(position).getPosition();
        String checked_string = mDataList.get(position).getChecked_complete();

        holder.setPosition(position_string);
        holder.setChecked(checked_string);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClick("to_checkList_page", position_string);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addItem(String position, String checked) {
        Position item = new Position(position, checked);
        mDataList.add(item);
    }

    public void clear() {
        int size = mDataList.size();
        for(int i = 0; i < size; i++)
            mDataList.remove(0);
    }
}
