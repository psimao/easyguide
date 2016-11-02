package com.easyguide.data.repository.user;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.User;
import com.easyguide.data.entity.mapper.UserEntityMapper;
import com.easyguide.util.rxfirebase.RxFirebaseHandler;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserRemoteDataSource implements UserDataSource {

    private final FirebaseAuth firebaseAuth;
    private final GoogleApiClient googleApiClient;

    public UserRemoteDataSource(@NonNull FirebaseAuth firebaseAuth, @NonNull GoogleApiClient googleApiClient) {
        this.firebaseAuth = firebaseAuth;
        this.googleApiClient = googleApiClient;
    }

    @Override
    public Observable<User> getUser() {
        return Observable.fromCallable(new Callable<User>() {
            @Override
            public User call() throws Exception {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    return UserEntityMapper.transform(firebaseUser);
                } else {
                    return null;
                }
            }
        });
    }

    @Override
    public Observable<Boolean> persistUser(@NonNull User user) {
        checkNotNull(user);
        final AuthCredential credential = GoogleAuthProvider.getCredential(user.getIdToken(), null);
        return Observable.create(new Observable.OnSubscribe<AuthResult>() {
            @Override
            public void call(final Subscriber<? super AuthResult> subscriber) {
                RxFirebaseHandler.assignOnTask(subscriber, firebaseAuth.signInWithCredential(credential));
            }
        }).map(new Func1<AuthResult, Boolean>() {
            @Override
            public Boolean call(AuthResult authResult) {
                return authResult.getUser() != null;
            }
        });
    }

    @Override
    public Observable<Boolean> signOut() {

        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                FirebaseAuth.getInstance().signOut();
                googleApiClient.blockingConnect();
                Status status = Auth.GoogleSignInApi.signOut(googleApiClient).await();
                return status.isSuccess();
            }
        });
    }
}
