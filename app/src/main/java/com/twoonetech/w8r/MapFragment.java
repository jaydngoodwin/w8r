package com.twoonetech.w8r;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.android.AndroidFullGraphRenderer;
import org.graphstream.ui.android_viewer.AndroidViewer;
import org.graphstream.ui.android_viewer.DefaultView;
import org.graphstream.ui.layout.Layout;
import org.graphstream.ui.layout.Layouts;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {

    private String robotId;
    private List<NodeObject> nodes = new ArrayList<>();
    private List<EdgeObject> edges = new ArrayList<>();
    private List<W8rNode> w8rs = new ArrayList<>();


    // Tag for the logs
    private String ltag = "MainActivity";

    String jsonString = "{\n" +
            "  \"metadata\": {\n" +
            "    \"robot_id\": \"C3-PO\"\n" +
            "  },\n" +
            "  \"nodes\": [\n" +
            "    {\n" +
            "      \"id\": 1,\n" +
            "      \"neighbours\": [\n" +
            "        3,\n" +
            "        0,\n" +
            "        0,\n" +
            "        0\n" +
            "      ],\n" +
            "      \"edges\": [\n" +
            "        \"a\"\n" +
            "      ],\n" +
            "      \"coordinates\": {\n" +
            "        \"x\": 3,\n" +
            "        \"y\": 1\n" +
            "      },\n" +
            "      \"type\": \"bar\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 2,\n" +
            "      \"neighbours\": [\n" +
            "        6,\n" +
            "        3,\n" +
            "        0,\n" +
            "        0\n" +
            "      ],\n" +
            "      \"edges\": [\n" +
            "        \"b\",\n" +
            "        \"e\"\n" +
            "      ],\n" +
            "      \"coordinates\": {\n" +
            "        \"x\": 1,\n" +
            "        \"y\": 3\n" +
            "      },\n" +
            "      \"type\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 3,\n" +
            "      \"neighbours\": [\n" +
            "        2,\n" +
            "        4,\n" +
            "        6,\n" +
            "        0\n" +
            "      ],\n" +
            "      \"coordinates\": {\n" +
            "        \"x\": 3,\n" +
            "        \"y\": 3\n" +
            "      },\n" +
            "      \"edges\": [\n" +
            "        \"b\",\n" +
            "        \"c\",\n" +
            "        \"f\"\n" +
            "      ],\n" +
            "      \"type\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 4,\n" +
            "      \"neighbours\": [\n" +
            "        3,\n" +
            "        8,\n" +
            "        5,\n" +
            "        0\n" +
            "      ],\n" +
            "      \"edges\": [\n" +
            "        \"c\",\n" +
            "        \"d\",\n" +
            "        \"g\"\n" +
            "      ],\n" +
            "      \"coordinates\": {\n" +
            "        \"x\": 4,\n" +
            "        \"y\": 3\n" +
            "      },\n" +
            "      \"type\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 5,\n" +
            "      \"neighbours\": [\n" +
            "        4,\n" +
            "        0,\n" +
            "        0,\n" +
            "        0\n" +
            "      ],\n" +
            "      \"edges\": [\n" +
            "        \"d\"\n" +
            "      ],\n" +
            "      \"coordinates\": {\n" +
            "        \"x\": 5,\n" +
            "        \"y\": 3\n" +
            "      },\n" +
            "      \"type\": \"table\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 6,\n" +
            "      \"neighbours\": [\n" +
            "        3,\n" +
            "        0,\n" +
            "        0,\n" +
            "        0\n" +
            "      ],\n" +
            "      \"edges\": [\n" +
            "        \"f\"\n" +
            "      ],\n" +
            "      \"coordinates\": {\n" +
            "        \"x\": 3,\n" +
            "        \"y\": 4\n" +
            "      },\n" +
            "      \"type\": \"table\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 7,\n" +
            "      \"neighbours\": [\n" +
            "        8,\n" +
            "        11,\n" +
            "        2,\n" +
            "        0\n" +
            "      ],\n" +
            "      \"edges\": [\n" +
            "        \"j\",\n" +
            "        \"n\",\n" +
            "        \"e\"\n" +
            "      ],\n" +
            "      \"coordinates\": {\n" +
            "        \"x\": 1,\n" +
            "        \"y\": 5\n" +
            "      },\n" +
            "      \"type\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 8,\n" +
            "      \"neighbours\": [\n" +
            "        9,\n" +
            "        7,\n" +
            "        4,\n" +
            "        0\n" +
            "      ],\n" +
            "      \"edges\": [\n" +
            "        \"i\",\n" +
            "        \"g\",\n" +
            "        \"h\"\n" +
            "      ],\n" +
            "      \"coordinates\": {\n" +
            "        \"x\": 4,\n" +
            "        \"y\": 5\n" +
            "      },\n" +
            "      \"type\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 9,\n" +
            "      \"neighbours\": [\n" +
            "        8,\n" +
            "        12,\n" +
            "        0,\n" +
            "        0\n" +
            "      ],\n" +
            "      \"edges\": [\n" +
            "        \"i\",\n" +
            "        \"k\"\n" +
            "      ],\n" +
            "      \"coordinates\": {\n" +
            "        \"x\": 5,\n" +
            "        \"y\": 5\n" +
            "      },\n" +
            "      \"type\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 10,\n" +
            "      \"neighbours\": [\n" +
            "        11,\n" +
            "        0,\n" +
            "        0,\n" +
            "        0\n" +
            "      ],\n" +
            "      \"edges\": [\n" +
            "        \"l\"\n" +
            "      ],\n" +
            "      \"coordinates\": {\n" +
            "        \"x\": 0,\n" +
            "        \"y\": 6\n" +
            "      },\n" +
            "      \"type\": \"table\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 11,\n" +
            "      \"neighbours\": [\n" +
            "        7,\n" +
            "        10,\n" +
            "        0,\n" +
            "        0\n" +
            "      ],\n" +
            "      \"edges\": [\n" +
            "        \"l\",\n" +
            "        \"j\"\n" +
            "      ],\n" +
            "      \"coordinates\": {\n" +
            "        \"x\": 1,\n" +
            "        \"y\": 6\n" +
            "      },\n" +
            "      \"type\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 12,\n" +
            "      \"neighbours\": [\n" +
            "        9,\n" +
            "        0,\n" +
            "        0,\n" +
            "        0\n" +
            "      ],\n" +
            "      \"edges\": [\n" +
            "        \"k\"\n" +
            "      ],\n" +
            "      \"coordinates\": {\n" +
            "        \"x\": 5,\n" +
            "        \"y\": 6\n" +
            "      },\n" +
            "      \"type\": \"table\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"edges\": [\n" +
            "    {\n" +
            "      \"id\": \"a\",\n" +
            "      \"nodes\": [\n" +
            "        1,\n" +
            "        3\n" +
            "      ],\n" +
            "      \"length\": 2\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"b\",\n" +
            "      \"nodes\": [\n" +
            "        2,\n" +
            "        3\n" +
            "      ],\n" +
            "      \"length\": 2\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"c\",\n" +
            "      \"nodes\": [\n" +
            "        3,\n" +
            "        4\n" +
            "      ],\n" +
            "      \"length\": 1\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"d\",\n" +
            "      \"nodes\": [\n" +
            "        4,\n" +
            "        5\n" +
            "      ],\n" +
            "      \"length\": 1\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"e\",\n" +
            "      \"nodes\": [\n" +
            "        7,\n" +
            "        2\n" +
            "      ],\n" +
            "      \"length\": 2\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"f\",\n" +
            "      \"nodes\": [\n" +
            "        3,\n" +
            "        6\n" +
            "      ],\n" +
            "      \"length\": 1\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"g\",\n" +
            "      \"nodes\": [\n" +
            "        4,\n" +
            "        8\n" +
            "      ],\n" +
            "      \"length\": 2\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"h\",\n" +
            "      \"nodes\": [\n" +
            "        7,\n" +
            "        8\n" +
            "      ],\n" +
            "      \"length\": 3\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"i\",\n" +
            "      \"nodes\": [\n" +
            "        8,\n" +
            "        9\n" +
            "      ],\n" +
            "      \"length\": 1\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"j\",\n" +
            "      \"nodes\": [\n" +
            "        7,\n" +
            "        11\n" +
            "      ],\n" +
            "      \"length\": 1\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"k\",\n" +
            "      \"nodes\": [\n" +
            "        9,\n" +
            "        12\n" +
            "      ],\n" +
            "      \"length\": 1\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"l\",\n" +
            "      \"nodes\": [\n" +
            "        10,\n" +
            "        11\n" +
            "      ],\n" +
            "      \"length\": 1\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    //Unfortunately, I can't get it to find the stylesheet, so it has to be put directly into a string. We should find a way to solve that problem
    String styleString = "graph {\n" +
            "\n" +
            "}\n" +
            "\n" +
            "/* Insert styles for edges here */\n" +
            "edge {\n" +
            "    size: 12px;\n" +
            "    fill-color: #000000;\n" +
            "}\n" +
            "\n" +
            "/* Insert styles for all nodes here */\n" +
            "node {\n" +
            "    size: 50px, 50px;\n" +
            "    fill-color: #000000;\n" +
            "}\n" +
            "\n" +
            "/* Insert styles for w8rs here */\n" +
            "node.w8r {\n" +
            "    /* If you can find a way to get the url thingy working,\n" +
            "       you can use it to make w8rs and other things have a nice image */\n" +
            "    /*fill-image: url(\"potato.jpeg\");*/\n" +
            "    shape: diamond;\n" +
            "}\n" +
            "\n" +
            "/* Insert styles for tables here */\n" +
            "node.table {\n" +
            "    shape: box;\n" +
            "    fill-color: #fc1622;\n" +
            "}\n" +
            "\n" +
            "/* Insert styles for intersections here */\n" +
            "node.intersection {\n" +
            "\n" +
            "}";

    private AndroidViewer viewer = null ;
    private AndroidFullGraphRenderer renderer ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Debug","MapFragment : onCreate");

        try {
            parseNodes(jsonString);
            parseEdges(jsonString);
        } catch (JSONException e) {
            Log.d("Parsing error:", e.toString());
        }

        Graph graph = createGraph("Tutorial 1");

        //example adding a w8r to the map
        w8rs.add(new W8rNode("potato_w8r", graph.addNode("potato_w8r"), 6, 6));

        viewer = new AndroidViewer(graph, AndroidViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        renderer = new AndroidFullGraphRenderer();
        renderer.setContext(context);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (DefaultView) viewer.addView(AndroidViewer.DEFAULT_VIEW_ID, renderer);
    }

    public Graph createGraph(String gName) {
        Graph graph = new SingleGraph(gName);
        int iterator = 0;

        /*Log.v(ltag, getClass().getClassLoader().getResource(".").getPath());*/

        //Adding stylesheet to the graph
        graph.setAttribute("ui.stylesheet", styleString);


        Log.v(ltag, "Adding nodes to graph");
        for (NodeObject n : nodes) {
            Log.d("Node:", n.toString());
            graph.addNode(String.valueOf(n.getId()));

            Node node = graph.getNode(String.valueOf(n.getId()));

            //Sets the position of the node in the graph
            node.setAttribute("x", n.getX());
            node.setAttribute("y", n.getY());

            // Adding the graph node to the NodeObject
            n.setNode(node);

        }
        Log.v(ltag, "Adding edges to graph");
        for (EdgeObject e : edges) {

            graph.addEdge(
                    String.valueOf(e.getId()),
                    String.valueOf(e.getNodes().get(0)),
                    String.valueOf(e.getNodes().get(1)));
        }
        Log.v(ltag, "Edges added, returning graph");

        return graph;
    }

    /**
     * This method parses all Nodes from the JSON string and adds them to a List<Node>.
     *
     * @param json -> String to be parsed.
     * @throws JSONException -> If for some reason the parsing goes wrong it will throw an exception.
     */

    public void parseNodes(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject robotIdObject = jsonObject.getJSONObject("metadata");

        JSONArray nodesArray = jsonObject.getJSONArray("nodes");
        robotId = robotIdObject.getString("robot_id");

        Log.v(ltag, "Parsing nodes");

        for (int i = 0; i < nodesArray.length(); i++) {

            int xCoordinate = 0;
            int yCoordinate = 0;

            List<Integer> nodeNeighbours = new ArrayList<>();
            List<String> nodeEdges = new ArrayList<>();

            JSONObject obj = nodesArray.getJSONObject(i);
            JSONObject nodeCoordinates = obj.getJSONObject("coordinates");
            JSONArray neighboursArray = obj.getJSONArray("neighbours");
            JSONArray edgesArray = obj.getJSONArray("edges");
            int id = Integer.parseInt(obj.getString("id"));
            String type = obj.getString("type");

            for (int j = 0; j < neighboursArray.length(); j++) {
                nodeNeighbours.add(neighboursArray.getInt(j));
            }


            for (int k = 0; k < edgesArray.length(); k++) {
                nodeEdges.add(edgesArray.getString(k));
            }

            for (int h = 0; h <nodeCoordinates.length()-1 ; h++) {
                xCoordinate = nodeCoordinates.getInt("x");
                yCoordinate = nodeCoordinates.getInt("y");
            }


            NodeObject node = new NodeObject(id, type, xCoordinate, yCoordinate);

            //Uncomment this when we have position in the JSON string map
            //JSONArray xyPos = obj.getJSONArray("position");

            node.addEdges(nodeEdges);
            node.addNeighbours(convertIntegers(nodeNeighbours));

            //Uncomment this as well when we have JSON string coordinates
//            node.setX(xyPos.getInt(0));
//            node.setY(xyPos.getInt(1));

            nodes.add(node);
        }
    }


    //Converts the List<Integer> to int[], so that it matches Node and Edge's type.
    private static int[] convertIntegers(List<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = integers.get(i);
        }
        return ret;
    }


    /**
     * This method parses all Edges from the JSON string and adds them to a List<Edge>.
     *
     * @param json -> String to be parsed.
     * @throws JSONException -> If for some reason the parsing goes wrong it will throw an exception.
     */

    public void parseEdges(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray edgesArray = jsonObject.getJSONArray("edges");

        Log.v(ltag, "Parsing edges");

        for (int i = 0; i < edgesArray.length(); i++) {
            EdgeObject e = new EdgeObject();
            List<Integer> nodesID = new ArrayList<>();
            JSONObject obj = edgesArray.getJSONObject(i);
            JSONArray edgeNodesArray = obj.getJSONArray("nodes");
            e.setId(obj.getString("id"));
            e.setLength(obj.getDouble("length"));

            for (int j = 0; j < edgeNodesArray.length(); j++) {
                nodesID.add(edgeNodesArray.getInt(j));
            }

            for (NodeObject n:nodes) {
                int iterator = 0;
                for (int k:nodesID) {
                    if (n.getId() == nodesID.get(iterator)) {
                        e.addNode(n.getId());
                    }
                    iterator++;
                }
            }

            edges.add(e);
        }

        Log.d("FinishLoop", "parseEdges: ");

    }
}