package com.example.news_app;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.news_app.model.video.Video;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private final Context context;
    private final List<Video> videos;
    private ViewType viewType;


    public VideoAdapter(Context context, List<Video> videos, ViewType viewType) {
        this.context = context;
        this.videos = videos;
        this.viewType= viewType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View videoView = null;
        switch (this.viewType) {
            case OneColumn:
                videoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
                break;
            case TwoColumns:
                videoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_two_columns, parent, false);
                break;
            case ThreeColumns:
                videoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_three_columns, parent, false);
                break;
        }
        Log.d(context.getString(R.string.vAdapter), context.getString(R.string.create_view_holder));
        return new ViewHolder(videoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(context.getString(R.string.vAdapter), context.getString(R.string.bind_view_holder) + position);
        // get the movie at the passed in position
        Video video = videos.get(position);
        // Bind the movie in to the VH
        holder.bind(video);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumbnail;
        TextView tvTitle;
        TextView tvChannelTitle;
        TextView tvDuration;
        TextView tvPublishedAt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvChannelTitle = itemView.findViewById(R.id.tvChannelTitle);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvPublishedAt = itemView.findViewById(R.id.tvPublishedAt);
        }

        public void bind(Video video) {
            tvTitle.setText(video.getSnippet().getTitle());
            tvChannelTitle.setText(video.getSnippet().getChannelTitle());
            Glide.with(context).load(video.getSnippet().getThumbnails().getHigh().getUrl()).into(ivThumbnail);
            tvPublishedAt.setText(video.getSnippet().getPublishedAt());
            tvDuration.setText(video.getContentDetails().getDuration());

            ivThumbnail.setOnClickListener(v -> {
                Intent i = new Intent(context, VideoPlayerActivity.class);
                i.putExtra("VideoCue", video.getId());
                i.putExtra("TitleCue", video.getSnippet().getTitle());
                i.putExtra("ThumbnailsCue", video.getSnippet().getThumbnails().getHigh().getUrl());
                context.startActivity(i);
            });
        }
    }

    public void clear() {
        videos.clear();
        notifyDataSetChanged();
    }

}
