package com.twoonetech.w8r;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RobotAPI {

    public static JSONObject request(String ip, String command, String[] args_names,
                                                    String[] args_values) {
        String datetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)
                .format(new Date());
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
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()));
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