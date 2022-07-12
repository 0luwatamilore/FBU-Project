package com.example.news_app.fragment;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.news_app.LoginActivity;
import com.example.news_app.NotificationReceiver;
import com.example.news_app.R;
import com.example.news_app.model.Parse.User;
import com.parse.ParseUser;

import java.util.Calendar;

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

    Button btnNotification;


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

            FragmentTransaction tr = getFragmentManager().beginTransaction();
            tr.attach(ProfileFragment.this).commit();
        });

        // UPDATE USER PASSWORD
        btnUpdatePassword = view.findViewById(R.id.btnUpdatePassword);
        btnUpdatePassword.setOnClickListener(v -> {
            etNewPassword = view.findViewById(R.id.etNewPassword);
            updatePassword();
        });

        createNotificationChannel();

        btnNotification = view.findViewById(R.id.btnNotification);
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Reminder Set!", Toast.LENGTH_SHORT).show();
                Log.i("tag", "Time  >>>>  " + Calendar.getInstance().getTime());
                setAlarm();
            }
        });
    }

    private void setAlarm() {

        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 18);
        calendar.set(Calendar.SECOND, 30);

        if(calendar.getTime().after(now.getTime())) {
            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);

            Intent alarmIntent = new Intent(getContext(), NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NewChannel";
            String description = "Channel for App Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("new notification", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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