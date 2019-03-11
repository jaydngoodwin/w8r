package com.twoonetech.w8r;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class RobotFragment extends Fragment {

    private String ip;
    private SharedViewModel model;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ip = getArguments().getString("ip");
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_robot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(view1 -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            RobotsFragment robotsFragment = new RobotsFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container,robotsFragment).addToBackStack("robots").commit();
        });

        ImageView state = view.findViewById(R.id.state);

        //Observe live data from robots view model
        model.getLiveRobots().observe(this, robots -> {
            Robot robot = model.getRobotWithIp(ip);
            if (robot != null) {
                //If the robot data has been fetched
                switch (robot.getState()) {
                    case "idle":
                        state.setBackgroundResource(R.mipmap.idle_status);
                        break;
                    case "following":
                        state.setBackgroundResource(R.mipmap.serving_status);
                        break;
                    case "obstacle":
                        state.setBackgroundResource(R.mipmap.fallen_status);
                        break;
                }
            }
        });

        Button goToTable = view.findViewById(R.id.go_to_table);
        Button returnToBar = view.findViewById(R.id.return_to_bar);

        goToTable.setOnClickListener(view1 -> AsyncTask.execute(() -> model.getRobotWithIp(ip).goToTable("1")));

        returnToBar.setOnClickListener(view1 -> AsyncTask.execute(() -> model.getRobotWithIp(ip).returnToBar()));
    }

}