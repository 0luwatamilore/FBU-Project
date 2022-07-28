package com.example.news_app.model.Parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("History")
public class History extends ParseObject {
    public static final String HISTORY_USER = "user";
    public static final String HISTORY_THUMBNAIL = "thumbnail";
    public static final String HISTORY_VIDEO_ID = "videoId";
    public static final String HISTORY_TITLE = "title";
    public static final String HISTORY_CREATED_AT = "createdAt";

    public History() {}

    public void setHistoryUser(ParseUser user) {
        put(HISTORY_USER, user);
    }

    public ParseUser getHistoryUser() {
        return getParseUser(HISTORY_USER);
    }

    public void setHistoryTitle(String title) {
        put(HISTORY_TITLE, title);
    }

    public String getHistoryTitle() {
        return getString(HISTORY_TITLE);
    }

    public void setHistoryVideoId(String videoId) {
        put(HISTORY_VIDEO_ID, videoId);
    }

    public String getHistoryVideoId() {
        return getString(HISTORY_VIDEO_ID);
    }

    public void setHistoryThumbnail(String thumbnail) {
        put(HISTORY_THUMBNAIL, thumbnail);
    }

    public String getHistoryThumbnail() {
        return getString(HISTORY_THUMBNAIL);
    }

}
