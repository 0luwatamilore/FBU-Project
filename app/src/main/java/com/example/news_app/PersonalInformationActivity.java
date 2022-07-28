package com.example.news_app;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.news_app.model.Parse.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.parse.ParseUser;

import java.util.HashMap;

public class PersonalInformationActivity extends AppCompatActivity {

    public static final String TAG = "PERSONAL INFORMATION ACTIVITY";
    private ProgressDialog progressDialog;
    private String photoUrl;
    private User currentUser;
    private TextView tv_first_name;
    private TextView tv_last_name;
    private TextView tv_user_name;
    private ImageView iv_profile_pic;
    private Button btn_change_picture;
    private Button btn_save;
    private Uri photoUri;
    private String photoFileName = "photo.jpg";
    ActivityResultLauncher<String> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        // init UI Views
        currentUser = (User) ParseUser.getCurrentUser();
        tv_first_name = findViewById(R.id.tv_first_name);
        tv_last_name = findViewById(R.id.tv_last_name);
        tv_user_name = findViewById(R.id.tv_user_name);
        iv_profile_pic = findViewById(R.id.iv_profile_pic);
        btn_change_picture = findViewById(R.id.btn_change_picture);

        // display data from database
        tv_first_name.setText(currentUser.getFirstName());
        tv_last_name.setText(currentUser.getLastName());
        tv_user_name.setText(currentUser.getUsername());

        // set Image placeholder
        String profileImage = currentUser.getImage();
        if(profileImage==null) {
            Glide.with(getApplicationContext())
                    .load("https://t3.ftcdn.net/jpg/03/46/83/96/360_F_346839683_6nAPzbhpSkIpb8pmAwufkC7c5eD7wYws.jpg")
                    .transform(new CenterCrop(), new RoundedCorners(14))
                    .into(iv_profile_pic);
        } else {
            Glide.with(getApplicationContext()).load(profileImage)
                    .transform(new CenterInside()
                            , new RoundedCorners(14))
                    .into(iv_profile_pic);
        }

        // setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Uploading Image");
        progressDialog.setCanceledOnTouchOutside(false);

        // save new profile picture to database
        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(v -> {
            // Upload image function call
            uploadToFireBase(photoUri);
        });

        // to initiate a change in profile picture
        btn_change_picture.setOnClickListener(v -> mGetContent.launch("image/*"));
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent()
                , result -> {
            if(result==null) {
                super.onBackPressed();
            }
            else{
                Intent intent = new Intent(PersonalInformationActivity.this
                        , CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 101);
            }
        });
    }

    private void uploadToFireBase(Uri photoUri) {
        // show uploading progress
        progressDialog.show();
        // timestamp
        String timestamp = "" + System.currentTimeMillis();
        // File path and name in firebase storage
        String filePathAndName = "Images/" + "image_" + timestamp;
        // storage reference
        StorageReference storageReference = FirebaseStorage
                .getInstance().getReference(filePathAndName);
        // upload image
        storageReference.putFile(photoUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // image uploaded, get url of uploaded image to put in Parse database
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri downloadUri = uriTask.getResult();
                    if (uriTask.isSuccessful()) {
                        // uri of uploaded image is received
                        // now I add image details to my firebase database
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("id", "" + timestamp);
                        hashMap.put("timeStamp", "" + timestamp);
                        hashMap.put("videoUrl", ""  + downloadUri);
                        photoUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase
                                .getInstance()
                                .getReference("Images");
                        reference.child(timestamp)
                                .setValue(hashMap)
                                .addOnSuccessListener(unused -> {
                                    // image details added to database
                                    savePhoto();
                                    progressDialog.dismiss();
                                    Toast.makeText(PersonalInformationActivity.this
                                            , "Image uploaded....", Toast.LENGTH_SHORT)
                                            .show();
                                })
                                .addOnFailureListener(e -> {
                                    // failed adding details to database
                                    progressDialog.dismiss();
                                    Toast.makeText(PersonalInformationActivity.this
                                            , "" + e.getMessage(), Toast.LENGTH_SHORT)
                                            .show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // failed uploading to storage
                    progressDialog.dismiss();
                    Toast.makeText(PersonalInformationActivity.this
                            , "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==-1 && requestCode==101)
        {
            String result = data.getStringExtra("RESULT");
            Uri resultUri = null;
            if(result!=null)
            {
                resultUri = Uri.parse(result);
                photoUri = resultUri;
            }
            iv_profile_pic.setImageURI(resultUri);
        }
    }

    @SuppressLint("LongLogTag")
    private void savePhoto() {
        currentUser = (User) ParseUser.getCurrentUser();
        currentUser.setImage(photoUrl);
        currentUser.saveInBackground(e -> {
            if (e != null) {
                Log.e(TAG, "Issues with saving image", e);
            }
        });
    }
}