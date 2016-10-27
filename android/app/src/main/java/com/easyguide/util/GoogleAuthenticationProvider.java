package com.easyguide.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.google.common.base.Preconditions.checkNotNull;

public class GoogleAuthenticationProvider {

    private static GoogleApiClient googleApiClientInstance;
    private static GoogleSignInOptions googleSignInOptions;

    public static GoogleSignInOptions provideGoogleSignInOptions(@NonNull String token) {
        checkNotNull(token);
        if (googleSignInOptions == null) {
            googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(token)
                    .requestEmail()
                    .build();

        }
        return googleSignInOptions;

    }

    public static GoogleApiClient provideGoogleApiClient(@NonNull FragmentActivity fragmentActivity,
                                                         @NonNull GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener,
                                                         @NonNull String optionsToken) {
        checkNotNull(fragmentActivity);
        checkNotNull(onConnectionFailedListener);
        checkNotNull(optionsToken);
        return provideGoogleApiClient(fragmentActivity, onConnectionFailedListener, provideGoogleSignInOptions(optionsToken));
    }

    public static GoogleApiClient provideGoogleApiClient(FragmentActivity fragmentActivity,
                                                         GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener,
                                                         GoogleSignInOptions googleSignInOptions) {
        checkNotNull(fragmentActivity);
        checkNotNull(onConnectionFailedListener);
        checkNotNull(googleSignInOptions);
        if (googleApiClientInstance == null) {
            Context context = fragmentActivity.getApplicationContext();
            googleApiClientInstance = new GoogleApiClient.Builder(context)
                    .enableAutoManage(fragmentActivity, onConnectionFailedListener)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                    .build();
        }
        return googleApiClientInstance;
    }
}
