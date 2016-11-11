package com.easyguide.presentation.home.history;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.Log;
import com.easyguide.data.repository.log.LogDataSource;
import com.easyguide.util.schedulers.BaseSchedulerProvider;

import java.util.List;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class HistoryPresenter implements HistoryContract.Presenter {

    private final HistoryContract.View view;

    private final LogDataSource logRepository;

    private final BaseSchedulerProvider schedulerProvider;

    private final CompositeSubscription subscriptions;

    public HistoryPresenter(@NonNull HistoryContract.View view, @NonNull LogDataSource logRepository,
                            @NonNull BaseSchedulerProvider schedulerProvider) {
        checkNotNull(view);
        checkNotNull(logRepository);
        checkNotNull(schedulerProvider);
        this.view = view;
        this.logRepository = logRepository;
        this.schedulerProvider = schedulerProvider;
        this.subscriptions = new CompositeSubscription();
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadHistory();
    }

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }

    @Override
    public void loadHistory() {
        view.showLoadingView();
        Subscription subscription =
                logRepository.getLogs()
                        .observeOn(schedulerProvider.ui())
                        .subscribeOn(schedulerProvider.computation())
                        .subscribe(onHistoryLoaded);
        subscriptions.add(subscription);
    }

    private Action1<List<Log>> onHistoryLoaded = new Action1<List<Log>>() {
        @Override
        public void call(List<Log> logs) {
            view.setHistory(logs);
            view.hideLoadingView();
        }
    };
}
