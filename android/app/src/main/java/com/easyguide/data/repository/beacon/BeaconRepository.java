package com.easyguide.data.repository.beacon;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.Beacon;

import java.util.List;

import rx.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

public class BeaconRepository implements BeaconDataSource {

    private static BeaconRepository INSTANCE = null;

    private final BeaconRemoteDataSource beaconRemoteDataSource;

    private BeaconRepository(@NonNull BeaconRemoteDataSource beaconRemoteDataSource) {
        this.beaconRemoteDataSource = checkNotNull(beaconRemoteDataSource);
    }

    public static BeaconRepository getInstance(@NonNull BeaconRemoteDataSource beaconRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new BeaconRepository(beaconRemoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Beacon>> getBeacons() {
        return beaconRemoteDataSource.getBeacons();
    }
}
