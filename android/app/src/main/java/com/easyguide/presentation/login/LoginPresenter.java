package com.easyguide.presentation.login;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.User;
import com.easyguide.data.repository.user.UserDataSource;
import com.easyguide.util.schedulers.BaseSchedulerProvider;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoginPresenter implements LoginContract.Presenter {

    @NonNull
    private final BaseSchedulerProvider schedulerProvider;

    @NonNull
    private final LoginContract.View view;

    @NonNull
    private final UserDataSource userRepository;

    private final CompositeSubscription subscriptions;

    public LoginPresenter(@NonNull LoginContract.View view, @NonNull UserDataSource userRepository, @NonNull BaseSchedulerProvider schedulerProvider) {
        checkNotNull(view);
        checkNotNull(userRepository);
        checkNotNull(schedulerProvider);

        this.view = view;
        this.userRepository = userRepository;
        this.schedulerProvider = schedulerProvider;

        this.subscriptions = new CompositeSubscription();

        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        checkLoggedUser();
    }

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }

    @Override
    public void checkLoggedUser() {
        view.showDefaultProgress();
        Subscription loginSubscription =
                userRepository.getUser()
                        .subscribeOn(schedulerProvider.computation())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(getUserOnNextAction, defaultOnErrorAction);
        subscriptions.add(loginSubscription);
    }

    @Override
    public void login(User user) {
        view.showDefaultProgress();
        Subscription subscription =
                userRepository.persistUser(user)
                        .subscribeOn(schedulerProvider.computation())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(loginOnNextAction, defaultOnErrorAction);
        subscriptions.add(subscription);
    }

    private Action1<User> getUserOnNextAction = new Action1<User>() {
        @Override
        public void call(User user) {
            view.dismissProgress();
            if (user != null) {
                view.startHomeActivity();
            }
        }
    };

    private Action1<Boolean> loginOnNextAction = new Action1<Boolean>() {
        @Override
        public void call(Boolean result) {
            view.dismissProgress();
            if (result) {
                view.startHomeActivity();
            }
        }
    };

    private Action1<Throwable> defaultOnErrorAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            view.showLoginErrorMessage(throwable.getMessage());
        }
    };
}
