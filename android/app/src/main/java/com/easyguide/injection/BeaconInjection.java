package com.easyguide.injection;

import android.content.Context;
import android.support.annotation.NonNull;

import com.easyguide.util.beacon.ProximityBeaconManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class BeaconInjection {

    public static ProximityBeaconManager provideProximityBeaconManager(@NonNull Context context) {
        checkNotNull(context);
        return new ProximityBeaconManager(context);
    }

}
