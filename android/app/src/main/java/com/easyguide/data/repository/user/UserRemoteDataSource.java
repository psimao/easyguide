package com.easyguide.data.repository.user;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.UserEntity;
import com.easyguide.data.entity.mapper.UserEntityMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Callable;

import rx.Observable;

public class UserRemoteDataSource implements UserDataSource {

    private final FirebaseAuth firebaseAuth;

    public UserRemoteDataSource(@NonNull FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public Observable<UserEntity> getUser() {
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
