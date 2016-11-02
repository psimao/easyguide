package com.easyguide.data.entity;

import com.google.firebase.database.PropertyName;

import java.util.List;

public class Beacon {

    @PropertyName("uuid")
    String uuid;
    @PropertyName("major_value")
    String major;
    @PropertyName("minor_value")
    String minor;
    @PropertyName("content")
    List<BeaconContent> content;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public List<BeaconContent> getContent() {
        return content;
    }

    public void setContent(List<BeaconContent> content) {
        this.content = content;
    }
}
