package com.easyguide.data.repository.preferences;

import android.support.annotation.NonNull;

import rx.Observable;

public interface PreferencesDataSource {

    Observable<Boolean> isFirstAccess();

    Observable<Void> setFirstAccess(@NonNull Boolean firstAccess);

}
