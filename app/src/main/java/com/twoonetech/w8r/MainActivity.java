package com.twoonetech.w8r;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = this.getSupportFragmentManager();

        Bundle args = new Bundle();
        args.putString("ip","192.168.105.149");
        RobotFragment robotFragment = new RobotFragment();
        robotFragment.setArguments(args);

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container,robotFragment).commit();

        Button help = findViewById(R.id.help);
        help.setOnClickListener(view -> {

        });

    }

}