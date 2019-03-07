package com.twoonetech.w8r;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SharedViewModel extends AndroidViewModel {

    public SharedViewModel(Application application){
        super(application);
    }

    private SharedPreferences robotsPrefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
    private MutableLiveData<List<Robot>> robots;

    public void init() {
        //take from shared prefs and put into shared prefs
        robots = new MutableLiveData<>();
        List<Robot> robots = new ArrayList<>();
        Map<String,String> ipNameMap = (Map<String,String>) robotsPrefs.getAll();
        Iterator it = ipNameMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            robots.add(new Robot((String) pair.getKey(),(String) pair.getValue()));
            it.remove(); // avoids a ConcurrentModificationException
        }
        this.robots.setValue(robots); //set since this will NOT be in a background thread
    }

    public void updateRobots() {
        //call update on each robot in the list
        List<Robot> robots = this.robots.getValue();
        for (Robot robot : robots) {
            robot.updateState();
            robot.updateTables(); //add a check here so it doesnt repeat
        }
        this.robots.postValue(robots);
    }

    public void addRobot(String ip, String name) {
        //take in ip?
        //add to shared prefs
        //register?
        //use new AsyncTask<>() ?
        robotsPrefs.edit().putString(ip, name.toString()).apply();
        Robot robot = new Robot(ip,name);
        String registerResponse = robot.register(Settings.Secure.getString(getApplication().getContentResolver(), Settings.Secure.ANDROID_ID));
        if (registerResponse == "ok") {
            List<Robot> robots = this.robots.getValue();
            robots.add(robot);
            this.robots.postValue(robots); //post since this will be in a background thread
        }
    }

    public LiveData<List<Robot>> getRobots() {
        return robots;
    }

    public Robot getRobotWithIp(String ip) {
        for (Robot robot : robots.getValue()) {
            if (robot.getIp().equals(ip)) {
                return robot;
            }
        }
        return null;
    }

//    public void init() {
//        if (robots == null) {
//            robots = new MutableLiveData<>();
//            robots.setValue(new ArrayList<>());
//        }
//    }
//
//    public MutableLiveData<List<Robot>> getRobots() {
//        if (robots == null) {
//            robots = new MutableLiveData<>();
//            robots.setValue(new ArrayList<>());
//        }
//        return robots;
//    }
//
//    public void addRobot(Robot robot) {
//        List<Robot> updatedRobots = robots.getValue();
//        updatedRobots.add(robot);
//        robots.postValue(updatedRobots);
//    }
//
//    public void updateRobot(String ip) {
//        List<Robot> updatedRobots = robots.getValue();
//        Robot updatedRobot = getRobotWithIp(ip);
//        updatedRobot.updateState();
//        if (updatedRobot.getTables().size() == 0) {
//            updatedRobot.updateTables();
//        }
//        updatedRobots.add(updatedRobot);
//        robots.postValue(updatedRobots);
//    }
//
//    public Robot getRobotWithIp(String ip) {
//        for (Robot robot : robots.getValue()) {
//            if (robot.getIp().equals(ip)) {
//                return robot;
//            }
//        }
//        return null;
//    }

}