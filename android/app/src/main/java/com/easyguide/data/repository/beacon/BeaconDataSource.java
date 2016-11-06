package com.easyguide.data.repository.beacon;

import com.easyguide.data.entity.Beacon;

import java.util.List;

import rx.Observable;

public interface BeaconDataSource {

    Observable<List<Beacon>> getBeacons();

}
