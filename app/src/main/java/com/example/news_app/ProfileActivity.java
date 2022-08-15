package com.example.news_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.news_app.model.Parse.User;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseUser;

public class ProfileActivity extends AppCompatActivity {

    public static final String TAG = "PROFILE ACTIVITY";
    private TextInputLayout et_new_password;
    private EditText edt_new_password;
    private String newPassword;
    private Button btnLogOut;
    private Button btnUpdatePassword;
    private User currentUser;
    private Button btnPersonalInfo;
    SharedPreferences sharedPreferences;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        currentUser = (User) ParseUser.getCurrentUser();

        // USER LOG-OUT
        btnLogOut = findViewById(R.id.btn_log_out);
        btnLogOut.setOnClickListener(v -> {
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        });

        // UPDATE USER PASSWORD
        btnUpdatePassword = (Button) findViewById(R.id.btn_update_password);
        btnUpdatePassword.setOnClickListener(v -> {
            update_password_dialog(v);
        });

        btnPersonalInfo = findViewById(R.id.btn_personal_info);
        btnPersonalInfo.setOnClickListener(v -> {
            Intent i = new Intent(ProfileActivity.this, PersonalInformationActivity.class);
            startActivity(i);
        });
    }

    public void update_password_dialog(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.update_password_dialog, null);

        et_new_password = (TextInputLayout) mView.findViewById(R.id.et_new_Password);
        edt_new_password = (EditText) mView.findViewById(R.id.edt_new_password);
        Button btnCancel = mView.findViewById(R.id.btn_cancel);
        Button btnOk = mView.findViewById(R.id.btn_ok);

        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btnCancel.setOnClickListener(v -> alertDialog.dismiss());

        btnOk.setOnClickListener(v -> {
            newPassword = edt_new_password.getText().toString();
            updatePassword(newPassword, alertDialog);
        });
        alertDialog.show();
    }

    private void updatePassword(String new_password, AlertDialog alertDialog) {
        if (confirmInfo(new_password)) {
            if (currentUser != null) {
                // Other attributes than "email" will remain unchanged!
                currentUser.setPassword(new_password);
                // Saves the object.
                currentUser.saveInBackground(e -> {
                    if (e == null) {
                        //Save successful
                        Toast.makeText(this, R.string.save_Successful, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    } else {
                        // Something went wrong while saving
                        Log.e(TAG, "Update error " + e);
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private boolean confirmInfo(String new_password) {
        if(new_password.isEmpty()) {
            et_new_password.setError("This is a required field!");
            return false;
        }
        et_new_password.setError(null);
        return true;
    }

}