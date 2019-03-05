package com.twoonetech.w8r;

import android.provider.Settings;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Robot {

    private String ip;
    private String name;
    private String state;
    private String currentPosition;
    private String destination;
    private List<String> tables;
    private String appId;

    public Robot(String ip) {
        this.ip = ip;
    }

    public Robot(String ip, String name) {
        this.ip = ip;
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public void goToTable(String tableId) {
        httpRequest("go_to_table", new String[]{"id"}, new String[]{tableId}); //change id to tableId
    }

    public void returnToBar() {
        httpRequest("stop",new String[]{}, new String[]{});
    }

    public void updateState(){
        JSONObject response = httpRequest("get_status", new String[]{}, new String[]{});
        try {
            state = response.getJSONObject("output").getString("status");
        }
        catch (Exception e){
            Log.d("Robot",e.getLocalizedMessage());
        }
    }

    public String register(String appId) {
        this.appId = appId;
        //JSONObject response = httpRequest("register",new String[]{"appId"},new String[]{appId});;
        //return response.toString();
        return "registered_successfully";
    }

    public void updateTables(){
        JSONObject response = httpRequest("get_tables", new String[]{}, new String[]{});
        try {
            tables = Arrays.asList(response.getString("output").split(" "));
        }
        catch (Exception e){
            Log.d("Robot",e.getLocalizedMessage());
        }
    }

    private JSONObject httpRequest(String command, String[] args_names, String[] args_values) {
        String datetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).format(new Date());
        JSONObject response = null;
        try {
            JSONObject arguments = new JSONObject().put("name", command);
            for (int i = 0; i < args_names.length; i++) {
                arguments.put(args_names[i], args_values[i]);
            }
            JSONObject request = new JSONObject().put("timestamp", datetime).put("appId",appId).put("arguments", arguments);
            //This encodes the result string to UTF-8, so that it can be received correctly by the Pi.
            Log.d("hi", request.toString());
            String encodedJsonString = URLEncoder.encode(request.toString(), "UTF-8");
            URL url = new URL("http://"+ip+":5000/data?json=" + encodedJsonString);
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
}
