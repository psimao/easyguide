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
    public Observable<Boolean> getPreference(@NonNull String preference, @NonNull Boolean defautValue) {
        return preferencesLocalDataSource.getPreference(preference, defautValue);
    }

    @Override
    public Observable<Void> setPreference(@NonNull String preference, @NonNull Boolean value) {
        return preferencesLocalDataSource.setPreference(preference, value);
    }
}
