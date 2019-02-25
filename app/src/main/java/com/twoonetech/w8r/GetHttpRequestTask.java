package com.twoonetech.w8r;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GetHttpRequestTask extends AsyncTask<String,Void,String> {

    private OnTaskCompleted listener;

    public GetHttpRequestTask(OnTaskCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... args) {

        String urn = args[0];

        String datetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).format(new Date());

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject().put("timestamp",datetime).put("command","get_status").put("args",new JSONArray(""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonString = jsonObject.toString();

        try {
            //This encodes the result string to UTF-8, so that it can be received correctly by the Pi.
            String encodedJsonString = URLEncoder.encode(jsonString, "UTF-8");
            String urn = "http://" + ip + ":5000/data?json=" + encodedJsonString;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BufferedReader reader = null;

        try {
            URL url = new URL(urn);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    //The String that is returned in the doInBackground() method is sent to the onPostExecute() method below.
    //The String should contain JSON data.
    @Override
    protected void onPostExecute(String result) {
        listener.onTaskCompleted(result);
    }
}