package com.easyguide.presentation.launch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.easyguide.BaseActivity;
import com.easyguide.Injection;
import com.easyguide.R;
import com.easyguide.presentation.home.HomeActivity;
import com.easyguide.presentation.introduction.IntroductionActivity;
import com.easyguide.presentation.login.LoginActivity;

public class LaunchActivity extends BaseActivity implements LaunchContract.View {

    private static final long DELAY_MILLISECONDS = 1000;

    private LaunchContract.Presenter presenter;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        new LaunchPresenter(
                this,
                Injection.providePreferencesRepository(this),
                Injection.provideUserRepository(getApplicationContext()),
                Injection.provideSchedulerProvider()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(delayedRunnable, DELAY_MILLISECONDS);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(delayedRunnable);
        presenter.unsubscribe();
    }

    @Override
    public void startIntroductionActivity() {
        baseStartActivity(IntroductionActivity.class);
    }

    @Override
    public void startLoginActivity() {
        baseStartActivity(LoginActivity.class);
    }

    @Override
    public void startHomeActivity() {
        baseStartActivity(HomeActivity.class);
    }

    @Override
    public void setPresenter(LaunchContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void baseStartActivity(Class clazz) {
        finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    private Runnable delayedRunnable = new Runnable() {
        @Override
        public void run() {
            presenter.subscribe();
        }
    };
}
