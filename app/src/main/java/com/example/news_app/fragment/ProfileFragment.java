package com.example.news_app.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.news_app.LoginActivity;
import com.example.news_app.R;
import com.example.news_app.model.Parse.User;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public static final String TAG = "PROFILE FRAGMENT";
    public static final String MY_PREFERENCE = "nightModePrefs";
    public static final String IS_NIGHT_MODE = "isNightMode";
    SharedPreferences sharedPreferences;
    Button btnLogOut;
    Button btnUpdatePassword;
    EditText etNewPassword;
    Switch aSwitch;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        
        // USER LOG-OUT
        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(v -> {
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
            Intent i = new Intent(getContext(), LoginActivity.class);
            startActivity(i);
            ProfileFragment.super.onDestroyView();
        });

        // ACTIVATE NIGHT MODE
        aSwitch = view.findViewById(R.id.switchl);
        sharedPreferences = getContext().getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);

        checkNightModeActivated();

        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                saveNightModeState(true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                saveNightModeState(false);
            }
            getActivity().recreate();
        });

        // UPDATE USER PASSWORD
        btnUpdatePassword = view.findViewById(R.id.btnUpdatePassword);
        btnUpdatePassword.setOnClickListener(v -> {
            etNewPassword = view.findViewById(R.id.etNewPassword);
            updatePassword();
        });
    }

    private void saveNightModeState(boolean nightMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_NIGHT_MODE, nightMode);
        editor.apply();
    }

    private void checkNightModeActivated() {
        if (sharedPreferences.getBoolean(IS_NIGHT_MODE, false)) {
            aSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            aSwitch.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void updatePassword() {
        User currentUser = (User) ParseUser.getCurrentUser();
        if (currentUser != null) {
            // Other attributes than "email" will remain unchanged!
            String newPassword = etNewPassword.getText().toString();
            currentUser.setPassword(newPassword);
            // Saves the object.
            currentUser.saveInBackground(e -> {
                if (e == null) {
                    //Save successful
                    Toast.makeText(getContext(), R.string.save_Successful, Toast.LENGTH_SHORT).show();
                } else {
                    // Something went wrong while saving
                    Log.e(TAG, "Update error " + e);
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}