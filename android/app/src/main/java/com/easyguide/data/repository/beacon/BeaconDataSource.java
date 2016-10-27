package com.easyguide.data.repository.beacon;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.UserEntity;

import rx.Observable;

public interface BeaconDataSource {

    Observable<UserEntity> getUser(@NonNull String username, @NonNull String password);

}
