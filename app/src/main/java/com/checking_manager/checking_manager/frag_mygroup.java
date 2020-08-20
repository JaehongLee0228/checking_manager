package com.checking_manager.checking_manager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class frag_mygroup extends Fragment {
    private View view;

    public static frag_mygroup newinstance(){
        frag_mygroup fragfirst = new frag_mygroup();
        return fragfirst;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_first,container, false);

        return view;
    }
}
