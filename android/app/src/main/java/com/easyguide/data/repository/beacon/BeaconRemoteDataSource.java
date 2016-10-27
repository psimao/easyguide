package com.easyguide.data.repository.beacon;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.UserEntity;
import com.easyguide.data.entity.mapper.UserEntityMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Callable;

import rx.Observable;

public class BeaconRemoteDataSource implements BeaconDataSource {

    private final FirebaseAuth firebaseAuth;

    public BeaconRemoteDataSource(@NonNull FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public Observable<UserEntity> getUser(@NonNull String username, @NonNull String password) {
        return Observable.fromCallable(new Callable<UserEntity>() {
            @Override
            public UserEntity call() throws Exception {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    return UserEntityMapper.transform(firebaseUser);
                } else {
                    return null;
                }
            }
        });
    }
}
