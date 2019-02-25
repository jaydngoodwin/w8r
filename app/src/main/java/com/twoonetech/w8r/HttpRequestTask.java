package com.twoonetech.w8r;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestTask extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String... params) {
        String urn = params[0];

        try {
            URL url = new URL(urn);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            // setting the  Request Method Type
            httpURLConnection.setRequestMethod("POST");
            // adding the headers for request
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            try{
                //to tell the connection object that we will be writing some data on the server and then will fetch the output result
                httpURLConnection.setDoOutput(true);
                // this is used for just in case we don't know about the data size associated with our request
                httpURLConnection.setChunkedStreamingMode(0);
                // to log the response code of your request
                Log.d("HttpRequestTask", "HttpRequestTask doInBackground : " +httpURLConnection.getResponseCode());
                // to log the response message from your server after you have tried the request.
                Log.d("HttpRequestTask", "HttpRequestTask doInBackground : " +httpURLConnection.getResponseMessage());

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                // this is done so that there are no open connections left when this task is going to complete
                httpURLConnection.disconnect();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}