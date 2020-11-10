package com.checking_manager.checking_manager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.checking_manager.checking_manager.login.log_in;
import com.checking_manager.checking_manager.login.sign_up_activity;
import com.google.android.material.tabs.TabLayout;

public class Main_sum extends AppCompatActivity {

    private FragmentPagerAdapter fragmentPagerAdapter;
    public static Class returnClass;
    private String group_name;
    private Button Btn_back;
    private TextView Tv_title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        returnClass = this.getClass();
        ViewPager viewPager = findViewById(R.id.viewpager);
        fragmentPagerAdapter = new ViewPagerAdaptor(getSupportFragmentManager());

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Btn_back = findViewById(R.id.btn_back);
        Tv_title = findViewById(R.id.tv_title);
        group_name = getIntent().getStringExtra("group_name");
        Tv_title.setText(group_name);

        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }
}
