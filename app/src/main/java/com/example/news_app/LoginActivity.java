package com.example.news_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LOGIN ACTIVITY";
    private EditText etEmail;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            loginUser(email, password);
        });

        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(v -> goSignUpActivity());

    }


    private void loginUser(String email, String password) {
        Log.i(TAG, getString(R.string.login_attempt) + email);

        ParseUser.logInInBackground(email, password, (user, e) -> {
            if (e != null) {
                Toast.makeText(LoginActivity.this, getString(R.string.login_issue), Toast.LENGTH_SHORT).show();
                Log.e(TAG, getString(R.string.login_issue), e);
                return;
            }
            goMainActivity();
            Toast.makeText(LoginActivity.this, "Welcome! ", Toast.LENGTH_SHORT).show();
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void goSignUpActivity() {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }

}
