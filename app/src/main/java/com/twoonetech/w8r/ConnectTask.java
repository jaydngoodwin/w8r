package com.twoonetech.w8r;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * The connect task gets a string argument from the MainActivity input text field and passes it
 * to the server.py on the Raspberry Pi. The whole process runs in the background since the task
 * is AsynchTask.
 *
 * @author Kiril, Aristide
 * @since  28/01/2019
 */

public class ConnectTask extends AsyncTask<String, Integer, Long> {

    /**
     * This method takes an argument of type String from the MainAcitvity, opens a socket to the
     * Pi, sends the string and finally closes the socket.
     *
     * @param strings
     *
     */

    public static String response;

    protected Long doInBackground(String... strings) {

       response = "No response from server.";

        try {
            String sentence = strings[0];
            Log.d("Parsed argument", sentence);

            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            Socket clientSocket = new Socket("192.168.105.149", 9999);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToServer.writeUTF(sentence);
            response = inFromServer.readLine();
            System.out.println("FROM SERVER: " + response);
            clientSocket.close();


        } catch (Exception exception) {
            System.err.println("JavaClient: " + exception);
        }
        return null;



    }


    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(Long result) {
        Log.d("Finished", "Execution finished!");
    }
}
