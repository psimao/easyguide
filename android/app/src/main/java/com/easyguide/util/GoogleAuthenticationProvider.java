package com.easyguide.util;

import android.content.Context;
import android.support.annotation.NonNull;

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

    public static GoogleApiClient provideGoogleApiClient(@NonNull Context context, @NonNull String optionsToken) {
        checkNotNull(context);
        checkNotNull(optionsToken);
        return provideGoogleApiClient(context, provideGoogleSignInOptions(optionsToken));
    }

    public static GoogleApiClient provideGoogleApiClient(@NonNull Context context, @NonNull GoogleSignInOptions googleSignInOptions) {
        checkNotNull(context);
        checkNotNull(googleSignInOptions);
        if (googleApiClientInstance == null) {
            googleApiClientInstance = new GoogleApiClient.Builder(context)
                    //.enableAutoManage(fragmentActivity, onConnectionFailedListener)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                    .build();
        }
        return googleApiClientInstance;
    }
}
