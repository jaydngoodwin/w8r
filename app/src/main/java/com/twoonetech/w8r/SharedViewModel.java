package com.twoonetech.w8r;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<List<Robot>> robots = new MutableLiveData<>(); //by default
    private MutableLiveData<Map<String,String>> maps = new MutableLiveData<>(); //by default

    public MutableLiveData<List<Robot>> getLiveRobots() {
        return robots;
    }

    //Because robots isn't a map and each robot identifier is within the Robot class
    public Robot getRobotWithIp(String ip) {
        for (Robot robot : robots.getValue()) {
            if (robot.getIp().equals(ip)) {
                return robot;
            }
        }
        return null;
    }

    public MutableLiveData<Map<String,String>> getLiveMaps() {
        return maps;
    }

}