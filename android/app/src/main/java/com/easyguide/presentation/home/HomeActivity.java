package com.easyguide.presentation.home;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.easyguide.BaseActivity;
import com.easyguide.R;

public class HomeActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_home);
    }
}
