package com.checking_manager.checking_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class registrationContentAdapter extends BaseAdapter {

    private OnItemClick mCallback;
    private ArrayList<registrationContentList> arrayList = new ArrayList<registrationContentList>();
    private TextView content_textView;
    private Button delete_button;

    public registrationContentAdapter(OnItemClick listener) {
        this.mCallback = listener;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public registrationContentList getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.registe_content_lists, parent, false);
        }

        content_textView = (TextView)convertView.findViewById(R.id.registe_content_list);
        delete_button = (Button)convertView.findViewById(R.id.registe_content_delete_button);

        registrationContentList item = arrayList.get(position);
        final String check_content = item.getCheck_content();

        content_textView.setText(check_content);

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClick("delete", position + "");
            }
        });

        return convertView;
    }

    public void addItem(String check_content) {
        registrationContentList item = new registrationContentList(check_content);
        arrayList.add(item);
    }

    public void deleteItem(int position) {
        arrayList.remove(position);
    }
}
