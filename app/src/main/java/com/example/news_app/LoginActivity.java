package com.example.news_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LOGIN ACTIVITY";
    private EditText et_username;
    private EditText edt_password;
    private TextInputLayout et_password;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        et_username = findViewById(R.id.et_username);
        edt_password = findViewById(R.id.edt_password);
        et_password = findViewById(R.id.et_password);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            username = et_username.getText().toString();
            password = edt_password.getText().toString();
            if(validateInfo()){
                loginUser(username, password);
            }
        });

        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(v -> goSignUpActivity());
    }

    private boolean validateInfo() {
        if(username.isEmpty() || password.isEmpty()) {
            et_username.setError("This is a required field!");
            et_password.setError("This is a required field!");
            return false;
        }
        et_username.setError(null);
        et_password.setError(null);
        return true;
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, getString(R.string.login_attempt) + username);
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e != null) {
                et_password.setError("Invalid username/password");
                Toast.makeText(LoginActivity.this, getString(R.string.login_issue), Toast.LENGTH_SHORT).show();
                Log.e(TAG, getString(R.string.login_issue), e);
                return;
            }
            edt_password.setError(null);
            Toast.makeText(LoginActivity.this, "Welcome! ", Toast.LENGTH_SHORT).show();
            goMainActivity();
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
