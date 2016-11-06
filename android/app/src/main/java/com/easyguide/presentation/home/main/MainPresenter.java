package com.easyguide.presentation.home.main;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.Beacon;
import com.easyguide.data.repository.beacon.BeaconDataSource;
import com.easyguide.data.repository.user.UserDataSource;
import com.easyguide.util.beacon.ProximityBeaconManager;
import com.easyguide.util.schedulers.BaseSchedulerProvider;

import java.util.List;

import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainPresenter implements MainContract.Presenter {

    private final MainContract.View view;

    private final UserDataSource userRespository;
    private final BeaconDataSource beaconRepository;

    private final ProximityBeaconManager beaconManager;

    private final BaseSchedulerProvider schedulerProvider;

    private final CompositeSubscription subscriptions;
    private Subscription beaconManagerSubscription;

    public MainPresenter(@NonNull MainContract.View view,
                         @NonNull UserDataSource userRespository,
                         @NonNull BeaconDataSource beaconRepository,
                         @NonNull ProximityBeaconManager beaconManager,
                         @NonNull BaseSchedulerProvider schedulerProvider) {
        checkNotNull(view);
        checkNotNull(userRespository);
        checkNotNull(beaconRepository);
        checkNotNull(beaconManager);
        checkNotNull(schedulerProvider);

        this.view = view;
        this.userRespository = userRespository;
        this.beaconRepository = beaconRepository;
        this.beaconManager = beaconManager;
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
    public void subscribe() {
        view.showLoadingView();
        Subscription subscription = beaconRepository.getBeacons()
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(loadBeaconsOnNextAction);
        subscriptions.add(subscription);
    }

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

    private Action1<List<Beacon>> loadBeaconsOnNextAction = new Action1<List<Beacon>>() {
        @Override
        public void call(List<Beacon> beacons) {
            if (beaconManagerSubscription != null && !beaconManagerSubscription.isUnsubscribed()) {
                subscriptions.remove(beaconManagerSubscription);
            }
            beaconManagerSubscription = beaconManager.observeNearestBeacons(beacons)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(observeNearestBeaconsOnNextAction);
            subscriptions.add(beaconManagerSubscription);
        }
    };

    private Action1<List<Beacon>> observeNearestBeaconsOnNextAction = new Action1<List<Beacon>>() {
        @Override
        public void call(List<Beacon> beacons) {
            view.hideLoadingView();
            view.setBeacon(beacons);
        }
    };
}
