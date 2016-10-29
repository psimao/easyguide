package com.easyguide.data.repository.user;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.UserEntity;

import rx.Observable;

public interface UserDataSource {

    Observable<UserEntity> getUser();

    Observable<Boolean> persistUser(@NonNull UserEntity user);
}
