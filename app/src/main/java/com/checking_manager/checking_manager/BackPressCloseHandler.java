package com.checking_manager.checking_manager;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class BackPressCloseHandler {

    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;
    private View view;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public BackPressCloseHandler(Activity context, View view) {
        this.view = view;
        this.activity = context;
    }

    public void onBackPressed() {
        if(System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finishAffinity();
            System.runFinalization();
            System.exit(0);
            toast.cancel();
        }
    }

    public void showGuide() {
        //toast = Toast.makeText(activity, "\'뒤로가기\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        //Snackbar.make(view, "\'뒤로가기\'버튼을 한번 더 누르시면 종료됩니다.", Snackbar.LENGTH_SHORT).show();
        //toast.show();
    }
}
