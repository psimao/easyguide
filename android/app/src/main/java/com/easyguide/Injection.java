package com.easyguide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.easyguide.data.repository.beacon.BeaconRemoteDataSource;
import com.easyguide.data.repository.beacon.BeaconRepository;
import com.easyguide.data.repository.preferences.PreferencesLocalDataSource;
import com.easyguide.data.repository.preferences.PreferencesRepository;
import com.easyguide.data.repository.user.UserRemoteDataSource;
import com.easyguide.data.repository.user.UserRepository;
import com.easyguide.util.GoogleAuthenticationProvider;
import com.easyguide.util.beacon.ProximityBeaconManager;
import com.easyguide.util.schedulers.SchedulerProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injection {

    public static UserRepository provideUserRepository(@NonNull Context context) {
        checkNotNull(context);
        return UserRepository.getInstance(
                new UserRemoteDataSource(
                        FirebaseAuth.getInstance(),
                        GoogleAuthenticationProvider.provideGoogleApiClient(context, context.getString(R.string.default_web_client_id))
                )
        );
    }

    public static BeaconRepository provideBeaconRepository() {
        return BeaconRepository.getInstance(new BeaconRemoteDataSource(FirebaseDatabase.getInstance()));
    }

    public static PreferencesRepository providePreferencesRepository(@NonNull Context context) {
        checkNotNull(context);
        return PreferencesRepository.getInstance(new PreferencesLocalDataSource(context));
    }

    public static ProximityBeaconManager provideProximityBeaconManager(@NonNull Context context) {
        checkNotNull(context);
        return new ProximityBeaconManager(context);
    }

    public static SchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

}
