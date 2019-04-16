package com.twoonetech.w8r;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.android.AndroidFullGraphRenderer;
import org.graphstream.ui.android_viewer.AndroidViewer;
import org.graphstream.ui.android_viewer.DefaultView;
import org.graphstream.ui.view.Viewer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapEditorFragment extends Fragment {

    //Find a way to connect the css file instead of using a string
    String styleString = "graph {\n" +
            "\n" +
            "}\n" +
            "\n" +
            "/* Insert styles for edges here */\n" +
            "edge {\n" +
            "    size: 14px;\n" +
            "    fill-color: #d1752a;\n" +
            "}\n" +
            "\n" +
            "edge.active {\n" +
            "    size: 20px;\n" +
            "    fill-color: #0fa6b2;\n" +
            "}\n" +
            "\n" +
            "/* Insert styles for all nodes here */\n" +
            "node {\n" +
            "    size: 40px, 40px;\n" +
            "    fill-color: #000000;\n" +
            "}\n" +
            "\n" +
            "/* Insert styles for tables here */\n" +
            "node.table {\n" +
            "    shape: box;\n" +
            "    size: 50px, 50px;\n" +
            "    fill-color: #12c5dd;\n" +
            "    text-alignment: left;\n" +
            "    text-mode:normal;\n" +
            "    text-size: 30;\n" +
            "    text-style: bold;\n" +
            "    text-color: #000000;\n" +
//            "    text-background-mode: plain;\n" +
//            "    text-background-color: #000000;\n" +
//            "    text-padding: 20;\n" +
            "}\n" +
            "\n" +
            "/* Insert styles for intersections here */\n" +
            "node.intersection {\n" +
            "    size: 50px, 50px;\n" +
            "    fill-color: #ef8729;\n" +
            "    text-alignment: left;\n" +
            "    text-mode:normal;\n" +
            "    text-size: 30;\n" +
            "    text-style: bold;\n" +
            "    text-color: #000000;\n" +
//            "    text-background-mode: plain;\n" +
//            "    text-background-color: #000000;\n" +
//            "    text-padding: 20;\n" +
            "}";

    private FragmentActivity mContext;
    private String mapJson;
    private AndroidViewer viewer;
    private AndroidFullGraphRenderer renderer;
    private DefaultView vw;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (FragmentActivity) context;
        mapJson = getArguments().getString("mapJson");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Graph graph;
        graph = new SingleGraph("map");
        renderer = new AndroidFullGraphRenderer();
        renderer.setContext(mContext);
        viewer = new AndroidViewer(graph, AndroidViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        graph.setAttribute("ui.stylesheet", styleString); //Add stylesheet to the graph
        try {
            JSONObject mapJsonObject = new JSONObject(mapJson);

            JSONArray nodesJsonArray = mapJsonObject.getJSONArray("nodes");
            for (int i = 0; i < nodesJsonArray.length(); i++) {
                JSONObject nodeJson = nodesJsonArray.getJSONObject(i);
                graph.addNode(nodeJson.getString("name"));
                Node node = graph.getNode(nodeJson.getString("name"));
                node.setAttribute("ui.class",nodeJson.getString("type"));
                node.setAttribute("ui.label",nodeJson.getString("name"));
                node.setAttribute("x", nodeJson.getInt("x"));
                node.setAttribute("y", nodeJson.getInt("y"));
            }

            JSONArray edgesJsonArray = mapJsonObject.getJSONArray("edges");
            for (int i = 0; i < edgesJsonArray.length(); i++) {
                JSONObject edgeJson = edgesJsonArray.getJSONObject(i);
                graph.addEdge("edge"+String.valueOf(i),edgeJson.getString("node1"),edgeJson.getString("node2"));
            }
        } catch (JSONException e) {
            Log.e("MapFragment","Failed to create map");
        }
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vw = (DefaultView) viewer.addView(AndroidViewer.DEFAULT_VIEW_ID, renderer);
        vw.setMouseManager(new NoDragMouseManager());
        return vw;
    }

}