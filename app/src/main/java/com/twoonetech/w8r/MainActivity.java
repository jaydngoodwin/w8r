package com.twoonetech.w8r;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private SharedViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = ViewModelProviders.of(this).get(SharedViewModel.class);

        Map<String,String> mapNames = new HashMap<>();
        mapNames.put("Demo Map Small","{\"nodes\":[{\"name\":\"Bar\",\"x\":\"0\",\"y\":\"-1\",\"type\":\"table\"},{\"name\":\"Initial Node\",\"x\":\"0\",\"y\":\"0\",\"type\":\"intersection\" },{\"name\":\"1\",\"x\":\"0\",\"y\":\"1\",\"type\":\"table\"},{\"name\":\"f\",\"x\":\"1\",\"y\":\"0\",\"type\":\"intersection\"},{\"name\":\"2\",\"x\":\"2\",\"y\":\"0\",\"type\":\"table\"},{ \"name\":\"a\",\"x\":\"-1\",\"y\":\"0\",\"type\":\"intersection\"},{\"name\":\"b\",\"x\":\"-1\",\"y\":\"2\",\"type\":\"intersection\"},{\"name\":\"d\",\"x\":\"1\",\"y\":\"2\",\"type\":\"intersection\"},{\"name\":\"e\",\"x\":\"2\",\"y\":\"2\",\"type\":\"intersection\"},{\"name\":\"3\",\"x\":\"2\",\"y\":\"3\",\"type\":\"table\"},{\"name\":\"c\",\"x\":\"-1\",\"y\":\"3\",\"type\":\"intersection\"},{\"name\":\"4\",\"x\":\"-2\",\"y\":\"3\",\"type\":\"table\"}],\"edges\":[{\"node1\":\"Bar\",\"node2\":\"Initial Node\"},{\"node1\":\"1\",\"node2\":\"Initial Node\"},{\"node1\":\"Initial Node\",\"node2\":\"f\"},{\"node1\":\"f\",\"node2\":\"2\"},{\"node1\":\"Initial Node\",\"node2\":\"a\"},{\"node1\":\"a\",\"node2\":\"b\"},{\"node1\":\"b\",\"node2\":\"c\"},{\"node1\":\"c\",\"node2\":\"4\"},{\"node1\":\"f\",\"node2\":\"d\"},{\"node1\":\"d\",\"node2\":\"b\"},{\"node1\":\"d\",\"node2\":\"e\"},{\"node1\":\"e\",\"node2\":\"3\"}]}");

        mapNames.put("Demo Map Large","{\"nodes\":[{\"name\":\"Bar\",\"x\":\"0\",\"y\":\"-1\",\"type\":\"table\"},{\"name\":\"a\",\"x\":\"0\",\"y\":\"0\",\"type\":\"intersection\" },{\"name\":\"b\",\"x\":\"-1\",\"y\":\"0\",\"type\":\"intersection\"},{\"name\":\"c\",\"x\":\"-2\",\"y\":\"0\",\"type\":\"intersection\"},{ \"name\":\"d\",\"x\":\"1\",\"y\":\"0\",\"type\":\"intersection\"},{\"name\":\"e\",\"x\":\"2\",\"y\":\"0\",\"type\":\"intersection\"},{\"name\":\"f\",\"x\":\"2\",\"y\":\"-1\",\"type\":\"intersection\"},{\"name\":\"1\",\"x\":\"-2\",\"y\":\"-1\",\"type\":\"table\"},{\"name\":\"2\",\"x\":\"-3\",\"y\":\"0\",\"type\":\"table\"},{\"name\":\"3\",\"x\":\"-2\",\"y\":\"1\",\"type\":\"table\"},{\"name\":\"4\",\"x\":\"-1\",\"y\":\"1\",\"type\":\"table\"},{\"name\":\"5\",\"x\":\"1\",\"y\":\"1\",\"type\":\"table\"},{\"name\":\"6\",\"x\":\"1\",\"y\":\"-1\",\"type\":\"table\"}],\"edges\":[{\"node1\":\"Bar\",\"node2\":\"a\"},{\"node1\":\"a\",\"node2\":\"b\"},{\"node1\":\"b\",\"node2\":\"c\"},{\"node1\":\"a\",\"node2\":\"d\"},{\"node1\":\"d\",\"node2\":\"e\"},{\"node1\":\"e\",\"node2\":\"f\"},{\"node1\":\"b\",\"node2\":\"4\"},{\"node1\":\"c\",\"node2\":\"1\"},{\"node1\":\"c\",\"node2\":\"2\"},{\"node1\":\"c\",\"node2\":\"3\"},{\"node1\":\"d\",\"node2\":\"5\"},{\"node1\":\"f\",\"node2\":\"6\"}]}");

        Map<String,?> mapNamesPrefsEntries = getSharedPreferences("MapNamesPrefs",MODE_PRIVATE).getAll();
        for (Map.Entry<String,?> entry : mapNamesPrefsEntries.entrySet()) {
            mapNames.put(entry.getKey(),entry.getValue().toString());
        }
        model.getLiveMaps().setValue(mapNames);

        List<Robot> robots = new ArrayList<>();

        Map<String,?> robotNamesPrefsEntries = getSharedPreferences("RobotNamesPrefs",MODE_PRIVATE).getAll();
        Map<String,?> robotMapJsonsPrefsEntries = getSharedPreferences("RobotMapJsonsPrefs",MODE_PRIVATE).getAll();
        for (Map.Entry<String,?> nameEntry : robotNamesPrefsEntries.entrySet()) {
            Robot robot = new Robot(nameEntry.getKey(),nameEntry.getValue().toString());
            for (Map.Entry<String,?> mapEntry : robotMapJsonsPrefsEntries.entrySet()) {
                if (nameEntry.getKey().equals(mapEntry.getKey())) {
                    robot.setMapJson(mapEntry.getValue().toString());
                    break;
                }
            }
            robots.add(robot);
        }

        model.getLiveRobots().setValue(robots);

        //ALSO SAVE APPID?

        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container,new RobotsFragment()).commit();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<Robot> robots = model.getLiveRobots().getValue();
                if (!robots.isEmpty()) {
                    for (Robot robot : robots) {
                        robot.updateStatus();
                    }
                    model.getLiveRobots().postValue(robots);
                }
            }
        },0,1000);

    }
}