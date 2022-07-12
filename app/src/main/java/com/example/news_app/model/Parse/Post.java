package com.example.news_app.model.Parse;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String POST_USER = "user";
    public static final String POST_TITLE = "title";
    public static final String POST_VIDEO = "video";
    public static final String POST_CREATED_KEY = "createdAt";

    public Post() {}

    public ParseUser getPostUser() {
        return getParseUser(POST_USER);
    }

    public void setPostUser(ParseUser user) {
        put(POST_USER, user);
    }

    public String getPostTitle() {
        return getString(POST_TITLE);
    }

    public void setPostTitle(String title) {
        put(POST_TITLE, title);
    }

    public ParseFile getPostVideo() {
        return getParseFile(POST_VIDEO);
    }

    public void setPostVideo(ParseFile parseFile) {
        put(POST_VIDEO, parseFile);
    }

}
