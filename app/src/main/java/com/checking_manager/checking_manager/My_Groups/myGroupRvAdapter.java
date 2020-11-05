package com.checking_manager.checking_manager.My_Groups;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.checking_manager.checking_manager.Main_sum;
import com.checking_manager.checking_manager.R;

import java.util.ArrayList;

public class myGroupRvAdapter extends RecyclerView.Adapter<my_groups_viewHolder> {

    private ArrayList<usersGroupsList> mDataList = new ArrayList<usersGroupsList>();
    private Context mContext;

    public myGroupRvAdapter(Context context) {
        this.mContext = context;
    }

    public myGroupRvAdapter(ArrayList<usersGroupsList> dataList) {
        mDataList = dataList;
    }

    @NonNull
    @Override
    public my_groups_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
        View view = inflater.inflate(R.layout.users_groups_list, parent, false);
        my_groups_viewHolder viewHolder = new my_groups_viewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull my_groups_viewHolder viewHolder, int position) {
        final String group_name = mDataList.get(position).getGroupName();
        final String group_status = mDataList.get(position).getGroupStatus();

        viewHolder.group_name_setText(group_name);
        viewHolder.group_status_setText(group_status);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Main_sum.class);
                intent.putExtra("group_name", group_name);
                intent.putExtra("group_status", group_status);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addItem(String group_name, String group_status) {
        usersGroupsList item = new usersGroupsList(group_name, group_status);
        mDataList.add(item);
    }

    public void clear() {
        int size = mDataList.size();
        for(int i = 0; i < size; i++)
            mDataList.remove(0);
    }
}
