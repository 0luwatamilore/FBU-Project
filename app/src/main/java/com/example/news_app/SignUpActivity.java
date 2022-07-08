package com.example.news_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "Sign Up Activity";
    Button btnEnter;
    EditText etFirstName;
    EditText etLastName;
    EditText etUsername;
    EditText etPass;
    EditText edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etUsername = findViewById(R.id.etUsername);
        etPass = findViewById(R.id.etPass);
        edtEmail = findViewById(R.id.etEmail);

        btnEnter = findViewById(R.id.btnEnter);
        btnEnter.setOnClickListener(v -> {
            String FirstName = etFirstName.getText().toString();
            String LastName = etLastName.getText().toString();
            String Username = etUsername.getText().toString();
            String Password = etPass.getText().toString();
            String email = etPass.getText().toString();

            signupUser(FirstName, LastName, Username, Password, email);
        });
    }



    // Helper Methods
    private void signupUser(String firstName, String lastName, String username, String password, String email) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.signUpInBackground(e -> {
            if (e != null) {
                Log.e(TAG, "Issues with Sign Up! ", e);
                Toast.makeText(SignUpActivity.this, "Issue with Sign Up ", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(SignUpActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Successful! " + user);
            goMainActivity();
        });
    }

    private void goMainActivity() {
        Intent i = new Intent (SignUpActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}