package com.easyguide.util;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.Log;
import com.easyguide.data.repository.log.LogRemoteDataSource;
import com.google.firebase.database.FirebaseDatabase;

import rx.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

public class Logger {

    private static final LogRemoteDataSource remoteLogDataSource = new LogRemoteDataSource(FirebaseDatabase.getInstance());

    public static void registerLog(@NonNull Log log) {
        checkNotNull(log);
        remoteLogDataSource.setLog(log).subscribeOn(Schedulers.newThread()).subscribe();
    }

    public static void createAndRegisterLog(@NonNull String title, @NonNull String description, @NonNull String user) {
        checkNotNull(title);
        checkNotNull(description);
        checkNotNull(user);

        Log log = new Log();
        log.setTitle(title);
        log.setDescription(description);
        log.setUser(user);
        log.setTimestamp(System.currentTimeMillis());

        remoteLogDataSource.setLog(log).subscribeOn(Schedulers.newThread()).subscribe();
    }

}
