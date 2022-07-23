package com.example.news_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.news_app.model.Parse.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseUser;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "Sign Up Activity";
    TextInputLayout et_Firstname;
    TextInputLayout et_Lastname;
    TextInputLayout et_Username;
    TextInputLayout et_Pass;
    Button btnEnter;
    String firstname;
    String lastname;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_Firstname = findViewById(R.id.et_FirstName);
        et_Lastname = findViewById(R.id.et_LastName);
        et_Username = findViewById(R.id.et_Username);
        et_Pass = findViewById(R.id.et_Pass);


        btnEnter = findViewById(R.id.btnEnter);
        btnEnter.setOnClickListener(v -> {
            firstname = et_Firstname.getEditText().getText().toString();
            lastname = et_Lastname.getEditText().getText().toString();
            username = et_Username.getEditText().getText().toString();
            password = et_Pass.getEditText().getText().toString();

            signupUser(firstname, lastname, username, password);
        });
    }

    private boolean validateInfo() {
        if(username.isEmpty() || password.isEmpty()) {
            et_Username.setError("This is a required field!");
            et_Pass.setError("This is a required field!");
            return false;
        }
        et_Username.setError(null);
        et_Pass.setError(null);
        return true;
    }

    private void signupUser(String firstName, String lastName, String username, String password) {
        if(validateInfo()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setFirstname(firstName);
            user.setLastname(lastName);
            user.signUpInBackground(e -> {
                if (e != null) {
                    Log.e(TAG, "Issues with Sign Up! ", e);
                    Toast.makeText(SignUpActivity.this, "Issue with Sign Up ", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(SignUpActivity.this, getString(R.string.welcome), Toast.LENGTH_SHORT).show();
                goMainActivity();
            });
        }
    }

    private void goMainActivity() {
        Intent i = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}