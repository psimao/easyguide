package com.easyguide.data.repository.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.concurrent.Callable;

import rx.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

public class PreferencesLocalDataSource implements PreferencesDataSource {

    private static final String SHAREDPREFERENCES_NAME_PREFERENCES = "sharedpreferences_preferences";

    private static final String SHAREDPREFERENCES_VALUE_FIRST_ACCESS = "first_access";

    private final SharedPreferences sharedPreferences;

    public PreferencesLocalDataSource(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCES_NAME_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public Observable<Boolean> isFirstAccess() {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return sharedPreferences.getBoolean(SHAREDPREFERENCES_VALUE_FIRST_ACCESS, true);
            }
        });
    }

    @Override
    public Observable<Void> setFirstAccess(@NonNull final Boolean firstAccess) {
        checkNotNull(firstAccess);
        return Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SHAREDPREFERENCES_VALUE_FIRST_ACCESS, firstAccess);
                editor.apply();
                return null;
            }
        });
    }
}
