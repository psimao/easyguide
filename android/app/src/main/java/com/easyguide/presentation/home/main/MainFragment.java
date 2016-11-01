package com.easyguide.presentation.home.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.easyguide.BaseFragment;
import com.easyguide.Injection;
import com.easyguide.R;
import com.easyguide.presentation.login.LoginActivity;

public class MainFragment extends BaseFragment implements MainContract.View {

    private MainContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        setHasOptionsMenu(true);
        new MainPresenter(
                this,
                Injection.provideUserRepository(getActivity().getApplicationContext()),
                Injection.provideSchedulerProvider()
        );
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    protected int fragmentTitleResourceId() {
        return R.string.main_title;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                presenter.logout();
                break;
        }
        return true;
    }

    @Override
    public void destroyAndStartLoginActivity() {
        getActivity().finish();
        startActivity(new Intent(getContext(), LoginActivity.class));
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
