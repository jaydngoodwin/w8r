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

    //liveRobotData is a live mapping of the robot ips that are on the same network with their status (in JSONString form)
    //private MutableLiveData<String> liveRobotData = new MutableLiveData<>();
    private LiveData<Robot> robot;
    private RobotRepository robotRepo;

    public RobotViewModel(RobotRepository robotRepo) {
        this.robotRepo = robotRepo;
    }

    public void init(String ip){
        if (this.robot != null) {
            // ViewModel is created on a per-Fragment basis, so the robot doesn't change.
            return;
        }
        robot = robotRepo.getRobot(ip);
    }

    public LiveData<Robot> getRobot(){
        return robot;
    }

    /** This method takes the input text from the text field and converts it to a JSONString. The
     * return type of the method is an array of two Strings: JSONString you sent, and the return
     * String from the server.
     *
     * @author Kiril, Aristide, Jaydn
     * @since 31/01/2019
     * @param ip The ip of the robot being sent the command
     * @param command Name of the called method. E.g (moveToTable:).
     * @param args The arguments given to the method. E.g. (moveToTable, ["5","4"]).
     */

    @SuppressLint("StaticFieldLeak")
    public void sendCommand(String ip, String command, String[] args) {

        String datetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).format(new Date());

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject()
                    .put("timestamp", datetime)
                    .put("data", new JSONObject()
                            .put("command", command)
                            .put("args", new JSONArray(args)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonString = jsonObject.toString();

        try {
            //This encodes the result string to UTF-8, so that it can be received correctly by the Pi.
            String encodedJsonString= URLEncoder.encode(jsonString, "UTF-8" );
            String urn = "http://"+ip+":5000/data?json="+encodedJsonString;

            //Instantiation of the Async task that’s defined below
//            RequestDataAsync requestDataAsync = new RequestDataAsync();
//            requestDataAsync.execute(URL);

            new AsyncTask<Void,Void,String>(){
                @Override
                protected String doInBackground(Void... voids) {

                    BufferedReader reader = null;

                    try {
                        URL url = new URL(urn);
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

                //The String that is returned in the doInBackground() method is sent to the onPostExecute() method below.
                //The String should contain JSON data.
                @Override
                protected void onPostExecute(String result) {
                    //parseJson(result);
                    liveRobotData.postValue(result);
                }
            };
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static String[] parseJson(String jsonString) {

        String type = "";
        String stateType = "";
        String timeStamp = "";
        String message = "";

        String[] resultString;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            type = jsonObject.get("type").toString();
            timeStamp = jsonObject.get("timestamp").toString();
            JSONObject arguments = jsonObject.getJSONObject("arguments");
            stateType = arguments.getString("name");
            message = arguments.getString("args");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        resultString = new String[]{type, stateType, timeStamp, message};

        return resultString;
    }
}
