package com.example.news_app.model.Parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Playlist")
public class Playlist extends ParseObject {
    public static final String PLAYLIST_USER = "user";
    public static final String PLAYLIST_Name = "playlist_name";
    public static final String PLAYLIST_ITEMS = "playlist_items";

    public ParseUser getUser() {
        return getParseUser(PLAYLIST_USER);
    }

    public void setUser(ParseUser user) {
        put(PLAYLIST_USER, user);
    }

    public String getPLAYLIST_Name() {
        return getString(PLAYLIST_Name);
    }

    public void setPLAYLIST_Name(String playlist_name) {
        put(PLAYLIST_Name, playlist_name);
    }

    public List<String> getPlaylistItems() {
        List<String> playlist_items = getList(PLAYLIST_ITEMS);
        if (playlist_items != null) {
            return getList(PLAYLIST_ITEMS);
        } else {
            return new ArrayList<>();
        }
    }

    public void setPlaylistItems(List<String> playlist_items) {
        put(PLAYLIST_ITEMS, playlist_items);
    }

}
