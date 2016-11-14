package com.easyguide.presentation.home.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easyguide.BaseFragment;
import com.easyguide.R;
import com.easyguide.injection.RepositoryInjection;
import com.easyguide.injection.SchedulerProviderInjection;
import com.easyguide.ui.customview.CircularImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class ProfileFragment extends BaseFragment implements ProfileContract.View {

    @BindView(R.id.civ_profile)
    CircularImageView civProfile;

    @BindView(R.id.textview_user_name)
    TextView textViewUserName;
    @BindView(R.id.textview_user_profile)
    TextView textViewUserProfile;

    @BindView(R.id.switch_discovery_mode)
    SwitchCompat switchDiscoveryMode;

    private ProfileContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        new ProfilePresenter(
                this,
                RepositoryInjection.provideUserRepository(getContext()),
                RepositoryInjection.providePreferencesRepository(getContext()),
                SchedulerProviderInjection.provideSchedulerProvider()
        );
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.subscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    protected int fragmentTitleResourceId() {
        return R.string.profile_title;
    }

    @Override
    public void setUserPicture(String pictureUrl) {
        Picasso.with(getContext())
                .load(pictureUrl)
                .error(R.drawable.ic_person_white_48dp)
                .into(civProfile);
    }

    @Override
    public void setUserName(String name) {
        textViewUserName.setText(name);
    }

    @Override
    public void setUserProfile(String profile) {
        textViewUserProfile.setText(profile);
    }

    @Override
    public void setDiscoveryModeStatus(boolean status) {
        switchDiscoveryMode.setChecked(status);
    }

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @OnCheckedChanged(R.id.switch_discovery_mode)
    public void onSwitchDiscoveryModeCheckedChanged(boolean checked) {
        presenter.updateDiscoveryMode(checked);
    }
}
