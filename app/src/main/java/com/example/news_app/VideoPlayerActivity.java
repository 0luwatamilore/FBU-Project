package com.example.news_app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.news_app.model.Parse.Playlist;
import com.example.news_app.model.Parse.User;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;

public class VideoPlayerActivity extends YouTubeBaseActivity {

    private static final String TAG = "VideoPlayerActivity: ";
    private User currentUser;
    private String videoId;
    private EditText etPlaylistName;
    private EditText etNewPlaylistName;
    private Button btn_Add_To_Playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        currentUser = (User) ParseUser.getCurrentUser();
        videoId = getIntent().getStringExtra("VideoCue");

        // resolve the player view from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);

        playerView.initialize(getString(R.string.API_KEY), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                // do any work here to cue video, play video, etc.
                youTubePlayer.cueVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {// log the error
                Log.e(TAG, "Error initializing YouTube player");
                Log.e(TAG, "youTubeInitializationResult  >>> " + youTubeInitializationResult.toString());
            }
        });
        btn_Add_To_Playlist = findViewById(R.id.btn_Add_To_Playlist);
        btn_Add_To_Playlist.setOnClickListener(v -> Add_Dialog(v));
    }


    // User can Add the current video to an existing playlist
    public void Add_Dialog(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(VideoPlayerActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.update_playlist_dialog, null);
        etPlaylistName = (EditText) mView.findViewById(R.id.etPlaylistName);
        Button btnCancel = (Button) mView.findViewById(R.id.btnCancel);
        Button btnAdd = (Button) mView.findViewById(R.id.btnAdd);
        Button btnNew = (Button) mView.findViewById(R.id.btnNew);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        btnCancel.setOnClickListener(v -> alertDialog.dismiss());
        btnAdd.setOnClickListener(v -> {
            add_to_playlist();
            alertDialog.dismiss();
        });

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_dialog();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void create_dialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(VideoPlayerActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.create_playlist_dialog, null);
        etNewPlaylistName = (EditText) mView.findViewById(R.id.etNewPlaylistName);
        Button btnCancel = (Button) mView.findViewById(R.id.btnCancel);
        Button btnCreate = (Button) mView.findViewById(R.id.btnCreate);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        btnCancel.setOnClickListener(v -> alertDialog.dismiss());
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_playlist();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    // Adds the video to an already created playlist
    public void add_to_playlist() {
        String playlistName = etPlaylistName.getText().toString().toLowerCase();
        String id = currentUser.getObjectId();
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
                    if (e1 == null) {
                        // Success
                        Toast.makeText(this, "Added to Playlist!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Error
                        Log.e(TAG, "onFailure!  >>>  " + object.getPlaylistItems() + e1);
                    }
                });
            }
        });
    }

    // Automatically adds the video to the newly created playlist
    private void create_playlist() {
        String NewPlaylistName = etNewPlaylistName.getText().toString().toLowerCase();
        String id = (String) currentUser.getObjectId();
        if(etNewPlaylistName.getText().toString().length() != 0) {
            Playlist playlist = new Playlist();
            playlist.setPLAYLIST_Name(NewPlaylistName);
            playlist.setUser(currentUser);
            playlist.addAllUnique("playlist_items", Arrays.asList(videoId));
            playlist.saveInBackground(e -> {
                if (e==null) {
                    Toast.makeText(this, "Playlist Created!", Toast.LENGTH_SHORT).show();
                }else{
                    Log.e(TAG, e.getMessage());
                }
            });
        }else{
            Log.e(TAG, "Please enter a title and description");
        }
    }
}