package com.example.news_app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.news_app.R;
import com.example.news_app.VideoAdapter;
import com.example.news_app.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public static final String TAG = "Main Activity";
    public static final String NOW_PLAYING_URL ="https://youtube.googleapis.com/youtube/v3/videos?part=snippet&chart=MostPopular&maxResults=4&videoCategoryId=17&key=AIzaSyD8bq4-Cv1uZ0Xx531Pa5PTsodeR56azzg";
    private RecyclerView rvVideos;
    private VideoAdapter adapter;
    List<Video> allVideos;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvVideos = view.findViewById(R.id.rvVideos);
        allVideos = new ArrayList<>();
        adapter = new VideoAdapter(getContext(), allVideos);
        rvVideos.setAdapter(adapter);
        rvVideos.setLayoutManager(new LinearLayoutManager(getContext()));



        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray items = jsonObject.getJSONArray("items");
                    Log.i(TAG, "Items: " + items.toString());
                    allVideos.addAll(Video.fromJsonArray(items));
                    Log.i(TAG, "Videos: " + allVideos.size());
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String errorResponse, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e(TAG,"onFailure!");
            }
        }
        );


    }
}