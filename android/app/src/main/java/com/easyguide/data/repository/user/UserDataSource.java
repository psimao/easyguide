package com.easyguide.data.repository.user;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.User;

import rx.Observable;

public interface UserDataSource {

    Observable<User> getUser();

    Observable<Boolean> persistUser(@NonNull User user);

    Observable<Boolean> signOut();
}
