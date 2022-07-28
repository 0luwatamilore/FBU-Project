package com.example.news_app.model.Parse;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("_User")
public class User extends ParseUser {

    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_LASTNAME = "lastName";
    public static final String KEY_SEARCH_KEYWORDS = "search_keywords";
    public static final String KEY_HISTORY = "search_history";
    public static final String KEY_PLAYLIST = "playlists";
    public static final String KEY_IMAGE = "profileImage";

    public User() {}

    public void setImage(String photoUrl) {
        put(KEY_IMAGE, photoUrl);
    }

    public String getImage() {
        return getString(KEY_IMAGE);
    }

    public List<String> getPlaylist() {
        List<String> playlists = getList(KEY_PLAYLIST);
        if (playlists != null) {
            return getList(KEY_PLAYLIST);
        } else {
            return new ArrayList<>();
        }
    }

    public String getFirstName() {
        return getString(KEY_FIRSTNAME);
    }

    public void setFirstname(String firstname) {
        put(KEY_FIRSTNAME, firstname);
    }

    public String getLastName() {
        return getString(KEY_LASTNAME);
    }

    public void setLastname(String lastname) {
        put(KEY_LASTNAME, lastname);
    }

    public List<String> getSearchKeyword() {
        List<String> search_keywords = getList(KEY_SEARCH_KEYWORDS);
        if (search_keywords != null) {
            return getList(KEY_SEARCH_KEYWORDS);
        } else {
            return new ArrayList<>();
        }
    }

    public void setKeySearchKeywords(List<String> search_keywords) {
        put(KEY_SEARCH_KEYWORDS, search_keywords);
    }

    public List<String> getHistory() {
        List<String> history = getList(KEY_HISTORY);
        if (history != null) {
            return getList(KEY_HISTORY);
        } else {
            return new ArrayList<>();
        }
    }

}
