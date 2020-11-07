package com.checking_manager.checking_manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.checking_manager.checking_manager.group_information.frag_assign;
import com.checking_manager.checking_manager.registerd_stuffs_list.frag_mygroup;

public class ViewPagerAdaptor extends FragmentPagerAdapter {

    public ViewPagerAdaptor(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return frag_mygroup.newinstance();
            case 1:
                return frag_makegroup.newinstance();
            case 2:
                return frag_assign.newinstance();
            default:
                break;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "등록된 장비";
            case 1:
                return "물품 점검";
            case 2:
                return "그룹 정보";
            default:
                break;
        }
        return null;
    }
}
