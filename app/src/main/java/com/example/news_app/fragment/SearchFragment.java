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
import android.widget.Button;
import android.widget.EditText;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.news_app.R;
import com.example.news_app.SearchAdapter;
import com.example.news_app.model.Search;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    public static final String TAG = "Search Fragment";
    private String SEARCH_URL = "https://youtube.googleapis.com/youtube/v3/search?part=snippet&q=";
    EditText etSearch;
    RecyclerView rvSearch;
    SearchAdapter adapter;
    Button btnEnter;
    List<Search> searches;



    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvSearch = view.findViewById(R.id.rvSearch);
        searches = new ArrayList<>();
        adapter = new SearchAdapter(getContext(), searches);
        rvSearch.setAdapter(adapter);
        btnEnter = view.findViewById(R.id.btnEnter);
        etSearch = view.findViewById(R.id.etSearch);
        rvSearch.setLayoutManager(new LinearLayoutManager(getContext()));


        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSearch(v);
            }
        });




    }

    public void createSearch(View view){
        String searchInput = (String) etSearch.getText().toString();
        SEARCH_URL += searchInput + "&key=AIzaSyD8bq4-Cv1uZ0Xx531Pa5PTsodeR56azzg";
        Log.i(TAG,"Search URL! >>> " + SEARCH_URL);
        searchRequest();
    }


    public void searchRequest() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(SEARCH_URL, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d(TAG, "onSuccess");
                        JSONObject jsonObject = json.jsonObject;
                        try {
                            JSONArray items = jsonObject.getJSONArray("items");
                            Log.i(TAG, "Items: " + items.toString());
                            searches.addAll(Search.fromJsonArray(items));
                            adapter.notifyDataSetChanged();
                            Log.i(TAG, "Searches: " + searches.size());
                        } catch (JSONException e) {
                            Log.e(TAG, "Hit json exception", e);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String errorResponse, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.e(TAG,"onFailure!");
                        Log.e(TAG,"statusCode! >>>> " + statusCode);
                        Log.e(TAG,"errorResponse! >>>> " + errorResponse);
                        Log.e(TAG,"Header! >>>> " + headers.toString());
                    }
                }
        );
    }

}