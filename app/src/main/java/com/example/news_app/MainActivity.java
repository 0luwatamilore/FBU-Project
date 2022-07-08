package com.example.news_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.news_app.fragment.ComposeFragment;
import com.example.news_app.fragment.HomeFragment;
import com.example.news_app.fragment.ProfileFragment;
import com.example.news_app.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {


    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_home:
                    Toast.makeText(MainActivity.this, "Home!", Toast.LENGTH_SHORT).show();
                    fragment = new HomeFragment();
                    break;
                case R.id.action_compose:
                    Toast.makeText(MainActivity.this, "Compose!", Toast.LENGTH_SHORT).show();
                    fragment = new ComposeFragment();
                    break;
                case R.id.action_search:
                    Toast.makeText(MainActivity.this, "Search!", Toast.LENGTH_SHORT).show();
                    fragment = new SearchFragment();
                    break;
                case R.id.action_profile:
                default:
                    Toast.makeText(MainActivity.this, "Profile!", Toast.LENGTH_SHORT).show();
                    fragment = new ProfileFragment();
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            return true;
        });
        // Set default selection
        bottomNavigation.setSelectedItemId(R.id.action_home);


    }

}