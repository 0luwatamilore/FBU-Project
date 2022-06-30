package com.example.news_app.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Search {
    String videoId;
    String title;
    String description;
    String thumbnail;

    public Search() {}

    public Search(JSONObject jsonObject) throws JSONException {
        videoId = jsonObject.getString("id");
        title = jsonObject.getJSONObject("snippet").getString("title");
        description = jsonObject.getJSONObject("snippet").getString("description");
        thumbnail = jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url");
    }

    public static List<Search> fromJsonArray(JSONArray searchJsonArray) throws JSONException {
        List<Search> searches = new ArrayList<>();
        for(int i=0; i< searchJsonArray.length(); i++){
            searches.add(new Search(searchJsonArray.getJSONObject(i)));
        }
        return searches;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
