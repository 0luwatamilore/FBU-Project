package com.example.news_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.news_app.model.Video;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private Context context;
    private List<Video> videos;

    public VideoAdapter (Context context, List<Video> videos){
        this.context = context;
        this.videos = videos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("VideoAdapter", "onCreateViewHolder");
        View videoView = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(videoView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("VideoAdapter", "nBindViewHolder " + position);
        // get the movie at the passed in position
        Video video = videos.get(position);
        // Bind the movie in to the VH
        holder.bind(video);

    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvDescription;
        ImageView ivThumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
        }

        public void bind(Video video) {
            tvTitle.setText(video.getTitle());
            tvDescription.setText(video.getDescription());
            Glide.with(context).load(video.getThumbnail()).into(ivThumbnail);

        }
    }
}
