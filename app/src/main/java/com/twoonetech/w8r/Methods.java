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
import java.util.Date;
import java.util.Locale;

public class Methods {

//    public void updateStatus(String robotIp,String appId){
//        JSONObject response = httpRequest(robotIp,appId,"get_status", new String[]{}, new String[]{});
//        if (response != null) {
//            try {
//                state = response.getJSONObject("output").getString("status");
//                currentEdge = response.getJSONObject("output").getString("edge");
//            } catch (Exception e) {
//                Log.d("Robot", e.getLocalizedMessage());
//            }
//        } else {
//            state = null;
//            currentEdge = null;
//        }
//    }

    public String goToTable(String robotIp, String appId, String tableId) {
        JSONObject response = httpRequest(robotIp,appId,"go_to_table", new String[]{"id"}, new String[]{tableId}); //change id to tableId
        if (response != null) {
            try {
                return response.getString("output");
            } catch (Exception e) {
                Log.e("Robot", "Failed to go to table", e);
            }
        }
        return null;
    }

    public String register(String robotIp, String appId) {
        //Add password
        JSONObject response = httpRequest(robotIp,appId,"register",new String[]{"appId","password"},new String[]{appId,appId});
        if (response != null) {
            try {
                return response.getString("output");
            } catch (Exception e) {
                Log.e("Robot", "Failed to register",e);
            }
        }
        return null;
    }

    public String assignMap(String robotIp, String appId, String mapJson) {
        JSONObject response = httpRequest(robotIp, appId,"assign_map",new String[]{"map_json"},new String[]{MapConverter.convert(mapJson)});
        if (response != null) {
            try {
                return response.getString("output");
            } catch (Exception e) {
                Log.e("Robot","Failed to assign map",e);
            }
        }
        return null;
    }

    public JSONObject httpRequest(String robotIp, String appId, String commandName, String[] argsNames, String[] argsValues) {
        try {
            String datetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).format(new Date());
            JSONObject arguments = new JSONObject().put("name", commandName);
            for (int i = 0; i < argsNames.length; i++) {
                arguments.put(argsNames[i], argsValues[i]);
            }
            JSONObject request = new JSONObject().put("timestamp", datetime).put("appId",appId).put("arguments", arguments);
            //This encodes the result string to UTF-8, so that it can be received correctly by the Pi.
            Log.d("Robot Command", request.toString());
            String encodedJsonString = URLEncoder.encode(request.toString(), "UTF-8");
            URL url = new URL("http://"+robotIp+":5000/data?json=" + encodedJsonString);
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
            Log.e("Methods","HTTP message or response could not be parsed");
            return null;
        } catch (ConnectException e) {
            Log.e("Methods","HTTP connection could not be established");
            return null;
        } catch (IOException e) {
            Log.e("Methods","Failed to read HTTP response");
            return null;
        }
    }
}
