package com.easyguide.presentation.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.easyguide.BaseActivity;
import com.easyguide.R;
import com.easyguide.presentation.home.history.HistoryFragment;
import com.easyguide.presentation.home.main.MainFragment;
import com.easyguide.presentation.home.profile.ProfileFragment;
import com.easyguide.ui.adapter.HomeAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tablayout_home)
    TabLayout tabLayout;

    @BindView(R.id.viewpager_home)
    ViewPager viewPagerHome;

    private Fragment[] tabsContent = new Fragment[]{
            new ProfileFragment(),
            new MainFragment(),
            new HistoryFragment()
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        for(Fragment fragment : tabsContent) {
            tabLayout.addTab(tabLayout.newTab().setText(fragment.getClass().getSimpleName().replace("Fragment", "")));
        }

        viewPagerHome.setAdapter(new HomeAdapter(getSupportFragmentManager(), tabsContent));

        viewPagerHome.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerHome.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPagerHome.setCurrentItem(1);
    }
}
