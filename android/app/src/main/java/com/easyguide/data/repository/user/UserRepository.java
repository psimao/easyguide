package com.easyguide.data.repository.user;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.UserEntity;

import rx.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserRepository implements UserDataSource {

    private static UserRepository INSTANCE = null;

    private final UserRemoteDataSource userRemoteDataSource;

    private UserRepository(@NonNull UserRemoteDataSource userRemoteDataSource) {
        this.userRemoteDataSource = checkNotNull(userRemoteDataSource);
    }

    public static UserRepository getInstance(@NonNull UserRemoteDataSource userRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new UserRepository(userRemoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<UserEntity> getUser() {
        return userRemoteDataSource.getUser();
    }

    @Override
    public Observable<Boolean> persistUser(@NonNull UserEntity user) {
        return userRemoteDataSource.persistUser(user);
    }
}
