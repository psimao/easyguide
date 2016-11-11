package com.easyguide.data.repository.log;

import android.support.annotation.NonNull;

import com.easyguide.data.entity.Log;

import java.util.List;

import rx.Observable;

public interface LogDataSource {

    Observable<List<Log>> getLogs();

    Observable<Void> setLog(@NonNull Log log);
}
