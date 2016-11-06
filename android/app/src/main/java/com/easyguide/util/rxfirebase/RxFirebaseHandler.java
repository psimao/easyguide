package com.easyguide.util.rxfirebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import rx.Subscriber;

public class RxFirebaseHandler <T> implements OnSuccessListener<T>, OnFailureListener, OnCompleteListener<T> {

    private final Subscriber<? super T> subscriber;

    private RxFirebaseHandler(Subscriber<? super T> observer) {
        this.subscriber = observer;
    }

    public static <T> void assignOnTask(Subscriber<? super T> observer, Task<T> task) {
        RxFirebaseHandler<T> handler = new RxFirebaseHandler<T>(observer);
        task.addOnSuccessListener(handler);
        task.addOnFailureListener(handler);
        try {
            task.addOnCompleteListener(handler);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void onSuccess(T res) {
        if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(res);
        }
    }

    @Override
    public void onComplete(@NonNull Task<T> task) {
        if (!subscriber.isUnsubscribed()) {
            subscriber.onCompleted();
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        if (!subscriber.isUnsubscribed()) {
            subscriber.onError(e);
        }
    }
}
