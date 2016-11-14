package com.easyguide.presentation.introduction;

import android.support.annotation.NonNull;

import com.easyguide.data.repository.preferences.PreferencesDataSource;
import com.easyguide.util.schedulers.BaseSchedulerProvider;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class IntroductionPresenter implements IntroductionContract.Presenter {

    private final IntroductionContract.View view;
    private final PreferencesDataSource preferencesRepository;
    private final BaseSchedulerProvider schedulerProvider;

    private final CompositeSubscription subscriptions;

    public IntroductionPresenter (@NonNull IntroductionContract.View view, @NonNull PreferencesDataSource preferencesRepository,
                                  @NonNull BaseSchedulerProvider schedulerProvider) {
        checkNotNull(view);
        checkNotNull(preferencesRepository);
        checkNotNull(schedulerProvider);

        this.view = view;
        this.preferencesRepository = preferencesRepository;
        this.schedulerProvider = schedulerProvider;

        this.subscriptions = new CompositeSubscription();

        this.view.setPresenter(this);
    }

    @Override
    public void setupFirstAccess() {
        view.showDefaultProgress();
        Subscription subscription =
                preferencesRepository.setPreference(PreferencesDataSource.PREFERENCE_FIRST_ACCESS, false)
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.computation())
                .subscribe(setupFirstAccessAction);
        subscriptions.add(subscription);
    }

    @Override
    public void subscribe() {}

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }

    private Action1<Void> setupFirstAccessAction = new Action1<Void>() {
        @Override
        public void call(Void aVoid) {
            view.dismissProgress();
            view.startLoginActivity();
        }
    };
}
