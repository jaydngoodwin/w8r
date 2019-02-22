package com.twoonetech.w8r;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RobotsViewModel model = ViewModelProviders.of(this).get(RobotsViewModel.class);

        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Button searchForRobots = findViewById(R.id.search_for_robots);
        searchForRobots.setOnClickListener(view -> {

        });

        Button help = findViewById(R.id.help);
        searchForRobots.setOnClickListener(view -> {

        });

    }

}