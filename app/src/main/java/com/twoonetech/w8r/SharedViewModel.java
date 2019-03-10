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
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SharedViewModel extends ViewModel {

//    public SharedViewModel(Application application){
//        super(application);
//    }

    public MutableLiveData<List<Robot>> robots = new MutableLiveData<>(); //by default

//    public void init() {
//        //take from shared prefs and put into shared prefs
//        robots = new MutableLiveData<>();
//        List<Robot> robots = new ArrayList<>();
//        Map<String,String> ipNameMap = (Map<String,String>) robotsPrefs.getAll();
//        Iterator it = ipNameMap.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//            robots.add(new Robot((String) pair.getKey(),(String) pair.getValue()));
//            it.remove(); // avoids a ConcurrentModificationException
//        }
//        this.robots.setValue(robots); //set since this will NOT be in a background thread
//    }

//    public void updateRobots() {
//        //call update on each robot in the list
//        List<Robot> robots = this.robots.getValue();
//        for (Robot robot : robots) {
//            robot.updateState();
//            if (robot.getTables() == null) {
//                robot.updateTables();
//            }
//        }
//        this.robots.postValue(robots);
//    }

//    public void addRobot(String ip, String name) {
//        Robot robot = new Robot(ip,name);
//        if (getRobotWithIp(ip) == null) {
//            String registerResponse = robot.register(Settings.Secure.getString(getApplication().getContentResolver(), Settings.Secure.ANDROID_ID));
//            if (registerResponse != null && registerResponse.equals("ok")) {
//                robotsPrefs.edit().putString(ip, name.toString()).apply();
//                List<Robot> robots = this.robots.getValue();
//                robots.add(robot);
//                this.robots.postValue(robots); //post since this will be in a background thread
//            }
//        }
//    }

    public MutableLiveData<List<Robot>> getLiveRobots() {
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

}