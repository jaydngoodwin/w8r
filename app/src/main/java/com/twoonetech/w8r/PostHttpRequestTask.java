package com.twoonetech.w8r;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostHttpRequestTask extends AsyncTask<String,Void,Void> {

    @Override
    protected Void doInBackground(String... args) {
        String urn = args[0];
        String command = args[1];

        try {
            URL url = new URL(urn);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            // setting the  Request Method Type
            httpURLConnection.setRequestMethod("POST");

            try{
                //to tell the connection object that we will be writing some data on the server and then will fetch the output result
                httpURLConnection.setDoOutput(true);
                // this is used for just in case we don't know about the data size associated with our request
                httpURLConnection.setChunkedStreamingMode(0);

                return null;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }finally {
                // this is done so that there are no open connections left when this task is going to complete
                httpURLConnection.disconnect();
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}