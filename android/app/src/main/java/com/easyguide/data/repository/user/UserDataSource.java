package com.easyguide.data.repository.user;

import com.easyguide.data.entity.UserEntity;

import rx.Observable;

public interface UserDataSource {

    Observable<UserEntity> getUser();

}
