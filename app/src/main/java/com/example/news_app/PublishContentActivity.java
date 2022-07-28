package com.example.news_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.news_app.fragment.HomeFragment;
import com.example.news_app.model.Parse.Post;
import com.example.news_app.model.Parse.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.parse.ParseUser;

import java.util.HashMap;


public class PublishContentActivity extends AppCompatActivity {

    public static final String TAG = "PUBLISH ACTIVITY";
    private static final int VIDEO_PICK_GALLERY_CODE = 100;
    private static final int VIDEO_PICK_CAMERA_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private Uri videoUri = null; // Uri of picked video
    private String[] cameraPermissions;
    private ProgressDialog progressDialog;
    private EditText etTitle;
    private User currentUser;
    private String videoUrl;
    private String title;
    VideoView videoView;
    Button btnSubmit;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_content);

        currentUser = (User) ParseUser.getCurrentUser();
        videoView = findViewById(R.id.videoView);
        etTitle = findViewById(R.id.et_title);

        // setup progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Uploading video");
        progressDialog.setCanceledOnTouchOutside(false);

        // Camera Permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA
                , Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // Handle click, pick video from camera/gallery
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(v -> {
            videoPickDialog();
        });

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(v -> {
            title = etTitle.getText().toString().trim();
            if(TextUtils.isEmpty(title)) {
                Toast.makeText(this
                        , "Title is required....."
                        , Toast.LENGTH_SHORT).show();
            }
            else if(videoUri==null) {
                // video is not picked
                Toast.makeText(this
                        , "Pick a video before you can upload....."
                        , Toast.LENGTH_SHORT).show();
            }
            else {
                // upload video function call
                uploadVideoFirebase();
            }
        });
    }

    private void uploadVideoFirebase() {
        // show progress
        progressDialog.show();
        // timestamp
        String timeStamp = "" + System.currentTimeMillis();
        // file path and name in firebase storage
        String filePathAndName = "Videos/" + "video_" + timeStamp;
        // storage reference
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        // upload video, you can upload any type of file using this method
        storageReference.putFile(videoUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // video uploaded, get url of uploaded video
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri downloadUri = uriTask.getResult();
                    if (uriTask.isSuccessful()) {
                        // uri of uploaded is received
                        // now we can video details to our firebase database
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("id", "" + timeStamp);
                        hashMap.put("title", "" + title);
                        hashMap.put("timeStamp", "" + timeStamp);
                        hashMap.put("videoUrl", "" + downloadUri);
                        videoUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase
                                .getInstance()
                                .getReference("Videos");
                        reference.child(timeStamp)
                                .setValue(hashMap)
                                .addOnSuccessListener(aVoid -> {
                                    // video uploaded to database
                                    savePost();
                                    progressDialog.dismiss();
                                    Toast.makeText(PublishContentActivity.this
                                            , "Video Uploaded...", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    // failed adding details to database
                                    progressDialog.dismiss();
                                    Toast.makeText(PublishContentActivity.this
                                            , ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // failed uploading to college
                    progressDialog.dismiss();
                    Toast.makeText(PublishContentActivity.this
                            , ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void videoPickDialog() {
        // dialog display options
        String[] options = {"Camera", "Gallery"};
        // dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Video Form")
                .setItems(options, (dialog, i) -> {
                    if(i==0) {
                        // Camera clicked
                        if (!checkCameraPermission()) {
                            // camera permission not allowed, request it
                            requestCameraPermission();
                        } else {
                            // permission already allowed, take picture
                            videoPickCamera();
                        }
                    }
                    else if (i==1){
                        // Gallery clicked
                        videoPickGallery();
                    }
                })
                .show();
    }

    private void requestCameraPermission() {
        // request camera permission
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result1 = ContextCompat.checkSelfPermission(this
                , Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean result2 = ContextCompat.checkSelfPermission(this
                , Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED;
        return result1 && result2;
    }

    private void videoPickGallery() {
        // pick video from gallery - intent
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent
                .createChooser(intent, "Select Video"), VIDEO_PICK_GALLERY_CODE);
    }

    private void videoPickCamera() {
        // pick video from gallery - intent
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, VIDEO_PICK_CAMERA_CODE);
    }

    private void setVideoToVideoView() {
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        // set mediaController to videoView
        videoView.setMediaController(mediaController);
        // set video Uri
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.setOnPreparedListener(mp -> videoView.pause());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if(grantResults.length > 0) {
                    // check permission allowed or not
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted) {
                        // both permissions allowed
                        videoPickCamera();
                    } else {
                        // both or one of the permissions denied
                        Toast.makeText(this
                                , "Camera & Storage permissions are required"
                                , Toast.LENGTH_SHORT).show();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // called after picking video from camera/gallery
        if(resultCode==RESULT_OK) {
            if(requestCode==VIDEO_PICK_GALLERY_CODE) {
                videoUri = data.getData();
                // show picked video in videoView
                setVideoToVideoView();
            }
            else if(requestCode==VIDEO_PICK_CAMERA_CODE) {
                videoUri = data.getData();
                // show picked video in videoView
                setVideoToVideoView();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void savePost() {
        Post post = new Post();
        post.setPostUser(currentUser);
        post.setPostTitle(title);
        post.setPostVideo(videoUrl);
        post.saveInBackground(e -> {
            if (e!=null) {
                Log.e(TAG, "Issues with saving Post", e);
            }else {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.publish_content, new HomeFragment()).commit();
            }
        });
    }
}
