package com.easyguide;

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {

    protected abstract int fragmentTitleResourceId();

    public int getFragmentTitleResourceId(){
        return this.fragmentTitleResourceId();
    }

}
