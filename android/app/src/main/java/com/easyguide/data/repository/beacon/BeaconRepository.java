package com.easyguide.data.repository.beacon;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.UserEntity;

import rx.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

public class BeaconRepository implements BeaconDataSource {

    private static BeaconRepository INSTANCE = null;

    private final BeaconRemoteDataSource userRemoteDataSource;

    private BeaconRepository(@NonNull BeaconRemoteDataSource userRemoteDataSource) {
        this.userRemoteDataSource = checkNotNull(userRemoteDataSource);
    }

    public static BeaconRepository getInstance(@NonNull BeaconRemoteDataSource userRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new BeaconRepository(userRemoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<UserEntity> getUser(@NonNull String username, @NonNull String password) {
        return userRemoteDataSource.getUser(username, password);
    }
}
