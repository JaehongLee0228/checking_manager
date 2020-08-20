package com.checking_manager.checking_manager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class frag_makegroup  extends Fragment {
    private View view;

    public static frag_makegroup newinstance(){
        frag_makegroup fragsnd = new frag_makegroup();
        return fragsnd;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_snd,container, false);

        return view;
    }
}
