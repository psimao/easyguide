package com.easyguide.data.entity;

import com.google.firebase.database.PropertyName;

public class BeaconContent {

    @PropertyName("profile")
    Long profile;
    @PropertyName("image_url")
    String imageUrl;
    @PropertyName("speech_content")
    String speechContent;
    @PropertyName("speech_description")
    String speechDescription;
    @PropertyName("text_content")
    String textContent;
    @PropertyName("text_description")
    String textDescription;

    public Long getProfile() {
        return profile;
    }

    public void setProfile(Long profile) {
        this.profile = profile;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSpeechContent() {
        return speechContent;
    }

    public void setSpeechContent(String speechContent) {
        this.speechContent = speechContent;
    }

    public String getSpeechDescription() {
        return speechDescription;
    }

    public void setSpeechDescription(String speechDescription) {
        this.speechDescription = speechDescription;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getTextDescription() {
        return textDescription;
    }

    public void setTextDescription(String textDescription) {
        this.textDescription = textDescription;
    }
}
