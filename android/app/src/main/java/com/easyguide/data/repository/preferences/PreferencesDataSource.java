package com.easyguide.data.repository.preferences;

import android.support.annotation.NonNull;

import rx.Observable;

public interface PreferencesDataSource {

    String PREFERENCE_FIRST_ACCESS = "first_access";
    String PREFERENCE_DISCOVERY_MODE = "discovery_mode";

    Observable<Boolean> getPreference(@NonNull String preference, @NonNull Boolean defautValue);

    Observable<Void> setPreference(@NonNull String preference, @NonNull Boolean value);

}
