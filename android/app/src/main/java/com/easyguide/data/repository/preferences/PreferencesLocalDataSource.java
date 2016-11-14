package com.easyguide.data.repository.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.concurrent.Callable;

import rx.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

public class PreferencesLocalDataSource implements PreferencesDataSource {

    private static final String SHAREDPREFERENCES_NAME_PREFERENCES = "sharedpreferences_preferences";

    private final SharedPreferences sharedPreferences;

    public PreferencesLocalDataSource(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCES_NAME_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public Observable<Boolean> getPreference(@NonNull final String preference, @NonNull final Boolean defautValue) {
        checkNotNull(preference);
        checkNotNull(defautValue);
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return sharedPreferences.getBoolean(preference, defautValue);
            }
        });
    }

    @Override
    public Observable<Void> setPreference(@NonNull final String preference, @NonNull final Boolean value) {
        checkNotNull(preference);
        checkNotNull(value);
        return Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(preference, value);
                editor.apply();
                return null;
            }
        });
    }
}
