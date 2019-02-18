package com.twoonetech.w8r;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("App start", "App start");

        RobotsViewModel model = ViewModelProviders.of(this).get(RobotsViewModel.class);

        Button searchForRobots = findViewById(R.id.search_for_robots);
        searchForRobots.setOnClickListener(view -> {

        });

    }

}