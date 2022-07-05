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
    private Context context;
    private List<Search> searches;


    public SearchAdapter(Context context, List<Search> searches) {
        this.context = context;
        this.searches = searches;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("SearchAdapter", "onCreateViewHolder");
        View searchView = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        return new ViewHolder(searchView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("SearchAdapter", "onBindViewHolder " + position);
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
        private TextView search_title;
        private TextView search_description;
        private ImageView search_thumbnail;

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


            search_thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, SearchPlayerActivity.class);
                    i.putExtra("SearchCue", search.getId().getVideoId() != null ? search.getId().getVideoId() : search.getId().getChannelId());
                    context.startActivity(i);
                }
            });
        }
    }

    public void clear() {
        searches.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Search> list) {
        searches.addAll(list);
        notifyDataSetChanged();
    }
}
