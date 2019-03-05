package com.twoonetech.w8r;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<List<Robot>> robots;

    public void init() {
        if (robots == null) {
            robots = new MutableLiveData<>();
            robots.setValue(new ArrayList<>());
        }
    }

    public MutableLiveData<List<Robot>> getRobots() {
        if (robots == null) {
            robots = new MutableLiveData<>();
            robots.setValue(new ArrayList<>());
        }
        return robots;
    }

    public void addRobot(Robot robot) {
        List<Robot> updatedRobots = robots.getValue();
        updatedRobots.add(robot);
        robots.setValue(updatedRobots);
    }

    public void updateRobot(String ip) {
        List<Robot> updatedRobots = robots.getValue();
        Robot updatedRobot = getRobotWithIp(ip);
        updatedRobot.updateState();
        if (updatedRobot.getTables().size() == 0) {
            updatedRobot.updateTables();
        }
        updatedRobots.add(updatedRobot);
        robots.postValue(updatedRobots);
    }

    public Robot getRobotWithIp(String ip) {
        for (Robot robot : robots.getValue()) {
            if (robot.getIp().equals(ip)) {
                return robot;
            }
        }
        return null;
    }

}