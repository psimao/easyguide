package com.easyguide.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class HomeAdapter extends FragmentStatePagerAdapter {

    private Fragment[] adapterContent;

    public HomeAdapter(FragmentManager fm, Fragment[] adapterContent) {
        super(fm);
        this.adapterContent = adapterContent;
    }

    @Override
    public Fragment getItem(int position) {
        return adapterContent[position];
    }

    @Override
    public int getCount() {
        return adapterContent.length;
    }
}
