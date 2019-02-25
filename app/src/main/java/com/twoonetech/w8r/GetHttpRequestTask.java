package com.twoonetech.w8r;

import android.os.AsyncTask;

public class GetHttpRequestTask extends AsyncTask<String,Void,String>(){

    @Override
    protected String doInBackground(String... args) {

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
            //Get information out of result and put into robot class and then into robot live data and then return it

            JSONObject robotJson = null;
            try {
            robotJson = new JSONObject(result);
            } catch (JSONException e) {
            e.printStackTrace();
            }

            Robot robot = new Robot(ip);
            liveRobot.postValue(robot);
            }
}