package com.example.news_app.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Video {
    String id;
    String title;
    String description;
    String thumbnail;

    public Video(){}

    public Video(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getString("id");
        title = jsonObject.getJSONObject("snippet").getString("title");
        description = jsonObject.getJSONObject("snippet").getString("description");
        thumbnail = jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url");
    }

    public static List<Video> fromJsonArray(JSONArray videoJsonArray) throws JSONException {
        List<Video> videos = new ArrayList<>();
        for(int i=0; i< videoJsonArray.length(); i++){
            videos.add(new Video(videoJsonArray.getJSONObject(i)));
        }
        return videos;
    }


    public String getId() {
        return id;
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
