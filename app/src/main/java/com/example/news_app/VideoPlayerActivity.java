package com.example.news_app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.news_app.model.Parse.History;
import com.example.news_app.model.Parse.Playlist;
import com.example.news_app.model.Parse.User;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VideoPlayerActivity extends YouTubeBaseActivity {

    private static final String TAG = "VideoPlayerActivity: ";
    private User currentUser;
    private String videoId;
    private String id;
    private String videoTitle;
    private String videoThumbnail;
    private EditText etPlaylistName;
    private Button btn_Add_To_Playlist;
    private List<History> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoId = getIntent().getStringExtra("VideoCue");
        videoTitle = getIntent().getStringExtra("TitleCue");
        videoThumbnail = getIntent().getStringExtra("ThumbnailsCue");

        historyList = new ArrayList<>();
        currentUser = (User) ParseUser.getCurrentUser();
        id = currentUser.getObjectId();

        // resolve the player view from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);

        playerView.initialize(getString(R.string.API_KEY), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                // do any work here to cue video, play video, etc.
                youTubePlayer.cueVideo(videoId);
                youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                    @Override
                    public void onPlaying() {
                        queryHistory();
                    }

                    @Override
                    public void onPaused() {
                    }

                    @Override
                    public void onStopped() {
                        queryHistory();
                    }

                    @Override
                    public void onBuffering(boolean b) {
                    }

                    @Override
                    public void onSeekTo(int i) {
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {// log the error
                Log.e("VideoPlayerActivity", "Error initializing YouTube player");
                Log.e("VideoPlayerActivity", "youTubeInitializationResult  >>> " + youTubeInitializationResult.toString());
            }
        });

        btn_Add_To_Playlist = findViewById(R.id.btn_Add_To_Playlist);
        btn_Add_To_Playlist.setOnClickListener(v -> Add_Dialog(v));


    }

    // Verifies if the video has been played before
    public void queryHistory() {
        ParseQuery<History> query = ParseQuery.getQuery(History.class);
        query.whereContains("user", id);
        query.whereEqualTo("videoId", videoId);
        query.findInBackground((objects, e) -> {
            if (e != null) {
                Log.e(TAG,"Issues with getting posts", e);
                return;
            }
            historyList.addAll(objects);
            if(historyList.isEmpty()) {
                History history = new History();
                history.setHistoryUser(currentUser);
                history.setHistoryVideoId(videoId);
                history.setHistoryTitle(videoTitle);
                history.setHistoryThumbnail(videoThumbnail);
                history.saveInBackground();
            }
        });

    }

    public void add_to_playlist() {
        String playlistName = etPlaylistName.getText().toString().toLowerCase();
        ParseQuery<Playlist> query = ParseQuery.getQuery(Playlist.class);
        query.whereContains("user", id);
        query.whereEqualTo("playlist_name", playlistName);
        query.findInBackground((objects, e) -> {
            if (e != null) {
                Toast.makeText(VideoPlayerActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            for (Playlist object : objects) {
                object.addAllUnique("playlist_items", Arrays.asList(videoId));
                object.saveInBackground(e1 -> {
                    if (e1 != null) {
                        // Error
                        Log.e(TAG, "onFailure!  >>>  " + object.getPlaylistItems() + e1);
                    }
                });
            }
        });
    }

    public void Add_Dialog(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(VideoPlayerActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.layout_add_dialog, null);

        etPlaylistName = (EditText) mView.findViewById(R.id.etPlaylistName);

        Button btnCancel = (Button) mView.findViewById(R.id.btnCancel);
        Button btnOk = (Button) mView.findViewById(R.id.btnOk);

        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btnCancel.setOnClickListener(v -> alertDialog.dismiss());

        btnOk.setOnClickListener(v -> {
            add_to_playlist();
            alertDialog.dismiss();
        });

        alertDialog.show();
    }
}