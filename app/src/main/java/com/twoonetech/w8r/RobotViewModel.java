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

    public void updateLiveRobot(Robot robot) {
        liveRobot.setValue(robot);
    }


    public void getStatus(String ip){
        GetHttpRequestTask getHttpRequestTask = new GetHttpRequestTask(ip,
        GetHttpRequestTask getHttpRequestTask = new GetHttpRequestTask(ip,
    }

    public void getTables(String ip){

    }

    public void goToTable(int tableNumber) {

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