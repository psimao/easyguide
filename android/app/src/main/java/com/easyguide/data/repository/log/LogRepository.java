package com.easyguide.data.repository.log;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.Log;

import java.util.List;

import rx.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

public class LogRepository implements LogDataSource {

    private static LogRepository INSTANCE = null;

    private final LogRemoteDataSource logRemoteDataSource;

    private LogRepository(@NonNull LogRemoteDataSource logRemoteDataSource) {
        this.logRemoteDataSource = checkNotNull(logRemoteDataSource);
    }

    public static LogRepository getInstance(@NonNull LogRemoteDataSource logRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new LogRepository(logRemoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Log>> getLogs() {
        return logRemoteDataSource.getLogs();
    }

    @Override
    public Observable<Void> setLog(@NonNull Log log) {
        return logRemoteDataSource.setLog(log);
    }
}
