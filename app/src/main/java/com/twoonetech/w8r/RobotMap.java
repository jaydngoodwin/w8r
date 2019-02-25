package com.twoonetech.w8r;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RobotMap {

    String robotId;
    List<Node> nodes = new ArrayList<>();
    List<Edge> edges = new ArrayList<>();

    String jsonString = "{\"metadata\":{\"Robot ID\":\"C3-PO\"},\"nodes\":[{\"id\":1,\"neighbours\":[1,4],\"edges\":[1,2],\"type\":\"intersection\"},{\"id\":2,\"neighbours\":[3,1],\"edges\":[3,1],\"type\":\"table\"},{\"id\":3,\"neighbours\":[4,2],\"edges\":[4,3],\"type\":\"intersection\"},{\"id\":4,\"neighbours\":[5,3,1],\"edges\":[5,4,2],\"type\":\"intersection\"},{\"id\":5,\"neighbours\":[4],\"edges\":[5],\"type\":\"table\"}],\"edges\":[{\"id\":1,\"nodes\":[1,2],\"length\":181},{\"id\":2,\"nodes\":[1,4],\"length\":314},{\"id\":3,\"nodes\":[2,3],\"length\":211},{\"id\":4,\"nodes\":[3,4],\"length\":541},{\"id\":5,\"nodes\":[4,5],\"length\":111}]}";

    public RobotMap() throws JSONException {
        parseEdges(jsonString);
        parseNodes(jsonString);

    }


    public void parseEdges (String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject metadataObject = jsonObject.getJSONObject("metadata");
        JSONArray edgesArray = jsonObject.getJSONArray("edges");


        for (int i = 0; i <edgesArray.length() ; i++) {
            Edge e = new Edge();

            List<Integer> nodes = new ArrayList<>();
            JSONObject obj = edgesArray.getJSONObject(i);
            JSONArray edgeNodesArray = obj.getJSONArray("nodes");
            e.setId(obj.getInt("id"));
            e.setLength(obj.getDouble("length"));

            for (int j = 0; j <edgeNodesArray.length() ; j++) {
                nodes.add(edgeNodesArray.getInt(j));
            }

            e.addNodes(convertIntegers(nodes));
            edges.add(e);
        }

    }


    public void parseNodes(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject robotIdObject = jsonObject.getJSONObject("metadata");
        JSONArray nodesArray = jsonObject.getJSONArray("nodes");
        robotId = robotIdObject.getString("Robot ID");

        for (int i = 0; i < nodesArray.length(); i++) {

            List<Integer> nodeNeighbours = new ArrayList<>();
            List<Integer> nodeEdges = new ArrayList<>();

            JSONObject obj = nodesArray.getJSONObject(i);
            JSONArray neighboursArray = obj.getJSONArray("neighbours");
            JSONArray edgesArray = obj.getJSONArray("edges");


            Node node = new Node();
            node.setId(Integer.parseInt(obj.getString("id")));
            node.setType(obj.getString("type"));

            for (int j = 0; j < neighboursArray.length(); j++) {
                nodeNeighbours.add(neighboursArray.getInt(j));
            }

            node.addNeighbours(convertIntegers(nodeNeighbours));


            for (int k = 0; k < edgesArray.length(); k++) {
                nodeEdges.add(edgesArray.getInt(k));
            }

            node.addEdges(convertIntegers(nodeEdges));

            nodes.add(node);
        }

    }

    //Converts the List<Integer> to int[], so that it matches Node's type
    private static int[] convertIntegers(List<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }
}


//{
//        "metadata": {
//        "Robot ID": "C3-PO"
//        },
//        "nodes": [
//        {
//        "id": 1,
//        "neighbours": [
//        2,
//        4
//        ],
//        "edges": [
//        1,
//        2
//        ],
//        "type": "intersection"
//        },
//        {
//        "id": 2,
//        "neighbours": [
//        3,
//        1
//        ],
//        "edges": [
//        3,
//        1
//        ],
//        "type": "table"
//        },
//        {
//        "id": 3,
//        "neighbours": [
//        4,
//        2
//        ],
//        "edges": [
//        4,
//        3
//        ],
//        "type": "intersection"
//        },
//        {
//        "id": 4,
//        "neighbours": [
//        5,
//        3,
//        1
//        ],
//        "edges": [
//        5,
//        4,
//        2
//        ],
//        "type": "intersection"
//        },
//        {
//        "id": 5,
//        "neighbours": [
//        4
//        ],
//        "edges": [
//        5
//        ],
//        "type": "table"
//        }
//        ],
//        "edges": [
//        {
//        "id": 1,
//        "nodes": [
//        1,
//        2
//        ],
//        "length": 181
//        },
//        {
//        "id": 2,
//        "nodes": [
//        1,
//        4
//        ],
//        "length": 314
//        },
//        {
//        "id": 3,
//        "nodes": [
//        2,
//        3
//        ],
//        "length": 211
//        },
//        {
//        "id": 4,
//        "nodes": [
//        3,
//        4
//        ],
//        "length": 541
//        },
//        {
//        "id": 5,
//        "nodes": [
//        4,
//        5
//        ],
//        "length": 111
//        }
//        ]
//        }