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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.news_app.MainActivity;
import com.example.news_app.R;
import com.example.news_app.SearchAdapter;
import com.example.news_app.model.search.Search;
import com.example.news_app.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    public static final String TAG = "Search Fragment";
    private String SEARCH_URL = "https://youtube.googleapis.com/youtube/v3/search?part=snippet&q=";
    User currentUser;
//    EditText etSearch;
    RecyclerView rvSearch;
    SearchAdapter adapter;
    Button btnEnter;
    AutoCompleteTextView etSearch;
    List<Search> searches;

    List<String> search_list;


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
//        etSearch = view.findViewById(R.id.etSearch);
        etSearch = view.findViewById(R.id.etSearch);
        rvSearch.setLayoutManager(new LinearLayoutManager(getContext()));

        currentUser = (User) ParseUser.getCurrentUser();
        search_list = currentUser.getSearchKeyword();
        Log.i(TAG, "onSuccess!  >>>  " + search_list);
        etSearch.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,search_list));



        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSearch(v);
            }
        });



    }

    public void saveSearch(String search_keyword) {
        User currentUser = (User) ParseUser.getCurrentUser();
        currentUser.addAllUnique("search_keywords", Arrays.asList(search_keyword));
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Success
                    Log.i(TAG, "onSuccess!  >>>  " + currentUser.getSearchKeyword());
                } else {
                    // Error
                    Log.i(TAG, "onFailure!  >>>  " + currentUser.getSearchKeyword() + e);
                }
            }
        });
//        currentUser.getSearchKeyword().add(search_keyword);

    }

    public void createSearch(View view){
        String searchInput = (String) etSearch.getText().toString();
        SEARCH_URL += searchInput;
        SEARCH_URL += "&key=AIzaSyD8bq4-Cv1uZ0Xx531Pa5PTsodeR56azzg";
        Log.i(TAG,"Search URL! >>> " + SEARCH_URL);
        saveSearch(searchInput);
        searchRequest();
        SEARCH_URL = "https://youtube.googleapis.com/youtube/v3/search?part=snippet&q=";
    }


    public void searchRequest() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(SEARCH_URL, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d(TAG, "onSuccess");
                        JSONObject jsonObject = json.jsonObject;
                        try {
                            Type searchListType = new TypeToken<List<Search>>(){}.getType();
                            List<Search> items = new Gson().fromJson(jsonObject.getJSONArray("items").toString(),searchListType);
                            Log.i(TAG, "Items: " + items.toString());
                            searches.clear();
                            adapter.clear();
                            searches.addAll(items);
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