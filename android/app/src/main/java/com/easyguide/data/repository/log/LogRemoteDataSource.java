package com.easyguide.data.repository.log;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

import static com.google.common.base.Preconditions.checkNotNull;

public class LogRemoteDataSource implements LogDataSource {

    private final FirebaseDatabase firebaseDatabase;

    private static final String FIREBASE_REFERENCE_LOG = "log";
    private static final String FIREBASE_REFERENCE_LOG_USER = "user";

    public LogRemoteDataSource(@NonNull FirebaseDatabase firebaseDatabase) {
        checkNotNull(firebaseDatabase);
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public Observable<List<Log>> getLogs() {
        return Observable.create(new Observable.OnSubscribe<List<Log>>() {
            @Override
            public void call(final Subscriber<? super List<Log>> subscriber) {
                final DatabaseReference userLogReference = firebaseDatabase.getReference(FIREBASE_REFERENCE_LOG).child(FIREBASE_REFERENCE_LOG_USER);
                final ValueEventListener listener = userLogReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> firebaseBeacons = dataSnapshot.getChildren();
                        List<Log> logs = new ArrayList<>();
                        for (DataSnapshot item : firebaseBeacons) {
                            Log log = item.getValue(Log.class);
                            logs.add(log);
                        }
                        subscriber.onNext(logs);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        subscriber.onError(new Throwable(error.getMessage()));
                    }
                });
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        userLogReference.removeEventListener(listener);
                    }
                }));
            }
        });
    }

    @Override
    public Observable<Void> setLog(@NonNull final Log log) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(final Subscriber<? super Void> subscriber) {
                final DatabaseReference userLogReference = firebaseDatabase.getReference(FIREBASE_REFERENCE_LOG).child(FIREBASE_REFERENCE_LOG_USER);
                userLogReference.push()
                        .setValue(log)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                subscriber.onNext(aVoid);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                subscriber.onError(e);
                            }
                        })
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                subscriber.unsubscribe();
                            }
                        });
            }
        });
    }
}
