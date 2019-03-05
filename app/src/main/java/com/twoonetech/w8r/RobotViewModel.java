package com.twoonetech.w8r;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.Locale;

public class RobotViewModel extends ViewModel {

    private MutableLiveData<Robot> liveRobot = new MutableLiveData<>();

    public LiveData<Robot> getLiveRobot() {
        return liveRobot;
    }

    public void init(String ip) {
        liveRobot.setValue(new Robot(ip));
    }

    public void update() {
        Robot robot = liveRobot.getValue();
        //Long startTime = System.currentTimeMillis();
        robot.updateState();
        if (robot.getTables().size() == 0) {
            robot.updateTables();
        }
        //Long updateTime = System.currentTimeMillis();
        //Log.d("RobotUpdate",String.valueOf((updateTime/1000.0)-(startTime/1000.0)));
        liveRobot.postValue(robot);
    }


//    public void getMap(String ip){
//
//    }

}