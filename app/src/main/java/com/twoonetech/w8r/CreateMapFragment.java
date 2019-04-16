package com.twoonetech.w8r;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateMapFragment extends Fragment {

    private FragmentActivity mContext;
    private SharedViewModel model;
    private MapEditorFragment mapEditorFragment;
    private JSONObject mapJson;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mContext = (FragmentActivity) context;
        model = ViewModelProviders.of(mContext).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_map, container, false);
    }

    @SuppressLint("InflateParams")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        try {
            mapJson = new JSONObject("{\"nodes\":[{\"name\":\"Bar\",\"x\":\"0\",\"y\":\"-1\",\"type\":\"table\"},{\"name\":\"Initial Node\",\"x\":\"0\",\"y\":\"0\",\"type\":\"intersection\"}],\"edges\":[{\"node1\":\"Bar\",\"node2\":\"Initial Node\"}]}");
        } catch (JSONException e) {
            Log.e("CreateMapFragment","Failed to create map");
        }

        FragmentManager fm = getFragmentManager();

        mapEditorFragment = new MapEditorFragment();
        Bundle args = new Bundle();
        args.putString("mapJson",mapJson.toString());
        mapEditorFragment.setArguments(args);
        fm.beginTransaction().replace(R.id.map_container,mapEditorFragment).commit();

        LayoutInflater li = LayoutInflater.from(mContext);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        Button addNodeButton = view.findViewById(R.id.add_node_button);
        addNodeButton.setOnClickListener(view1 -> {
            View dialogView = li.inflate(R.layout.dialog_add_node,null);
            builder.setView(dialogView);
            EditText nodeId = dialogView.findViewById(R.id.node_id);
            EditText nodeX = dialogView.findViewById(R.id.x_position);
            EditText nodeY = dialogView.findViewById(R.id.y_position);
            CheckBox nodeType = dialogView.findViewById(R.id.is_table);
            builder.setPositiveButton("ADD", (dialogInterface, i) -> {
                if (nodeId.getText().toString().isEmpty() || nodeX.getText().toString().isEmpty() || nodeY.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "Missing text in input fields",Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray currentNodesJsonArray = mapJson.getJSONArray("nodes");
                        List<String> currentNodeNames = new ArrayList<>();
                        List<Pair<String,String>> nodeCoordinates = new ArrayList<>();
                        for (int j = 0; j < currentNodesJsonArray.length(); j++) {
                            currentNodeNames.add(currentNodesJsonArray.getJSONObject(j).getString("name"));
                            nodeCoordinates.add(Pair.create(currentNodesJsonArray.getJSONObject(j).getString("x"),currentNodesJsonArray.getJSONObject(j).getString("y")));
                        }
                        if (currentNodeNames.contains(nodeId.getText().toString()) || nodeCoordinates.contains(Pair.create(nodeX.getText().toString(),nodeY.getText().toString()))) {
                            Toast.makeText(mContext, "Node contains duplicate information",Toast.LENGTH_SHORT).show();
                        } else {
                            JSONObject newNode = new JSONObject()
                                    .put("name", nodeId.getText().toString())
                                    .put("x", nodeX.getText().toString())
                                    .put("y", nodeY.getText().toString());
                            if (nodeType.isChecked()) {
                                newNode.put("type", "table");
                            } else {
                                newNode.put("type", "intersection");
                            }
                            JSONArray nodes = mapJson.getJSONArray("nodes").put(newNode);
                            mapJson.put("nodes", nodes);
                            args.putString("mapJson", mapJson.toString());
                            mapEditorFragment = new MapEditorFragment();
                            mapEditorFragment.setArguments(args);
                            fm.beginTransaction().replace(R.id.map_container, mapEditorFragment).commit();
                        }
                    } catch (JSONException e) {
                        Log.e("CreateMapFragment", "Failed to add node");
                    }
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        Button addEdgeButton = view.findViewById(R.id.add_edge_button);
        addEdgeButton.setOnClickListener(view1 -> {
            View dialogView = li.inflate(R.layout.dialog_add_edge, null);
            builder.setView(dialogView);
            try {
                JSONArray currentNodesJsonArray = mapJson.getJSONArray("nodes");
                List<String> currentNodeNames = new ArrayList<>();
                for (int k = 0; k < currentNodesJsonArray.length(); k++) {
                    if (!currentNodesJsonArray.getJSONObject(k).getString("name").equals("Bar")) {
                        currentNodeNames.add(currentNodesJsonArray.getJSONObject(k).getString("name"));
                    }
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext,android.R.layout.simple_spinner_item, currentNodeNames);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner node1Spinner = dialogView.findViewById(R.id.node1_spinner);
                node1Spinner.setAdapter(dataAdapter);
                Spinner node2Spinner = dialogView.findViewById(R.id.node2_spinner);
                node2Spinner.setAdapter(dataAdapter);
                builder.setPositiveButton("ADD", (dialogInterface, i) -> {
                    try {
                        //CHECK HERE IF EDGE BETWEEN THESE NODES ALREADY EXISTS
                        JSONArray currentEdgesJsonArray = mapJson.getJSONArray("edges");
                        List<String> currentNode1Names = new ArrayList<>();
                        List<String> currentNode2Names = new ArrayList<>();
                        for (int l = 0; l < currentEdgesJsonArray.length(); l++) {
                            currentNode1Names.add(currentEdgesJsonArray.getJSONObject(l).getString("node1"));
                            currentNode2Names.add(currentEdgesJsonArray.getJSONObject(l).getString("node2"));
                        }

                        if (node1Spinner.getSelectedItem().toString().equals(node2Spinner.getSelectedItem().toString())) {
                            Toast.makeText(mContext, "Map can't contain loops", Toast.LENGTH_SHORT).show();
                        } else if ((currentNode1Names.contains(node1Spinner.getSelectedItem().toString()) && currentNode2Names.contains(node2Spinner.getSelectedItem().toString())) || (currentNode2Names.contains(node1Spinner.getSelectedItem().toString()) && currentNode1Names.contains(node2Spinner.getSelectedItem().toString()))) {
                            Toast.makeText(mContext, "Edge already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            String node1X = null;
                            String node1Y = null;
                            String node2X = null;
                            String node2Y = null;
                            for (int m = 0; m < currentNodesJsonArray.length(); m++) {
                                if (node1Spinner.getSelectedItem().toString().equals(currentNodesJsonArray.getJSONObject(m).getString("name"))) {
                                    node1X = currentNodesJsonArray.getJSONObject(m).getString("x");
                                    node1Y = currentNodesJsonArray.getJSONObject(m).getString("y");
                                }
                                if (node2Spinner.getSelectedItem().toString().equals(currentNodesJsonArray.getJSONObject(m).getString("name"))) {
                                    node2X = currentNodesJsonArray.getJSONObject(m).getString("x");
                                    node2Y = currentNodesJsonArray.getJSONObject(m).getString("y");
                                }
                            }
                            if (node1X.equals(node2X) || node1Y.equals(node2Y)) {
                                JSONObject edge = new JSONObject()
                                        .put("node1", node1Spinner.getSelectedItem().toString())
                                        .put("node2", node2Spinner.getSelectedItem().toString());
                                JSONArray edges = mapJson.getJSONArray("edges").put(edge);
                                mapJson.put("edges", edges);
                                args.putString("mapJson", mapJson.toString());
                                mapEditorFragment = new MapEditorFragment();
                                mapEditorFragment.setArguments(args);
                                fm.beginTransaction().replace(R.id.map_container, mapEditorFragment).commit();
                            } else {
                                Toast.makeText(mContext, "Edges must be perpendicular", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("CreateMapFragment","JSONException");
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } catch (JSONException e) {
                Log.e("CreateMapFragment","JSONException");
            }
        });

        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(view1 -> {
            View dialogView = li.inflate(R.layout.dialog_map_name, null);
            builder.setView(dialogView);
            EditText mapName = dialogView.findViewById(R.id.map_name);
            builder.setPositiveButton("SET", (dialogInterface, i) -> {
                Map<String,String> maps = model.getLiveMaps().getValue();
                maps.put(mapName.getText().toString(),mapJson.toString());
                model.getLiveMaps().setValue(maps);
                mContext.getSharedPreferences("MapNamesPrefs", Context.MODE_PRIVATE).edit().putString(mapName.getText().toString(),mapJson.toString()).apply();
                fm.beginTransaction().replace(R.id.fragment_container,new RobotsFragment()).commit();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(view2 -> {
            fm.beginTransaction().replace(R.id.fragment_container,new RobotsFragment()).commit();
        });
    }

}
