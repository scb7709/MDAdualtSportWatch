package com.headlth.management.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentsList;
    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {

        super(fm);
        this.fragmentsList = fragments;
    }

    @Override
    public int getCount() {
        Log.e("tag", "getCount");
        return fragmentsList.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        Log.e("nage",arg0+"");
        return fragmentsList.get(arg0);
    }



}
