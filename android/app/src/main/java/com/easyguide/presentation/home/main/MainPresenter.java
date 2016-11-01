package com.easyguide.presentation.home.main;

import android.support.annotation.NonNull;

import com.easyguide.data.repository.user.UserDataSource;
import com.easyguide.util.schedulers.BaseSchedulerProvider;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MainPresenter implements MainContract.Presenter {

    private final MainContract.View view;

    private final UserDataSource userRespository;

    private final BaseSchedulerProvider schedulerProvider;

    private final CompositeSubscription subscriptions;

    public MainPresenter(@NonNull MainContract.View view, @NonNull UserDataSource userRespository, @NonNull BaseSchedulerProvider schedulerProvider) {
        this.view = view;
        this.userRespository = userRespository;
        this.schedulerProvider = schedulerProvider;

        this.subscriptions = new CompositeSubscription();
        this.view.setPresenter(this);
    }

    @Override
    public void logout() {
        Subscription subscription = userRespository.signOut()
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(logoutOnNextAction);
        subscriptions.add(subscription);
    }

    @Override
    public void subscribe() {}

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }

    private Action1<Boolean> logoutOnNextAction = new Action1<Boolean>() {
        @Override
        public void call(Boolean aBoolean) {
            view.destroyAndStartLoginActivity();
        }
    };
}
