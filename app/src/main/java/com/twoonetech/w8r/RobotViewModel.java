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

    private final MutableLiveData<Robot> liveRobot = new MutableLiveData<>();

    public LiveData<Robot> getLiveRobot() {
        return liveRobot;
    }

    public void init(String ip) {
        Robot robot = new Robot(ip);
        liveRobot.setValue(robot);
        update();
    }

    public void update() {
        Robot robot = liveRobot.getValue();
        robot.setState(this.requestStatus(robot.getIp()));
        if (this.requestTables(robot.getIp()).size() == 0) {
            robot.setTables(this.requestTables(robot.getIp()));
        }
        liveRobot.postValue(robot);
    }

    public void goToTable(String tableId) {
        Robot robot = liveRobot.getValue();
        JSONObject response = httpRequest(robot.getIp(), "go_to_table", new String[]{"id"}, new String[]{tableId});
    }

    private String requestStatus(String ip){
        JSONObject response = httpRequest(ip, "get_status", new String[]{}, new String[]{});
        try {
            return response.getJSONObject("output").getString("status");
        }
        catch (Exception e){
            return e.getLocalizedMessage();
        }
    }

    private List<String> requestTables(String ip){
        JSONObject response = httpRequest(ip, "get_tables", new String[]{}, new String[]{});
        try {
            String[] tables = response.getString("output").split(" ");
            return Arrays.asList(tables);
        }
        catch (Exception e){
            return Arrays.asList(e.getLocalizedMessage());
        }
    }

    private static JSONObject httpRequest(String ip, String command, String[] args_names, String[] args_values) {
        String datetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).format(new Date());
        JSONObject response = null;
        try {
            JSONObject arguments = new JSONObject()
                    .put("name", command);
            for (int i = 0; i < args_names.length; i++) {
                arguments.put(args_names[i], args_values[i]);
            }
            JSONObject request = new JSONObject()
                    .put("timestamp", datetime)
                    .put("arguments", arguments);
            //This encodes the result string to UTF-8, so that it can be received
            // correctly by the Pi.
            Log.d("hi", request.toString());
            String encodedJsonString = URLEncoder.encode(request.toString(), "UTF-8");
            URL url = new URL("http://" + ip + ":5000/data?json=" + encodedJsonString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");  //timeout?

            String responseString = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                responseString += line + "\n";
            }
            response = new JSONObject(responseString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
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