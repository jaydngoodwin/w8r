package com.twoonetech.w8r;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RobotFragment extends Fragment {

    private Context mContext;

    private String ip;
    private RobotViewModel robotViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        ip = getArguments().getString("ip");
        robotViewModel = ViewModelProviders.of(this).get(RobotViewModel.class);
        robotViewModel.init(ip);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_robot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        //Get views

        //Observe live data from robots view model
        robotViewModel.getLiveRobot().observe(this, robot -> {
            if (robot != null) {
                //If the robot data has been fetched from the database
            } else {
                //If the robot data has not been fetched from the database
            }
        });

//        Button getStatus = view.findViewById(R.id.get_status);
//        getStatus.setOnClickListener(view1 -> {
//            robotViewModel.sendCommand("192.168.105.149","get_status",new String[]{});
//        });
//
//        Button getMap = view.findViewById(R.id.get_map);
//        getStatus.setOnClickListener(view1 -> {
//            robotViewModel.sendCommand("192.168.105.149","get_map",new String[]{});
//        });
//
//        Button getTables = view.findViewById(R.id.get_tables);
//        getTables.setOnClickListener(view1 -> {
//            robotViewModel.sendCommand("192.168.105.149","get_tables",new String[]{});
//        });

        Button goToTable = view.findViewById(R.id.go_to_table);
        goToTable.setOnClickListener(view1 -> {
            //robotViewModel.sendCommand("192.168.105.149","go_to_table",new String[]{});
            String datetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).format(new Date());

            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject()
                        .put("timestamp", datetime)
                        .put("data", new JSONObject()
                                .put("command", "go_to_table")
                                .put("args", new JSONArray("2")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String jsonString = jsonObject.toString();

            try {
                //This encodes the result string to UTF-8, so that it can be received correctly by the Pi.
                String encodedJsonString = URLEncoder.encode(jsonString, "UTF-8");
                String urn = "http://" + ip + ":5000/data?json=" + encodedJsonString;
                PostHttpRequestTask postHttpRequestTask = new PostHttpRequestTask();
                postHttpRequestTask.execute(urn);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        Button test = view.findViewById(R.id.test);
        test.setOnClickListener(view1 -> {

        });
    }

}