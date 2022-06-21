package com.example.news_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SignUpActivity extends AppCompatActivity {

    Button btnEnter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnEnter = findViewById(R.id.btnEnter);
        btnEnter.setOnClickListener(v -> {
            Intent i = new Intent (SignUpActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }
}