package com.example.news_app.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.news_app.ProfileActivity;
import com.example.news_app.PublishContentActivity;
import com.example.news_app.R;
import com.example.news_app.VideoAdapter;
import com.example.news_app.ViewType;
import com.example.news_app.model.video.Video;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment {

    private ViewType selectedViewType = ViewType.OneColumn;
    public static final String TAG = "HOME FRAGMENT";
    private RecyclerView rvVideos;
    private VideoAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private Menu menu;
    List<Video> allVideos;
    String NOW_PLAYING_URL;
    String secretValue;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
       return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        secretValue = getContext().getString(R.string.API_KEY);
        NOW_PLAYING_URL = "https://youtube.googleapis.com/youtube/v3/videos?part=snippet&part=contentDetails&chart=MostPopular&key="+secretValue;
        rvVideos = view.findViewById(R.id.rvVideos);
        allVideos = new ArrayList<>();
        adapter = new VideoAdapter(getContext(), allVideos, selectedViewType);
        rvVideos.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvVideos.setLayoutManager(linearLayoutManager);

        // API REQUEST
        networkRequest();

        // Lookup the swipe container view
        swipeContainer = view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(() -> {
            // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            allVideos.clear();
            adapter.clear();
            networkRequest();
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        rvVideos = view.findViewById(R.id.rvVideos);


        Button pick = view.findViewById(R.id.pick);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), PublishContentActivity.class);
                getActivity().startActivity(i);
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miChangeView:
                changeSelectedViewType();
                changeMenuItemIcon();
                changeRecyclerViewViewType();
                break;
            case R.id.miSettings:
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeMenuItemIcon() {
        switch (selectedViewType) {
            case OneColumn:
                menu.findItem(R.id.miChangeView).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_two_column));
                break;
            case TwoColumns:
                menu.findItem(R.id.miChangeView).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_three_column));
                break;
            case ThreeColumns:
                menu.findItem(R.id.miChangeView).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_one_column));
                break;
        }
    }

    private void changeSelectedViewType() {
        switch (selectedViewType) {
            case OneColumn:
                selectedViewType = ViewType.TwoColumns;
                break;
            case TwoColumns:
                selectedViewType = ViewType.ThreeColumns;
                break;
            case ThreeColumns:
                selectedViewType = ViewType.OneColumn;
                break;
        }
    }

    private void changeRecyclerViewViewType() {
        switch (selectedViewType) {
            case OneColumn:
                rvVideos.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new VideoAdapter(getContext(), allVideos, selectedViewType);
                rvVideos.setAdapter(adapter);
                break;
            case TwoColumns:
                rvVideos.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                adapter = new VideoAdapter(getContext(), allVideos, selectedViewType);
                rvVideos.setAdapter(adapter);
                break;
            case ThreeColumns:
                rvVideos.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                adapter = new VideoAdapter(getContext(), allVideos, selectedViewType);
                rvVideos.setAdapter(adapter);
                break;
        }
    }

    public void networkRequest() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, getContext().getString(R.string.success));
                JSONObject jsonObject = json.jsonObject;
                try {
                    Type movieListType = new TypeToken<List<Video>>() {
                    }.getType();
                    List<Video> items = new Gson().fromJson(jsonObject.getJSONArray("items").toString(), movieListType);
                    Log.i(TAG, "Items: " + items.toString());
                    allVideos.clear();
                    adapter.clear();
                    allVideos.addAll(items);
                    adapter.notifyDataSetChanged();
                    Log.i(TAG, "Videos: " + allVideos.size());
                    adapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String errorResponse, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e(TAG, getContext().getString(R.string.failure));
                Log.e(TAG, "statusCode! >>>> " + statusCode);
                Log.e(TAG, "errorResponse! >>>> " + errorResponse);
                Log.e(TAG, "Header! >>>> " + headers.toString());
            }
        });
    }
}