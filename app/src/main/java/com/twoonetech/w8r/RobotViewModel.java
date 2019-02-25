package com.twoonetech.w8r;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

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

    //private MutableLiveData<String> liveRobotData = new MutableLiveData<>();
    private LiveData<Robot> robot;
    private RobotRepository robotRepo;

    public RobotViewModel(RobotRepository robotRepo) {
        this.robotRepo = robotRepo;
    }

    public void init(String ip){
        if (robot != null) {
            // ViewModel is created on a per-Fragment basis, so the robot doesn't change.
            return;
        }
        robot = robotRepo.getRobot(ip);
    }

    public LiveData<Robot> getRobot(){
        return robot;
    }

//    private static String[] parseJson(String jsonString) {
//
//        String type = "";
//        String stateType = "";
//        String timeStamp = "";
//        String message = "";
//
//        String[] resultString;
//
//        try {
//            JSONObject jsonObject = new JSONObject(jsonString);
//            type = jsonObject.get("type").toString();
//            timeStamp = jsonObject.get("timestamp").toString();
//            JSONObject arguments = jsonObject.getJSONObject("arguments");
//            stateType = arguments.getString("name");
//            message = arguments.getString("args");
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        resultString = new String[]{type, stateType, timeStamp, message};
//
//        return resultString;
//    }
}
