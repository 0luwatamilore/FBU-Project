package com.example.news_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.news_app.model.Parse.History;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<History> allHistory;

    public HistoryAdapter(Context context, List<History> allHistory) {
        this.context = context;
        this.allHistory = allHistory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = allHistory.get(position);
        holder.bind(history);
    }

    @Override
    public int getItemCount() {
        return allHistory.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView history_image;
        TextView history_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            history_image = itemView.findViewById(R.id.history_image);
            history_title = itemView.findViewById(R.id.history_title);
        }

        public void bind(History history) {
            history_title.setText(history.getHistoryTitle());
            Glide.with(context).load(history.getHistoryThumbnail()).into(history_image);
        }
    }
}
