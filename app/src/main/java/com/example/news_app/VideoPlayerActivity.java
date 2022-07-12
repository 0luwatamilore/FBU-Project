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
    private String videoId;
    private EditText etPlaylistName;
    private Button btn_Add_To_Playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

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
                Log.e("VideoPlayerActivity", "Error initializing YouTube player");
                Log.e("VideoPlayerActivity", "youTubeInitializationResult  >>> " + youTubeInitializationResult.toString());
            }
        });

        btn_Add_To_Playlist = findViewById(R.id.btn_Add_To_Playlist);
        btn_Add_To_Playlist.setOnClickListener(v -> Add_Dialog(v));


    }


    //   Helper Methods
    public void add_to_playlist() {

        String playlistName = etPlaylistName.getText().toString().toLowerCase();
        User currentUser = (User) ParseUser.getCurrentUser();
        String id = currentUser.getObjectId();
//         specify what type of data we want to query - Playlist.class
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
                        Log.i(TAG, "onSuccess!  >>>  " + object.getPlaylistItems());
                    } else {
                        // Error
                        Log.i(TAG, "onFailure!  >>>  " + object.getPlaylistItems() + e1);
                    }
                });
                Log.i("VideoPlayerActivity: ", "Playlist Name: " + object.getPLAYLIST_Name() + ", objectId: " + object.getObjectId());
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
            Log.i("VideoPlayerActivity", "Add_Dialog  >>>   Success");
            add_to_playlist();
            alertDialog.dismiss();
        });

        alertDialog.show();
    }
}