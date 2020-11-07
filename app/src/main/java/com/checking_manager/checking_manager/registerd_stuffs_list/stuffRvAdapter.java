package com.checking_manager.checking_manager.registerd_stuffs_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.checking_manager.checking_manager.OnItemClick;
import com.checking_manager.checking_manager.R;

import java.util.ArrayList;

public class stuffRvAdapter extends RecyclerView.Adapter<stuff_viewHolder> {

    private ArrayList<stuff> mDataList = new ArrayList<stuff>();
    private Context context;
    private OnItemClick mCallback;

    public stuffRvAdapter(Context context) {
        this.context = context;
    }

    public stuffRvAdapter(Context context, OnItemClick listener) {
        this.context = context;
        this.mCallback = listener;
    }

    public stuffRvAdapter(ArrayList<stuff> dataList) {
        mDataList = dataList;
    }

    @NonNull
    @Override
    public stuff_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
        View view = inflater.inflate(R.layout.stuff_lists, parent, false);
        stuff_viewHolder viewHolder = new stuff_viewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull stuff_viewHolder viewHolder, int position) {
        final String kind_of_stuff = mDataList.get(position).getKind_of_stuff();

        viewHolder.kind_of_stuff_setText(kind_of_stuff);
        viewHolder.the_number_of_total_setText(mDataList.get(position).getThe_number_of_total() + "");
        viewHolder.the_number_of_checked_setText(mDataList.get(position).getThe_number_of_checked() + "");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClick("to_position_page", kind_of_stuff);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addItem(String stuff_name, int number_of_total, int number_of_checked) {
        stuff item = new stuff(stuff_name, number_of_total, number_of_checked);
        mDataList.add(item);
    }

    public void clear() {
        int size = mDataList.size();
        for(int i = 0; i < size; i++)
            mDataList.remove(0);
    }

    public void removeItem(int position) {
        mDataList.remove(position);
    }

    public String get_stuff_name(int position) {
        return mDataList.get(position).getKind_of_stuff();
    }
}
