package com.easyguide.presentation.launch;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.User;
import com.easyguide.data.repository.preferences.PreferencesDataSource;
import com.easyguide.data.repository.user.UserDataSource;
import com.easyguide.util.schedulers.BaseSchedulerProvider;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class LaunchPresenter implements LaunchContract.Presenter {

    private final LaunchContract.View view;

    private final PreferencesDataSource preferencesRepository;
    private final UserDataSource userRepository;

    private final BaseSchedulerProvider schedulerProvider;

    private final CompositeSubscription subscriptions;

    public LaunchPresenter(@NonNull LaunchContract.View view, @NonNull PreferencesDataSource preferencesRepository,
                           @NonNull UserDataSource userRepository, @NonNull BaseSchedulerProvider schedulerProvider) {
        checkNotNull(view);
        checkNotNull(preferencesRepository);
        checkNotNull(userRepository);
        checkNotNull(schedulerProvider);

        this.view = view;
        this.preferencesRepository = preferencesRepository;
        this.userRepository = userRepository;
        this.schedulerProvider = schedulerProvider;

        this.subscriptions = new CompositeSubscription();

        view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        Subscription preferencesSubscription = preferencesRepository.getPreference(PreferencesDataSource.PREFERENCE_FIRST_ACCESS, true)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(firstAccessCheckOnNextAction);
        subscriptions.add(preferencesSubscription);
    }

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }

    private Action1<Boolean> firstAccessCheckOnNextAction = new Action1<Boolean>() {
        @Override
        public void call(Boolean firstAccess) {
            if (firstAccess) {
                view.startIntroductionActivity();
            } else {
                subscriptions.clear();
                Subscription userSubscription = userRepository.getUser()
                        .subscribeOn(schedulerProvider.computation())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(userLoggedCheckOnNextAction);
                subscriptions.add(userSubscription);
            }
        }
    };

    private Action1<User> userLoggedCheckOnNextAction = new Action1<User>() {
        @Override
        public void call(User user) {
            if (user != null) {
                view.startHomeActivity();
            } else {
                view.startLoginActivity();
            }
        }
    };
}
