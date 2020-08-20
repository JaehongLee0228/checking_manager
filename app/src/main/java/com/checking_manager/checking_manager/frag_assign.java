package com.checking_manager.checking_manager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class frag_assign  extends Fragment {
    private View view;

    public static frag_assign newinstance(){
        frag_assign fragtrd = new frag_assign();
        return fragtrd;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_trd,container, false);

        return view;
    }
}
