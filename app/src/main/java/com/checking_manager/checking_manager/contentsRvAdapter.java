package com.checking_manager.checking_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class contentsRvAdapter extends RecyclerView.Adapter<contents_lists_viewHolder> {

    private Context context;
    private ArrayList<Contents> mDataList = new ArrayList<Contents>();

    public contentsRvAdapter(Context context) {
        this.context = context;
    }

    public contentsRvAdapter(ArrayList<Contents> arrayList) {
        this.mDataList = arrayList;
    }

    @NonNull
    @Override
    public contents_lists_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.contents_lists, parent, false);
        contents_lists_viewHolder viewHolder = new contents_lists_viewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull contents_lists_viewHolder holder, int position) {
        String special = mDataList.get(position).getSpecial();
        String okay = mDataList.get(position).getOkay();
        String content = mDataList.get(position).getContent();

        holder.contents_lists_setOkay(okay);
        holder.contents_lists_setSpecial(special);
        holder.contents_lists_setContent(content);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addItem(String content, String okay, String special) {
        Contents item = new Contents(content, okay, special);
        mDataList.add(item);
    }

    public void remove(int position) {
        mDataList.remove(position);
    }

    public void clear() {
        int size = mDataList.size();
        for(int i = 0; i < size; i++)
            mDataList.remove(0);
    }
}
