package com.twoonetech.w8r;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.Arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RobotViewModel extends ViewModel {

    private final MutableLiveData<Robot> liveRobot = new MutableLiveData<Robot>();

    public LiveData<Robot> getLiveRobot() {
        return this.liveRobot;
    }

    public void init(String ip) {
        Robot robot = new Robot(ip);
        liveRobot.setValue(robot);
    }

    private String requestStatus(String ip){
        RobotAPI api = new RobotAPI();
        JSONObject response = api.request(ip, "get_status",
                new String[]{}, new String[]{});
        try {
            return response.getJSONObject("output").getString("status");
        }
        catch (Exception e){
            return "unknown";
        }
    }

    public List<String> requestTables(String ip){
        RobotAPI api = new RobotAPI();
        JSONObject response = api.request(ip, "get_tables",
                new String[]{}, new String[]{});
        try {
            String[] tables = response.getString("output").split(" ");
            return Arrays.asList(tables);
        }
        catch (Exception e){
            return Arrays.asList();
        }
    }

    public void goToTable(int tableNumber) {
    }

    public void update() {
        Robot robot = liveRobot.getValue();
        robot.setState(this.requestStatus(robot.getIp()));
        if (this.requestTables(robot.getIp()).size() == 0) {
            robot.setTables(this.requestTables(robot.getIp()));
        }
    }




//    public void getMap(String ip){
//
//    }

    //Get information out of result and put into robot class and then into robot live data and then return it
//            JSONObject robotJson = null;
//            try {
//                robotJson = new JSONObject((String) result);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            Robot robot = new Robot(ip);
//            liveRobot.postValue(robot);

}