package com.twoonetech.w8r;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class RobotFragment extends Fragment {

    private Context mContext;

    private RobotViewModel robotViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        String ip = getArguments().getString("ip");
        robotViewModel = ViewModelProviders.of(this).get(RobotViewModel.class);
        robotViewModel.init(ip);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                robotViewModel.update();
            }
        },0,500);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_robot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        //Get views
        View state = view.findViewById(R.id.state);
        Button goToTable = view.findViewById(R.id.go_to_table);
        Button returnToBar = view.findViewById(R.id.return_to_bar);

        //Observe live data from robots view model
        robotViewModel.getLiveRobot().observe(this, robot -> {
            if (robot != null) {
                //If the robot data has been fetched
                if (robot.getState().equals("idle")) {
                    state.setBackgroundColor(Color.parseColor("#29ff11"));
                } else if (robot.getState().equals("following")) {
                    state.setBackgroundColor(Color.parseColor("#ff9500"));
                } else if (robot.getState().equals("obstacle")) {
                    state.setBackgroundColor(Color.parseColor("#ff140c"));
                }
            }
        });

        goToTable.setOnClickListener(view1 -> {
            robotViewModel.goToTable("2");
        });

        returnToBar.setOnClickListener(view1 -> {

        });
    }

}