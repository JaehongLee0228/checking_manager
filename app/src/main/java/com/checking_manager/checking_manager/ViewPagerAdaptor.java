package com.checking_manager.checking_manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
                return "가입한 그룹";
            case 1:
                return "그룹 만들기";
            case 2:
                return "그룹 가입하기";
            default:
                break;
        }
        return null;
    }
}
