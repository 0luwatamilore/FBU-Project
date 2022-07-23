package com.example.news_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.news_app.model.Parse.Post;
import com.example.news_app.model.Parse.User;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class PublishContentActivity extends AppCompatActivity {

    public static final String TAG = "PUBLISH ACTIVITY";
    private static final int PERMISSION = 101;
    private EditText etTitle;
    private User currentUser;
    VideoView videoView;
    Uri videoUri;
    Button btnSubmit;
    MediaController mediaController;
    Intent myFileIntent;
    Button button;
    File videoFile;
    byte[] videoBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_content);

        currentUser = (User) ParseUser.getCurrentUser();
        checkPermission();
        videoBytes = null;

        videoView = findViewById(R.id.videoView);
        mediaController = new MediaController(PublishContentActivity.this);
        videoView.setMediaController(mediaController);
        etTitle = findViewById(R.id.et_title);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(v -> {
            myFileIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            if(myFileIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(myFileIntent, 1);
            }
        });

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(v -> {
            try {
                savePost();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1) {
            videoUri = data.getData();
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
        Log.i(TAG, "video   >>>>>   " + videoUri);
        File file = new File(getRealPathFromURI(videoUri));
        Log.i(TAG, "video   >>>>>   " + file);
        videoFile = file;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void checkPermission() {
        if(ActivityCompat.checkSelfPermission(getApplicationContext()
                , Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(PublishContentActivity.this
                    , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , PERMISSION);
        }
        Log.d(TAG, "checkPermission: Permission granted");
    }

    private byte[] convertVideoToBytes(Context context, Uri uri) {
        videoBytes = null;
        try {//  w  w w  . j ava 2s . c  o m
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            FileInputStream fis = new FileInputStream(String.valueOf(uri));
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);

            videoBytes = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoBytes;
    }

    private void savePost() throws IOException {
        Post post = new Post();
        String Title = etTitle.getText().toString();
        post.setPostUser(currentUser);
        post.setPostTitle(Title);
        videoBytes = convertVideoToBytes(getApplicationContext(), videoUri);
        ParseFile file = new ParseFile("testVideo.mp4", videoBytes);
        file.saveInBackground((SaveCallback) e -> {
            if(e==null) {
                post.setPostVideo(file);
            }
        });
    }
}
