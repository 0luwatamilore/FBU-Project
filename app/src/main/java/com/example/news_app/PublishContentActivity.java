package com.example.news_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.news_app.fragment.HomeFragment;
import com.example.news_app.model.Parse.Post;
import com.example.news_app.model.Parse.User;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class PublishContentActivity extends AppCompatActivity {

    public static final String TAG = "PUBLISH ACTIVITY";
    VideoView videoView;
    Uri videoUri;
    MediaController mediaController;
    EditText etTitle;
    Button btnSubmit;
    String Title;
    File videoFile;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_content);

        videoView = findViewById(R.id.videoView);
        mediaController = new MediaController(PublishContentActivity.this);
        Intent intent = getIntent();
        if(intent != null){
            videoUri = intent.getData();
            videoView.setVideoURI(videoUri);
            videoView.setMediaController(mediaController);
            videoView.start();
        }

        etTitle = findViewById(R.id.etTitle);
        videoFile = getVideoFile(HomeFragment.path);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> {
            Title = etTitle.getText().toString();
            currentUser = (User) ParseUser.getCurrentUser();
            try {
                savePost(Title, currentUser, videoFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public File getVideoFile(String path) {
        File mediaStorageDir = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES), TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }
        return new File(mediaStorageDir.getPath() + File.separator + path);
    }

    private void savePost(String title, User currentUser, File videoFile) throws IOException {
        Post post = new Post();
//        post.setPostUser(currentUser);
//        post.setPostTitle(title);
        byte bytes[] = FileUtils.readFileToByteArray(videoFile);
        ParseFile file = new ParseFile("video.mp4", bytes);
        post.setPostVideo(file);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i(TAG, HomeFragment.path);
                if(e!=null) {
                    Log.e(TAG, "Error while saving", e);
                }else{
                    Log.i(TAG, "Post User and Title was successful!!");


                }
            }
        });
    }
}