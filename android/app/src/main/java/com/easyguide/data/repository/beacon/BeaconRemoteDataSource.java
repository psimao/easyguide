package com.easyguide.data.repository.beacon;

import android.support.annotation.NonNull;
import android.util.Log;

import com.easyguide.data.entity.Beacon;
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

public class BeaconRemoteDataSource implements BeaconDataSource {

    private final FirebaseDatabase firebaseDatabase;

    private static final String FIREBASE_REFERENCE_BEACONS = "beacon";

    public BeaconRemoteDataSource(@NonNull FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public Observable<List<Beacon>> getBeacons() {
        return Observable.create(new Observable.OnSubscribe<List<Beacon>>() {
            @Override
            public void call(final Subscriber<? super List<Beacon>> subscriber) {
                final DatabaseReference beaconReference = firebaseDatabase.getReference(FIREBASE_REFERENCE_BEACONS);
                final ValueEventListener listener = beaconReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> firebaseBeacons = dataSnapshot.getChildren();
                        List<Beacon> beacons = new ArrayList<>();
                        for (DataSnapshot item : firebaseBeacons) {
                            Beacon beacon = item.getValue(Beacon.class);
                            beacons.add(beacon);
                        }
                        Log.d("FB next", beacons.toString());
                        subscriber.onNext(beacons);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        subscriber.onError(new Throwable(error.getMessage()));
                    }
                });
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        beaconReference.removeEventListener(listener);
                    }
                }));
            }
        });
    }
}
