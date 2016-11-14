package com.easyguide.presentation.home.profile;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.User;
import com.easyguide.data.repository.preferences.PreferencesDataSource;
import com.easyguide.data.repository.user.UserDataSource;
import com.easyguide.util.schedulers.BaseSchedulerProvider;

import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class ProfilePresenter implements ProfileContract.Presenter {

    private final ProfileContract.View view;

    private final UserDataSource userRepository;
    private final PreferencesDataSource preferencesRepository;

    private final BaseSchedulerProvider schedulerProvider;

    private final CompositeSubscription subscriptions;

    public ProfilePresenter(@NonNull ProfileContract.View view, @NonNull UserDataSource userRepository,
                            @NonNull PreferencesDataSource preferencesRepository, @NonNull BaseSchedulerProvider schedulerProvider) {
        checkNotNull(view);
        checkNotNull(userRepository);
        checkNotNull(preferencesRepository);
        checkNotNull(schedulerProvider);

        this.view = view;
        this.userRepository = userRepository;
        this.preferencesRepository = preferencesRepository;
        this.schedulerProvider = schedulerProvider;

        subscriptions = new CompositeSubscription();
        view.setPresenter(this);
    }

    @Override
    public void loadUser() {
        Subscription subscription = userRepository.getUser()
                .observeOn(schedulerProvider.ui())
                .subscribeOn(Schedulers.newThread())
                .subscribe(onUserLoadedAction);
        subscriptions.add(subscription);
    }

    @Override
    public void loadSettings() {
        Subscription subscription = preferencesRepository.getPreference(PreferencesDataSource.PREFERENCE_DISCOVERY_MODE, true)
                .observeOn(schedulerProvider.ui())
                .subscribeOn(Schedulers.newThread())
                .subscribe(onSettingsLoadedAction);
        subscriptions.add(subscription);
    }

    @Override
    public void updateDiscoveryMode(boolean status) {
        Subscription subscription = preferencesRepository.setPreference(PreferencesDataSource.PREFERENCE_DISCOVERY_MODE, status)
                .observeOn(schedulerProvider.ui())
                .subscribeOn(Schedulers.newThread())
                .subscribe();
        subscriptions.add(subscription);
    }

    @Override
    public void subscribe() {
        loadUser();
        loadSettings();
    }

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }

    private Action1<User> onUserLoadedAction = new Action1<User>() {
        @Override
        public void call(User user) {
            view.setUserPicture(user.getPhotoUrl());
            view.setUserName(user.getName());
            view.setUserProfile(user.getEmail());
        }
    };

    private Action1<Boolean> onSettingsLoadedAction = new Action1<Boolean>() {
        @Override
        public void call(Boolean status) {
            view.setDiscoveryModeStatus(status);
        }
    };
}
