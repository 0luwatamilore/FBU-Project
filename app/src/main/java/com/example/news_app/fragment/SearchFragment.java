package com.example.news_app.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.news_app.R;
import com.example.news_app.SearchAdapter;
import com.example.news_app.model.search.Search;
import com.example.news_app.model.Parse.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseUser;

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

    public static final String TAG = "SEARCH FRAGMENT";
    private String searchUrl = "https://youtube.googleapis.com/youtube/v3/search?part=snippet&maxResults=100&q=";
    User currentUser;
    RecyclerView rvSearch;
    SearchAdapter adapter;
    Button btnEnter;
    AutoCompleteTextView etSearch;
    List<Search> searches;
    List<String> search_list;
    ArrayAdapter<String> arrayAdapter;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView  = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvSearch = view.findViewById(R.id.rvSearch);
        searches = new ArrayList<>();
        adapter = new SearchAdapter(getContext(), searches);
        rvSearch.setAdapter(adapter);
        btnEnter = view.findViewById(R.id.btnEnter);
        etSearch = (AutoCompleteTextView) view.findViewById(R.id.etSearch);
        rvSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        currentUser = (User) ParseUser.getCurrentUser();
        search_list = currentUser.getSearchKeyword();
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_activated_1, search_list);
        etSearch.setAdapter(arrayAdapter);

        // SEARCH FEATURE
        btnEnter.setOnClickListener(v -> {
            collapseKeyboard();
            createSearch();
            arrayAdapter.notifyDataSetChanged();
        });

        collapseKeyboard();

        // PREFETCH FEATURE
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                new Handler().postDelayed(() -> {
                    createSearch();
                    arrayAdapter.notifyDataSetChanged();
                },5000);
            }
        });
    }


    // Helper Methods
    // COLLAPSES KEYBOARD
    public void collapseKeyboard() {
        etSearch.setOnDismissListener(() -> {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getApplicationWindowToken(), 0);
        });
    }

    // Saving search keywords to database
    public void saveSearch(String search_keyword) {
        User currentUser = (User) ParseUser.getCurrentUser();
        currentUser.addAllUnique("search_keywords", Arrays.asList(search_keyword));
        currentUser.saveInBackground(e -> {
            if (e == null) {
                // Success
            } else {
                // Error
                Log.e(TAG, getString(R.string.failure) + "  >>>  " + currentUser.getSearchKeyword() + e);
            }
        });
    }

    // INITIATE SEARCH
    public void createSearch() {
        String searchInput = (String) etSearch.getText().toString();
        searchUrl += searchInput;
        searchUrl += "&key=" + getString(R.string.API_KEY);
        Log.i(TAG, "Search URL! >>> " + searchUrl);
        saveSearch(searchInput);
        arrayAdapter.add(searchInput);
        searchRequest();
        searchUrl = "https://youtube.googleapis.com/youtube/v3/search?part=snippet&maxResults=100&q=";
    }

    // SEARCH DATA API REQUEST
    public void searchRequest() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, getString(R.string.success));
                JSONObject jsonObject = json.jsonObject;
                try {
                    Type searchListType = new TypeToken<List<Search>>() {
                    }.getType();
                    List<Search> items = new Gson().fromJson(jsonObject.getJSONArray("items").toString(), searchListType);
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
                Log.e(TAG, "onFailure!");
                Log.e(TAG, "statusCode! >>>> " + statusCode);
                Log.e(TAG, "errorResponse! >>>> " + errorResponse);
                Log.e(TAG, "Header! >>>> " + headers.toString());
            }
        });
    }

}