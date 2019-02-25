package com.twoonetech.w8r;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.json.JSONException;
import org.json.JSONObject;

public class RobotRepository {

    @SuppressLint("StaticFieldLeak")
    public LiveData<Robot> getRobot(String ip) {

        final MutableLiveData<Robot> liveRobot = new MutableLiveData<>();

//        String datetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).format(new Date());
//
//        JSONObject jsonObject = null;
//
//        try {
//            jsonObject = new JSONObject().put("timestamp",datetime).put("command","get_status").put("args",new JSONArray(""));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        String jsonString = jsonObject.toString();
//
//        try {
//            //This encodes the result string to UTF-8, so that it can be received correctly by the Pi.
//            String encodedJsonString= URLEncoder.encode(jsonString, "UTF-8" );
//            String urn = "http://"+ip+":5000/data?json="+encodedJsonString;
//
//            RobotAPI robotAPI = new RobotAPI(result -> {
//                //Get information out of result and put into robot class and then into robot live data and then return i
//                JSONObject robotJson = null;
//                try {
//                    robotJson = new JSONObject((String) result);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                Robot robot = new Robot(ip);
//                liveRobot.postValue(robot);
//            });
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        RobotAPI robotAPI = new RobotAPI(result -> {
            //Get information out of result and put into robot class and then into robot live data and then return i
            JSONObject robotJson = null;
            try {
                robotJson = new JSONObject((String) result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Robot robot = new Robot(ip);
            liveRobot.postValue(robot);
        });

        return liveRobot;
    }
}