package com.example.news_app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.news_app.model.Parse.History;
import com.example.news_app.model.Parse.Playlist;
import com.example.news_app.model.Parse.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class VideoPlayerActivity extends YouTubeBaseActivity {

    private static final String TAG = "VideoPlayerActivity: ";
    private TextInputLayout etPlaylistName;
    private ImageButton add_to_playlist;
    private EditText edt_playlist_name;
    private List<History> historyList;
    private List<String> playlistList;
    private String videoThumbnail;
    private String playlistName;
    private String videoTitle;
    private User currentUser;
    private String videoId;
    private String id;

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
        playlistList = new ArrayList<>();

        // resolve the player view from the layout
        YouTubePlayerView playerView = findViewById(R.id.youtube_player_view);

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
                    public void onPaused() {}
                    @Override
                    public void onStopped() {
                        queryHistory();
                    }
                    @Override
                    public void onBuffering(boolean b) {}
                    @Override
                    public void onSeekTo(int i) {}
                });
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {// log the error
                Log.e("VideoPlayerActivity", "Error initializing YouTube player");
                Log.e("VideoPlayerActivity", "youTubeInitializationResult  >>> " + youTubeInitializationResult.toString());
            }
        });
        add_to_playlist = findViewById(R.id.add_to_playlist);
        add_to_playlist.setOnClickListener(this::Add_Dialog);
    }

    // Verifies if the video has been played before
    public void queryHistory() {
        ParseQuery<History> query = ParseQuery.getQuery(History.class);
        query.whereContains("user", id);
        query.whereEqualTo("videoId", videoId);
        query.findInBackground((objects, e) -> {
            if (e != null) {
                Log.e(TAG, "Issues with getting posts", e);
                return;
            }
            historyList.addAll(objects);
            if (historyList.isEmpty()) {
                History history = new History();
                history.setHistoryUser(currentUser);
                history.setHistoryVideoId(videoId);
                history.setHistoryTitle(videoTitle);
                history.setHistoryThumbnail(videoThumbnail);
                history.saveInBackground();
            }
        });
    }

    public void verifyPlaylist(String playlistName, AlertDialog alertDialog) {
        if (playlistName.isEmpty()) {
            etPlaylistName.setError("This is a required field!");
            return;
        }
        etPlaylistName.setError(null);
        queryPlaylist(playlistName, alertDialog);
    }

    public void playlistResultVerify(boolean hasPlaylist, AlertDialog alertDialog) {
        if (!hasPlaylist) {
            etPlaylistName.setError("Enter a valid playlist");
        } else {
            etPlaylistName.setError(null);
            add_to_playlist();
            alertDialog.dismiss();
            Toast.makeText(this, "Added to playlist", Toast.LENGTH_SHORT).show();
        }
        return;
    }

    public void queryPlaylist(String playlistName, AlertDialog alertDialog) {
        ParseQuery<Playlist> query = ParseQuery.getQuery(Playlist.class);
        query.whereEqualTo("user", currentUser);
        query.whereEqualTo("playlist_name", playlistName);
        query.findInBackground((objects, e) -> {
            for(Playlist object:objects) {
                playlistList.add(object.getPlaylistVideo());
            }
            if (e == null && objects.size() == 0) {
                playlistResultVerify(false, alertDialog);
            } else if(e == null && objects.size()>0) {
                playlistResultVerify(true, alertDialog);
            } else {
                Log.e(TAG, "Issues with getting playlists", e);
                return;
            }
        });
    }

    public void Add_Dialog(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(VideoPlayerActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.layout_add_dialog, null);

        etPlaylistName = (TextInputLayout) mView.findViewById(R.id.et_playlist_name);
        edt_playlist_name = (EditText) mView.findViewById(R.id.edt_playlist_name);

        Button btnCreate = mView.findViewById(R.id.btn_create);
        Button btnCancel = mView.findViewById(R.id.btn_cancel);
        Button btnOk = mView.findViewById(R.id.btn_ok);

        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btnCancel.setOnClickListener(v -> alertDialog.dismiss());

        btnOk.setOnClickListener(v -> {
            playlistName = edt_playlist_name.getText().toString().toLowerCase();
            verifyPlaylist(playlistName, alertDialog);

        });
        alertDialog.show();
    }

    public void add_to_playlist() {
        if(!playlistList.contains(videoId)) {
            Playlist playlist = new Playlist();
            playlist.setPlaylistUser(currentUser);
            playlist.setPlaylistName(playlistName);
            playlist.setPlaylistTitle(videoTitle);
            playlist.setPlaylistThumbnail(videoThumbnail);
            playlist.setPlaylistVideo(videoId);
            playlist.saveInBackground();
        }
        return;
    }
}