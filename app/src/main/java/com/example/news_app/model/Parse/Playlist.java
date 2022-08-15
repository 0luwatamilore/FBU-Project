package com.example.news_app.model.Parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Playlist")
public class Playlist extends ParseObject {
    public static final String PLAYLIST_USER = "user";
    public static final String PLAYLIST_NAME = "playlist_name";
    public static final String PLAYLIST_THUMBNAIL = "thumbnail";
    public static final String PLAYLIST_TITLE = "title";
    public static final String PLAYLIST_VIDEO = "videoId";
    public static final String CREATED_KEY = "createdAt";

    public Playlist() {}

    public ParseUser getUser() {
        return getParseUser(PLAYLIST_USER);
    }

    public void setPlaylistUser(ParseUser user) {
        put(PLAYLIST_USER, user);
    }

    public String getPlaylistName() {
        return getString(PLAYLIST_NAME);
    }

    public void setPlaylistName(String playlist_name) {
        put(PLAYLIST_NAME, playlist_name);
    }

    public String getPlaylistThumbnail() {
        return getString(PLAYLIST_THUMBNAIL);
    }

    public void setPlaylistThumbnail(String playlist_thumbnail) {
        put(PLAYLIST_THUMBNAIL, playlist_thumbnail);
    }

    public String getPlaylistTitle() {
        return getString(PLAYLIST_TITLE);
    }

    public void setPlaylistTitle(String playlist_title) {
        put(PLAYLIST_TITLE, playlist_title);
    }

    public String getPlaylistVideo() {
        return getString(PLAYLIST_VIDEO);
    }

    public void setPlaylistVideo(String playlist_video) {
        put(PLAYLIST_VIDEO, playlist_video);
    }

}
