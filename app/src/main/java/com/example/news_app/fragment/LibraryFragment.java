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

import com.example.news_app.HistoryAdapter;
import com.example.news_app.R;
import com.example.news_app.model.Parse.History;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends Fragment {

    RecyclerView history_recyclerview;
    HistoryAdapter historyAdapter;
    List<History> allHistory;

    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        history_recyclerview = view.findViewById(R.id.history_recyclerView);
        allHistory = new ArrayList<>();
        historyAdapter = new HistoryAdapter(getContext(), allHistory);
        history_recyclerview.setAdapter(historyAdapter);
        history_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        queryHistory();
    }

    private void queryHistory() {
        ParseQuery<History> query = ParseQuery.getQuery(History.class);
        query.include(History.HISTORY_USER);
        query.whereEqualTo(History.HISTORY_USER, ParseUser.getCurrentUser());
        query.setLimit(6);
        query.addDescendingOrder(History.HISTORY_CREATED_AT);
        query.findInBackground(new FindCallback<History>() {
            @Override
            public void done(List<History> objects, ParseException e) {
                if(e != null){
                    Log.e("TAG","Issues with getting posts", e);
                    return;
                }
                allHistory.addAll(objects);
                historyAdapter.notifyDataSetChanged();
            }
        });
    }
}