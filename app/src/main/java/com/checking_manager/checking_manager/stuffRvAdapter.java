package com.checking_manager.checking_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class stuffRvAdapter extends RecyclerView.Adapter<stuff_viewHolder> {

    private ArrayList<stuff> mDataList = new ArrayList<stuff>();

    public stuffRvAdapter() {
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
        viewHolder.kind_of_stuff.setText(mDataList.get(position).getKind_of_stuff());
        viewHolder.the_number_of_total.setText(mDataList.get(position).getThe_number_of_total() + "");
        viewHolder.the_number_of_checked.setText(mDataList.get(position).getThe_number_of_checked() + "");
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addItem(String stuff_name, int number_of_total, int number_of_checked) {
        stuff item = new stuff(stuff_name, number_of_total, number_of_checked);
        mDataList.add(item);
    }
}
