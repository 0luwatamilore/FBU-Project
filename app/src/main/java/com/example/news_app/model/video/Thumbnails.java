package com.example.news_app.model.video;

public class Thumbnails {
    private ThumbnailsUrl defaultInfo;
    private ThumbnailsUrl medium;
    private ThumbnailsUrl high;

    public ThumbnailsUrl getDefaultInfo() {
        return defaultInfo;
    }

    public void setDefaultInfo(ThumbnailsUrl defaultInfo) {
        this.defaultInfo = defaultInfo;
    }

    public ThumbnailsUrl getMedium() {
        return medium;
    }

    public void setMedium(ThumbnailsUrl medium) {
        this.medium = medium;
    }

    public ThumbnailsUrl getHigh() {
        return high;
    }

    public void setHigh(ThumbnailsUrl high) {
        this.high = high;
    }
}
