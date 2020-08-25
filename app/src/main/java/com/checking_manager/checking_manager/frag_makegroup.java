package com.checking_manager.checking_manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class frag_makegroup  extends Fragment {
    private View view;

    private IntentIntegrator qrScan;
    Button scan_btn;

    public static frag_makegroup newinstance(){
        frag_makegroup fragsnd = new frag_makegroup();
        return fragsnd;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_snd,container, false);

        scan_btn = (Button)view.findViewById(R.id.btn_scan);
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forScan();
            }
        });


        return view;
    }

    private void forScan() {
        //qrScan = new IntentIntegrator(getActivity());
        qrScan = IntentIntegrator.forSupportFragment(frag_makegroup.this);
        qrScan.setOrientationLocked(false);
        qrScan.setPrompt("Take your Camera to QR Code");
        qrScan.initiateScan();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getActivity(), "Scanned : " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
