package com.example.news_app.model.search;

public class Thumbnails {
    private ThumbnailUrl defaultUrl;
    private ThumbnailUrl medium;
    private ThumbnailUrl high;

    public ThumbnailUrl getDefaultUrl() {
        return defaultUrl;
    }

    public void setDefaultUrl(ThumbnailUrl defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    public ThumbnailUrl getMedium() {
        return medium;
    }

    public void setMedium(ThumbnailUrl medium) {
        this.medium = medium;
    }

    public ThumbnailUrl getHigh() {
        return high;
    }

    public void setHigh(ThumbnailUrl high) {
        this.high = high;
    }


}
