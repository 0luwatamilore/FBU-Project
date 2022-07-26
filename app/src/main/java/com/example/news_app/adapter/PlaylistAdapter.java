package com.example.news_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news_app.R;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private final Context context;
    private final List<String> allPlaylist;

    public PlaylistAdapter(Context context, List<String> allPlaylist) {
        this.context = context;
        this.allPlaylist = allPlaylist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_playlists, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String playlist = allPlaylist.get(position);
        holder.bind(playlist);
    }

    @Override
    public int getItemCount() {
        return allPlaylist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView playlist_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playlist_name = itemView.findViewById(R.id.tv_playlist_name);
        }

        public void bind(String playlist) {
            playlist_name.setText(playlist);
        }
    }
}
