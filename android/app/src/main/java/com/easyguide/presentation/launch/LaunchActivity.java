package com.easyguide.presentation.launch;

import android.content.Intent;
import android.os.Bundle;

import com.easyguide.BaseActivity;
import com.easyguide.Injection;
import com.easyguide.R;
import com.easyguide.presentation.home.HomeActivity;
import com.easyguide.presentation.introduction.IntroductionActivity;
import com.easyguide.presentation.login.LoginActivity;

public class LaunchActivity extends BaseActivity implements LaunchContract.View {

    private LaunchContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        new LaunchPresenter(
                this,
                Injection.providePreferencesRepository(this),
                Injection.provideUserRepository(),
                Injection.provideSchedulerProvider()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
        startActivity(new Intent(this, clazz));
    }
}
