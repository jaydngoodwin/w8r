package com.twoonetech.w8r;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        //Get views
        View state = view.findViewById(R.id.state);
        TextView stateText = view.findViewById(R.id.state_text);
        Button goToTable = view.findViewById(R.id.go_to_table);
        Button returnToBar = view.findViewById(R.id.return_to_bar);

        //Observe live data from robots view model
        model.getRobots().observe(this, robots -> {
            Robot robot = model.getRobotWithIp(ip);
            if (robot != null) {
                //If the robot data has been fetched
                switch (robot.getState()) {
                    case "idle":
                        state.setBackgroundColor(Color.parseColor("#29ff11"));
                        stateText.setText("IDLE");
                        break;
                    case "following":
                        state.setBackgroundColor(Color.parseColor("#ff9500"));
                        stateText.setText("FOLLOWING");
                        break;
                    case "obstacle":
                        state.setBackgroundColor(Color.parseColor("#ff140c"));
                        stateText.setText("OBSTACLE");
                        break;
                }
            }
        });

        goToTable.setOnClickListener(view1 -> AsyncTask.execute(() -> model.getRobotWithIp(ip).goToTable("1")));

        returnToBar.setOnClickListener(view1 -> AsyncTask.execute(() -> model.getRobotWithIp(ip).returnToBar()));
    }

}