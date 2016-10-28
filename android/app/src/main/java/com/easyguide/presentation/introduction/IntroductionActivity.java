package com.easyguide.presentation.introduction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.easyguide.BaseActivity;
import com.easyguide.R;

import butterknife.BindView;

public class IntroductionActivity extends BaseActivity {

    @BindView(R.id.intro_view_pager)
    ViewPager introViewPager;

    private int[] icons = new int[]{};
    private int[] titles = new int[]{};
    private int[] messages = new int[]{};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
    }
}
