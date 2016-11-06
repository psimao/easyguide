package com.easyguide.data.repository.preferences;

import android.support.annotation.NonNull;

import rx.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

public class PreferencesRepository implements PreferencesDataSource {

    private static PreferencesRepository INSTANCE = null;

    private final PreferencesLocalDataSource preferencesLocalDataSource;

    private PreferencesRepository(@NonNull PreferencesLocalDataSource preferencesLocalDataSource) {
        this.preferencesLocalDataSource = checkNotNull(preferencesLocalDataSource);
    }

    public static PreferencesRepository getInstance(@NonNull PreferencesLocalDataSource preferencesLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new PreferencesRepository(preferencesLocalDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<Boolean> isFirstAccess() {
        return preferencesLocalDataSource.isFirstAccess();
    }

    @Override
    public Observable<Void> setFirstAccess(Boolean firstAccess) {
        return preferencesLocalDataSource.setFirstAccess(firstAccess);
    }
}
