package com.easyguide.presentation.login;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.UserEntity;
import com.easyguide.data.repository.user.UserDataSource;
import com.easyguide.util.schedulers.SchedulerProvider;

import rx.Subscription;
import rx.functions.Action1;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoginPresenter implements LoginContract.Presenter {

    @NonNull
    private final SchedulerProvider schedulerProvider;

    @NonNull
    private final LoginContract.View view;

    @NonNull
    private final UserDataSource userRepository;

    private Subscription loginSubscription;

    public LoginPresenter(@NonNull LoginContract.View view, @NonNull UserDataSource userRepository, @NonNull SchedulerProvider schedulerProvider) {
        checkNotNull(view);
        checkNotNull(userRepository);
        checkNotNull(schedulerProvider);

        this.view = view;
        this.userRepository = userRepository;
        this.schedulerProvider = schedulerProvider;

        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if(loginSubscription != null && !loginSubscription.isUnsubscribed()) {
            loginSubscription.unsubscribe();
        }
    }

    @Override
    public void loginWithGoogleAccount() {
        if(loginSubscription == null || loginSubscription.isUnsubscribed()) {
            view.showDefaultProgress();
            loginSubscription =
                    userRepository.getUser()
                            .subscribeOn(schedulerProvider.computation())
                            .observeOn(schedulerProvider.ui())
                            .subscribe(getUserOnNextAction, getUserOnErrorAction);
        }
    }

    private Action1<UserEntity> getUserOnNextAction = new Action1<UserEntity>() {
        @Override
        public void call(UserEntity userEntity) {
            view.dismissProgress();
            if (userEntity != null) {
                view.startHomeActivity();
            } else {
                view.requestLoginWithGoogleAccount();
            }
            loginSubscription.unsubscribe();
        }
    };

    private Action1<Throwable> getUserOnErrorAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            view.showLoginErrorMessage(throwable.getMessage());
        }
    };
}
