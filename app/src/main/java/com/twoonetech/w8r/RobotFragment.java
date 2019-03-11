package com.twoonetech.w8r;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.List;
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
        FragmentManager fm = getActivity().getSupportFragmentManager();
        MapFragment mapFragment = new MapFragment();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.map_container,mapFragment).addToBackStack("map").commit();

        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(view1 -> {
            RobotsFragment robotsFragment = new RobotsFragment();
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
                        state.setBackgroundResource(R.mipmap.obstructed_status);
                        break;
                }
            }
        });

        Button goToTable = view.findViewById(R.id.go_to_table);
        Button returnToBar = view.findViewById(R.id.return_to_bar);

        goToTable.setOnClickListener(view1 -> {
            AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
            b.setTitle("Pick table number");
            //String[] data = (String[]) model.getRobotWithIp(ip).getTables().toArray();
            String[] data = model.getRobotWithIp(ip).getTables().toArray(new String[model.getRobotWithIp(ip).getTables().size()]);
            b.setItems(data, (dialogInterface, i) -> {
                dialogInterface.dismiss();
                AsyncTask.execute(() -> {
                    String goToTableResponse = model.getRobotWithIp(ip).goToTable(model.getRobotWithIp(ip).getTables().get(i));
                    if (goToTableResponse != null && goToTableResponse.equals("ok")) {
                        Handler handler = new Handler(getActivity().getMainLooper());
                        handler.post(() -> Toast.makeText(getActivity(), "Robot destination: table "+model.getRobotWithIp(ip).getTables().get(i),Toast.LENGTH_LONG).show());
                    } else {
                        Log.e("MainActivity", "robot isn't turned on or doesn't exist");
                        Handler handler = new Handler(getActivity().getMainLooper());
                        handler.post(() -> Toast.makeText(getActivity(), "Robot isn't turned on or table doesn't exist",Toast.LENGTH_LONG).show());
                    }
                });
            });
            b.show();
        });

        returnToBar.setOnClickListener(view1 -> AsyncTask.execute(() -> model.getRobotWithIp(ip).returnToBar()));
    }

}