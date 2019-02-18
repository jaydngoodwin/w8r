package com.twoonetech.w8r;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Methods {

    /** This method takes the input text from the text field and converts it to a JSONString. The
     * return type of the method is an array of two Strings: JSONString you sent, and the return
     * String from the server.
     *
     * @author Kiril, Aristide
     * @since 31/01/2019
     * @param username The username of the waiter calling a commands.
     * @param name Name of the called method. E.g (moveToTable:).
     * @param type Type of the message ("Commands, "Debug", etc).
     * @param args The arguments given to the method. E.g. (moveToTable: 5,4).
     * @return jsonString Return a the JSONString.
     */

    public static String[] sendCommand(String username, String name, String type, String[] args) {

        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String format = s.format(new Date());

        JSONObject jsonString = null;

        try {
            jsonString = new JSONObject()
                    .put("user", username )
                    .put("type", type)
                    .put("timestamp", format)
                    .put("arguments", new JSONObject()
                            .put("name", name)
                            .put("args", new JSONArray(args)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String result = jsonString.toString();

        Methods methods = new Methods();
        methods.requestData("http://192.168.105.149:5000/data?json=" + encode(result));

        return new String[]{result};

    }

    /**
     * This method is used to request data from the PI.
     * @param url
     */
    private void requestData(String url) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUrl(url);

        Downloader downloader = new Downloader(); //Instantiation of the Async task
        //that’s defined below

        downloader.execute(requestPackage);
    }

    private class Downloader extends AsyncTask<RequestPackage, String, String> {
        @Override
        protected String doInBackground(RequestPackage... params) {
            return HttpManager.getData(params[0]);
        }

        //The String that is returned in the doInBackground() method is sent to the
        // onPostExecute() method below. The String should contain JSON data.
        @Override
        protected void onPostExecute(String result) {

        }
    }


    /**
     * This method encodes the result string to UTF-8, so that it can be
     * received correctly by the Pi.
     * @param result
     * @return
     */
    public static String encode(String result)
    {
        try {
            String encodeURL=URLEncoder.encode( result, "UTF-8" );
            return encodeURL;
        } catch (UnsupportedEncodingException e) {
            return "Issue while encoding" +e.getMessage();
        }
    }

    /** This method parses the JSONString and divides its arguments to several substrings.
     *
     * type -> The type of the command given. E.g: commands, debug, etc.
     * stateType -> name of the method which will be called in remote.py
     * timeStamp -> the date and time the command has been send
     * message -> arguments to the method (if there are any).
     *
     * @since 31/01/2019
     * @author Kiril, Aristide
     * @param jsonString
     * @return resultString;
     */

    public static String[] parseJson(String jsonString) {

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
