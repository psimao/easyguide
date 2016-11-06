package com.easyguide.data.entity.mapper;

import com.easyguide.data.entity.Beacon;
import com.estimote.sdk.Utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class BeaconMapper {

    private static final DecimalFormat df = new DecimalFormat("#.##");

    public static Beacon transform(com.estimote.sdk.Beacon estimoteBeacon) {
        Beacon beacon = new Beacon();
        beacon.setUuid(estimoteBeacon.getProximityUUID().toString());
        beacon.setMajor((long) estimoteBeacon.getMajor());
        beacon.setMinor((long) estimoteBeacon.getMinor());
        Double distance = Utils.computeAccuracy(estimoteBeacon);
        df.setRoundingMode(RoundingMode.CEILING);
        beacon.setDistance(Double.valueOf(df.format(distance).replace(",", ".")));
        return beacon;
    }

}
