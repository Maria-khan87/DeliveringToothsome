package com.example.foodapp.bawarchipager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.example.foodapp.BawarchiDashboard;
import com.example.foodapp.fragments.BawarchiMenu;
import com.example.foodapp.fragments.BawarchiOrders;
import com.example.foodapp.fragments.BawarchiProfile;

public class MyPagerAdapter extends FragmentStatePagerAdapter {


    BawarchiDashboard activity;
    public MyPagerAdapter(FragmentManager fm, BawarchiDashboard activity){
        super(fm);
        this.activity=activity;
    }
    @Override    public Fragment getItem(int position) {
        switch (position){
            case 0: return new BawarchiOrders(activity);
            case 1: return new BawarchiMenu(activity);
            case 2: return new BawarchiProfile(activity);
        }
        return null;
    }
    @Override
    public int getCount() {
        return 3;
    }
    @Override    public CharSequence getPageTitle(int position) {        switch (position){
        case 0: return "My Orders";
        case 1: return "My Menu";
        case 2: return "My Profile";
        default: return null;
    }
    }

}
