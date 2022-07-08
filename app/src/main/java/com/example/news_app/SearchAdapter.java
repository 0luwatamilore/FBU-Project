package com.example.news_app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.news_app.model.search.Search;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private final Context context;
    private final List<Search> searches;


    public SearchAdapter(Context context, List<Search> searches) {
        this.context = context;
        this.searches = searches;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("SearchAdapter", context.getString(R.string.create_view_holder));
        View searchView = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        return new ViewHolder(searchView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("SearchAdapter", context.getString(R.string.bind_view_holder) + position);
        // get the movie at the passed in position
        Search search = searches.get(position);
        // Bind the movie in to the VH
        holder.bind(search);
    }

    @Override
    public int getItemCount() {
        return searches.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView search_title;
        private final TextView search_description;
        private final ImageView search_thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            search_title = itemView.findViewById(R.id.search_title);
            search_description = itemView.findViewById(R.id.search_description);
            search_thumbnail = itemView.findViewById(R.id.search_thumbnail);
        }

        public void bind(Search search) {
            search_title.setText(search.getSnippet().getTitle());
            search_description.setText(search.getSnippet().getDescription());
            Glide.with(context).load(search.getSnippet().getThumbnails().getHigh().getUrl()).into(search_thumbnail);


            search_thumbnail.setOnClickListener(v -> {
                Intent i = new Intent(context, SearchPlayerActivity.class);
                i.putExtra("SearchCue", search.getId().getVideoId() != null ? search.getId().getVideoId() : search.getId().getChannelId());
                context.startActivity(i);
            });
        }
    }

    public void clear() {
        searches.clear();
        notifyDataSetChanged();
    }

}
