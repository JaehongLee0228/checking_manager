package com.checking_manager.checking_manager.checking_sequence;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class checking_page_adapter extends FragmentPagerAdapter {
    int length;
    private ArrayList<String> quesGet = new ArrayList<String>();
    public checking_page_adapter(@NonNull FragmentManager fm, int size, ArrayList<String> ques) {
        super(fm);
        length = size;

        for(int i = 0; i < ques.size(); ++i){
            String tmp = ques.get(i);
            quesGet.add(tmp);
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        checking_page_fragment fragment = new checking_page_fragment();

        Bundle bundle = new Bundle();
        bundle.putString("question", quesGet.get(position));
        bundle.putInt("index", position);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return length;
    }
}
