package com.example.news_app;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("vf5W5u7wdTcn1f5HXaNmIuy6U2Zl9CrdDZpDmOyS")
                // if defined
                .clientKey("mO36b3M3GZH0AyNHUa64j9vp62by9vtkfUOHZfMY")
                .server("https://parseapi.back4app.com")
                .build()
        );

    }
}
