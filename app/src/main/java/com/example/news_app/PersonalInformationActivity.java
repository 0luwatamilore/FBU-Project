package com.example.news_app;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.news_app.model.Parse.User;
import com.parse.ParseUser;

public class PersonalInformationActivity extends AppCompatActivity {

    private User currentUser;
    private TextView tv_first_name;
    private TextView tv_last_name;
    private TextView tv_user_name;
    private ImageView iv_profile_pic;
    private Button btn_change_picture;
    ActivityResultLauncher<String> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        currentUser = (User) ParseUser.getCurrentUser();
        tv_first_name = findViewById(R.id.tv_first_name);
        tv_last_name = findViewById(R.id.tv_last_name);
        tv_user_name = findViewById(R.id.tv_user_name);
        iv_profile_pic = findViewById(R.id.iv_profile_pic);
        btn_change_picture = findViewById(R.id.btn_change_picture);

        tv_first_name.setText(currentUser.getFirstName());
        tv_last_name.setText(currentUser.getLastName());
        tv_user_name.setText(currentUser.getUsername());


        btn_change_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent = new Intent(PersonalInformationActivity.this, CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 101);
            }
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
            }
            iv_profile_pic.setImageURI(resultUri);
        }
    }
}