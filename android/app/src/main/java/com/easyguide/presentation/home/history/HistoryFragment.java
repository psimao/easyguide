package com.easyguide.presentation.home.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easyguide.BaseFragment;
import com.easyguide.R;

public class HistoryFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, null);
    }

    @Override
    protected int fragmentTitleResourceId() {
        return R.string.history_title;
    }
}
