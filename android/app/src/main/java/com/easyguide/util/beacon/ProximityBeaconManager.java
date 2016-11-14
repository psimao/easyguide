package com.easyguide.util.beacon;

import com.easyguide.data.entity.Beacon;
import com.easyguide.data.entity.mapper.BeaconMapper;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

public class ProximityBeaconManager {

    private static final String REGION_ALL_BEACONS = "ALL_BEACONS";
    private static final String REGION_FIREBASE_BEACONS = "FIREBASE_BEACONS";

    private HashMap<String, Beacon> observableBeacons = new HashMap<>();
    private HashMap<String, Beacon> nearBeacons = new HashMap<>();

    private BeaconManager beaconManager;

    private static ProximityBeaconManager proximityBeaconManager;

    private ProximityBeaconManager(BeaconManager beaconManager) {
        this.beaconManager = beaconManager;
    }

    public static ProximityBeaconManager getInstance(BeaconManager beaconManager) {
        if (proximityBeaconManager == null) {
            proximityBeaconManager = new ProximityBeaconManager(beaconManager);
        }
        return proximityBeaconManager;
    }

    public Observable<List<Beacon>> observeNearestBeacons(List<Beacon> observableBeacons) {
        for (Beacon beacon : observableBeacons) {
            this.observableBeacons.put(beacon.toString(), beacon);
        }
        return Observable.create(new Observable.OnSubscribe<List<Beacon>>() {
            @Override
            public void call(Subscriber<? super List<Beacon>> subscriber) {
                setBeaconRangingListener(subscriber);
                startBeaconManager();
                subscriber.add(stopBeaconManager());
            }
        });
    }

    private void setBeaconRangingListener(final Subscriber<? super List<Beacon>> subscriber) {
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<com.estimote.sdk.Beacon> list) {
                for (com.estimote.sdk.Beacon foundBeacon : list) {
                    Beacon convertedBeacon = BeaconMapper.transform(foundBeacon);
                    String key = convertedBeacon.toString();
                    convertedBeacon.setContent(observableBeacons.get(key).getContent());
                    nearBeacons.put(key, convertedBeacon);
                }

                List<Beacon> nearBeaconsList = new ArrayList<>(nearBeacons.values());
                Collections.sort(nearBeaconsList, new Comparator<Beacon>() {
                    @Override
                    public int compare(Beacon beacon1, Beacon beacon2) {
                        return beacon1.getDistance().compareTo(beacon2.getDistance());
                    }
                });
                subscriber.onNext(nearBeaconsList);
            }
        });
    }

    private void startBeaconManager() {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                if (observableBeacons != null) {
                    for (Map.Entry<String, Beacon> beaconEntry : observableBeacons.entrySet()) {
                        Beacon beacon = beaconEntry.getValue();
                        beaconManager.startRanging(
                                new Region(
                                        REGION_FIREBASE_BEACONS,
                                        UUID.fromString(beacon.getUuid()),
                                        beacon.getMajor().intValue(),
                                        beacon.getMinor().intValue()
                                )
                        );
                    }
                } else {
                    beaconManager.startRanging(new Region(REGION_ALL_BEACONS, null, null, null));
                }
            }
        });
    }

    private Subscription stopBeaconManager() {
        return Subscriptions.create(new Action0() {
            @Override
            public void call() {
                beaconManager.stopRanging(new Region(REGION_ALL_BEACONS, null, null, null));
                beaconManager.stopRanging(new Region(REGION_FIREBASE_BEACONS, null, null, null));
            }
        });
    }

}
