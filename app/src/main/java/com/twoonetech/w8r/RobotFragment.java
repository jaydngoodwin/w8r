package com.twoonetech.w8r;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RobotFragment extends Fragment {

    private String ip;
    private SharedViewModel model;
    private FragmentActivity mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (FragmentActivity) context;
        model = ViewModelProviders.of(mContext).get(SharedViewModel.class);
        ip = getArguments().getString("ip");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_robot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        FragmentManager fm = getFragmentManager();

        ImageView state = view.findViewById(R.id.state);
        TextView addMapPrompt = view.findViewById(R.id.add_map_prompt);
        TextView inactiveRobotTextMap = view.findViewById(R.id.inactive_robot_text_map);
        TextView inactiveRobotTextLog = view.findViewById(R.id.inactive_robot_text_log);
        ListView log = view.findViewById(R.id.log);

        ArrayAdapter<String> logAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, model.getRobotWithIp(ip).getLog());

        // Set the ArrayAdapter as the ListView's adapter.
        log.setAdapter(logAdapter);

        //Observe live data from robots view model
        model.getLiveRobots().observe(this, robots -> {
            if (model.getRobotWithIp(ip).getState() == null) {
                state.setBackgroundResource(R.mipmap.inactive_status);
                inactiveRobotTextLog.setVisibility(View.VISIBLE);
                inactiveRobotTextMap.setVisibility(View.VISIBLE);
                addMapPrompt.setVisibility(View.INVISIBLE);
                if (fm.findFragmentById(R.id.map_container) != null) {
                    fm.beginTransaction().remove(fm.findFragmentById(R.id.map_container)).commit();
                }
                if (model.getRobotWithIp(ip).getMapJson() != null) {
                    //DONT UNDERSTAND HOW THIS WORKS?
                    AsyncTask.execute(() -> {
                        String assignMapResponse = model.getRobotWithIp(ip).assignMap(model.getRobotWithIp(ip).getMapJson());
                        if (assignMapResponse != null && assignMapResponse.equals("ok")) {
                            Handler handler = new Handler(mContext.getMainLooper());
                            handler.post(() -> Toast.makeText(mContext, "Map succesfully assigned", Toast.LENGTH_SHORT).show());
                        }
                    });
                }
            } else {
                switch (model.getRobotWithIp(ip).getState()) {
                    case "idle":
                        state.setBackgroundResource(R.mipmap.idle_status);
                        break;
                    case "following":
                        state.setBackgroundResource(R.mipmap.serving_status);
                        break;
                    case "obstacle":
                        state.setBackgroundResource(R.mipmap.obstructed_status);
                        break;
                }
                inactiveRobotTextLog.setVisibility(View.INVISIBLE);
                inactiveRobotTextMap.setVisibility(View.INVISIBLE);
                if (model.getRobotWithIp(ip).getMapJson() == null) {
                    addMapPrompt.setVisibility(View.VISIBLE);
                } else {
                    addMapPrompt.setVisibility(View.INVISIBLE);
                    Bundle args = new Bundle();
                    args.putString("ip", ip);
                    MapFragment mapFragment = new MapFragment();
                    mapFragment.setArguments(args);
                    fm.beginTransaction().replace(R.id.map_container, mapFragment).commit();
                }
            }
        });

        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(view1 -> {
            fm.beginTransaction().replace(R.id.fragment_container,new RobotsFragment()).commit();
        });

        Button goToTable = view.findViewById(R.id.go_to_table);
        goToTable.setOnClickListener(view1 -> {
            if (model.getRobotWithIp(ip).getState() == null) {
                Toast.makeText(mContext, "Robot is not turned on",Toast.LENGTH_SHORT).show();
            } else if (model.getRobotWithIp(ip).getMapJson() == null) {
                Toast.makeText(mContext, "Robot is not assigned to a map",Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                b.setTitle("Choose table");
                String[] data = null;
                ArrayList<String> tableNamesArray = new ArrayList<>();
                try {
                    JSONArray nodesJsonArray = new JSONObject(model.getRobotWithIp(ip).getMapJson()).getJSONArray("nodes");
                    for (int i = 0; i < nodesJsonArray.length(); i++) {
                        if (nodesJsonArray.getJSONObject(i).getString("type").equals("table") && !nodesJsonArray.getJSONObject(i).getString("name").equals("Bar")) {
                            tableNamesArray.add(nodesJsonArray.getJSONObject(i).getString("name"));
                        }
                    }
                    data = tableNamesArray.toArray(new String[tableNamesArray.size()]);
                } catch (JSONException e) {
                    Log.e("CreateMapFragment", "Failed to get nodes");
                }
                b.setItems(data, (dialogInterface, i) -> {
                    AsyncTask.execute(() -> {
                        String goToTableResponse = model.getRobotWithIp(ip).goToTable(tableNamesArray.get(i));
                        if (goToTableResponse != null && goToTableResponse.equals("ok")) {
                            model.getRobotWithIp(ip).getLog().add("["+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).format(new Date())+"] Robot is going to table "+tableNamesArray.get(i));
                            getActivity().runOnUiThread(() -> logAdapter.notifyDataSetChanged());
                            Handler handler = new Handler(mContext.getMainLooper());
                            handler.post(() -> Toast.makeText(mContext, "Robot destination: table " + tableNamesArray.get(i), Toast.LENGTH_SHORT).show());
                        } else {
                            Handler handler = new Handler(mContext.getMainLooper());
                            handler.post(() -> Toast.makeText(mContext, "Failed to go to table", Toast.LENGTH_SHORT).show());
                        }
                    });
                });
                b.show();
            }
        });

        Button addMap = view.findViewById(R.id.add_map);
        addMap.setOnClickListener(view3 -> {
            if (model.getRobotWithIp(ip).getState() == null) {
                Toast.makeText(mContext, "Robot is not turned on", Toast.LENGTH_SHORT).show();
            } else {
                //SEND MAP TO ROBOT HERE
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Choose map");
                String[] data = model.getLiveMaps().getValue().keySet().toArray(new String[model.getLiveMaps().getValue().keySet().size()]);
                builder.setItems(data, (dialogInterface, i) -> {
                    AsyncTask.execute(() -> {
                        String assignMapResponse = model.getRobotWithIp(ip).assignMap(model.getLiveMaps().getValue().get(data[i]));
                        Handler handler = new Handler(mContext.getMainLooper());
                        if (assignMapResponse != null && assignMapResponse.equals("ok")) {
                            model.getRobotWithIp(ip).setMapJson(model.getLiveMaps().getValue().get(data[i])); //COULD PUT THIS IN ROBOT?
                            mContext.getSharedPreferences("RobotMapJsonsPrefs", Context.MODE_PRIVATE).edit().putString(ip, model.getLiveMaps().getValue().get(data[i])).apply();
                            model.getRobotWithIp(ip).getLog().add("["+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).format(new Date())+"] Robot has been assigned a new map");
                            getActivity().runOnUiThread(() -> logAdapter.notifyDataSetChanged());
                            addMapPrompt.setVisibility(View.INVISIBLE);
                            handler.post(() -> Toast.makeText(mContext, "Map successfully assigned", Toast.LENGTH_SHORT).show());
                        } else {
                            handler.post(() -> Toast.makeText(mContext, "Failed to assign map", Toast.LENGTH_SHORT).show());
                        }
                    });
                });
                builder.show();
            }
        });
    }

}