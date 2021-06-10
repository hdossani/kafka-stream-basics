package com.example.serialization;

import com.google.gson.annotations.SerializedName;

public class Tweet {

    @SerializedName("CreatedAt")
    private Long createdAt;
    @SerializedName("Id")
    private Long id;
    @SerializedName("Retweet")
    private boolean retweet;
    @SerializedName("Text")
    private String text;
    @SerializedName("Lang")
    private String language;

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isRetweet() {
        return retweet;
    }

    public void setRetweet(boolean retweet) {
        this.retweet = retweet;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
