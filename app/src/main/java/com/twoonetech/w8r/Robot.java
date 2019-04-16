package com.twoonetech.w8r;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Robot {

    private String ip;
    private String name;
    private String state = "idle";
    private String mapJson;
    private String currentEdge;
    private String currentDestination;
    private List<String> log = new ArrayList<>();
    private String appId;

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

    public String getCurrentEdge() {
        return this.currentEdge;
    }

    public void setCurrentEdge(String edgeName) {
        this.currentEdge = edgeName;
    }

    public String getMapJson() {
        return mapJson;
    }

    public void setMapJson(String mapJson) {
        this.mapJson = mapJson;
    }

    public List<String> getLog() {
        return log;
    }

    public void setLog(List<String> log) {
        this.log = log;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void updateStatus(){
        JSONObject response = httpRequest("get_status", new String[]{}, new String[]{});
        if (response != null) {
            try {
                state = response.getJSONObject("output").getString("status");
                currentEdge = response.getJSONObject("output").getString("edge");
            } catch (Exception e) {
                Log.d("Robot", e.getLocalizedMessage());
            }
        } else {
            state = null;
            currentEdge = null;
        }
    }

    public String goToTable(String tableId) {
        JSONObject response = httpRequest("go_to_table", new String[]{"id"}, new String[]{tableId}); //change id to tableId
        if (response != null) {
            try {
                return response.getString("output");
            } catch (Exception e) {
                Log.e("Robot", "Failed to go to table", e);
            }
        }
        return null;
    }

    public String register(String appId) {
        JSONObject response = httpRequest("register",new String[]{"appId","password"},new String[]{appId,appId});
        if (response != null) {
            try {
                return response.getString("output");
            } catch (Exception e) {
                Log.e("Robot", "Failed to register",e);
            }
        }
        return null;
    }

    public String assignMap(String mapJson) {
        JSONObject response = httpRequest("assign_map",new String[]{"map_json"},new String[]{MapConverter.convert(mapJson)});
        if (response != null) {
            try {
                return response.getString("output");
            } catch (Exception e) {
                Log.e("Robot","Failed to assign map",e);
            }
        }
        return null;
    }

    private JSONObject httpRequest(String command, String[] args_names, String[] args_values) {
        try {
            String datetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).format(new Date());
            JSONObject arguments = new JSONObject().put("name", command);
            for (int i = 0; i < args_names.length; i++) {
                arguments.put(args_names[i], args_values[i]);
            }
            JSONObject request = new JSONObject().put("timestamp", datetime).put("appId",appId).put("arguments", arguments);
            //This encodes the result string to UTF-8, so that it can be received correctly by the Pi.
            Log.d("Robot Command", request.toString());
            String encodedJsonString = URLEncoder.encode(request.toString(), "UTF-8");
            URL url = new URL("http://"+ip+":5000/data?json=" + encodedJsonString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(2000);

            String responseString = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                responseString += line + "\n";
            }
            return new JSONObject(responseString);
        } catch (JSONException e) {
            Log.e("Robot","HTTP message or response could not be parsed");
            return null;
        } catch (ConnectException e) {
            Log.e("Robot","HTTP connection could not be established");
            return null;
        } catch (IOException e) {
            Log.e("Robot","Failed to read HTTP response");
            return null;
        }
    }
}
