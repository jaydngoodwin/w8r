package com.twoonetech.w8r;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RobotsViewModel extends ViewModel {

    //liveRobotData is a live mapping of the robot ips that are on the same network with their status (in JSONString form)
    private MutableLiveData<Map<String,String>> liveRobotData;

    public LiveData<Map<String,String>> getLiveRobotData(){
        if (liveRobotData == null) {
            //If the live data is null, instantiate it with a new MutableLiveData and set it's value to null
            liveRobotData = new MutableLiveData<>();
            liveRobotData.setValue(null);
        }
        return liveRobotData;
    }

    public void updateRobotData(Map<String,String> robotData){
        //Update the local robot data
        liveRobotData.setValue(robotData);
    }

    //Use DHCP to get ip address of all devices on network that ARE robots and put it into the live list
//    public List<String> searchForRobots(){
//        List<String> ips = new ArrayList<String>(); //DO DHCP STUFF HERE
//        liveRobotData.postValue(ips); //Put updated list of ips into the live list
//        return ips;
//    }

    /** This method takes the input text from the text field and converts it to a JSONString. The
     * return type of the method is an array of two Strings: JSONString you sent, and the return
     * String from the server.
     *
     * @author Kiril, Aristide, Jaydn
     * @since 31/01/2019
     * @param ip The ip of the robot being sent the command
     * @param name Name of the called method. E.g (moveToTable:).
     * @param args The arguments given to the method. E.g. (moveToTable, ["5","4"]).
     */

    private void sendCommand(String ip, String name, String[] args) {

        String datetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject()
                    .put("timestamp", datetime)
                    .put("data", new JSONObject()
                            .put("name", name)
                            .put("args", new JSONArray(args)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonString = jsonObject.toString();

        try {
            //This encodes the result string to UTF-8, so that it can be received correctly by the Pi.
            String encodedJsonString= URLEncoder.encode(jsonString, "UTF-8" );

            //Instantiation of the Async task that’s defined below
            RequestDataAsync requestDataAsync = new RequestDataAsync();
            requestDataAsync.execute(ip,encodedJsonString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private class RequestDataAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            BufferedReader reader = null;

            String ip = params[0];
            String encodedJsonString = params[1];

            String uri = "http://"+ip+":5000/data?json="+encodedJsonString;

            try {
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

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

        //The String that is returned in the doInBackground() method is sent to the
        // onPostExecute() method below. The String should contain JSON data.
        @Override
        protected void onPostExecute(String result) {
            //Result will contain ip address of robot as well as its status
        }
    }

}
