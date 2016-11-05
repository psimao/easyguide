package com.easyguide.data.entity;

import com.google.firebase.database.PropertyName;

import java.util.HashMap;

public class Beacon {

    @PropertyName("uuid")
    String uuid;
    @PropertyName("major_value")
    Long major;
    @PropertyName("minor_value")
    Long minor;
    Double distance;
    @PropertyName("content")
    HashMap<String, BeaconContent> content;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getMajor() {
        return major;
    }

    public void setMajor(Long major) {
        this.major = major;
    }

    public Long getMinor() {
        return minor;
    }

    public void setMinor(Long minor) {
        this.minor = minor;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public HashMap<String, BeaconContent> getContent() {
        return content;
    }

    public void setContent(HashMap<String, BeaconContent> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return getUuid().toUpperCase() + ":" + getMajor() + ":" + getMinor();
    }
}
