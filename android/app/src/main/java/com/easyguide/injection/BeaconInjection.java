package com.easyguide.injection;

import android.content.Context;
import android.support.annotation.NonNull;

import com.easyguide.util.beacon.ProximityBeaconManager;
import com.estimote.sdk.BeaconManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class BeaconInjection {

    private static BeaconManager beaconManager;

    public static ProximityBeaconManager provideProximityBeaconManager(@NonNull BeaconManager beaconManager) {
        checkNotNull(beaconManager);
        return ProximityBeaconManager.getInstance(beaconManager);
    }

    public static BeaconManager provideBeaconManager(@NonNull Context context) {
        checkNotNull(context);
        if (beaconManager == null) {
            beaconManager = new BeaconManager(context);
        }
        return beaconManager;
    }

}
