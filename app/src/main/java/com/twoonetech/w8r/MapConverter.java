package com.twoonetech.w8r;

import android.util.Log;

import org.json.*;

// Converting json map in form of coordinates to the graph form.
public class MapConverter {
    public static String make_string(int n) {
        return "edge"+String.valueOf(n);
    }
    public static void add_connection(String from, String to, String edge_id, JSONArray nodes, JSONArray old_nodes) {
        try {
            int from_ind=0, to_ind=0;
            for (int i = 0; i < old_nodes.length(); i++) {
                if (old_nodes.getJSONObject(i).getString("name").equals(from)) {
                    from_ind=i;
                }
            }
            for (int i = 0; i < old_nodes.length(); i++) {
                if (old_nodes.getJSONObject(i).getString("name").equals(to)) {
                    to_ind=i;
                }
            }
            int from_x = old_nodes.getJSONObject(from_ind).getInt("x");
            int from_y = old_nodes.getJSONObject(from_ind).getInt("y");
            int to_x = old_nodes.getJSONObject(to_ind).getInt("x");
            int to_y = old_nodes.getJSONObject(to_ind).getInt("y");
            int order;
            if (from_x == to_x) {
                if (from_y < to_y) {
                    order = 0;
                } else {
                    order = 2;
                }
            } else {
                if (from_x < to_x) {
                    order = 1;
                } else {
                    order = 3;
                }
            }
            nodes.getJSONObject(from_ind).getJSONArray("neighbours").put(order, to);
            nodes.getJSONObject(from_ind).getJSONArray("edges").put(order, edge_id);
        } catch (JSONException e) {
        }
    }
    public static String convert(String mapJson) {
        try {
            JSONObject mapJsonObject = new JSONObject(mapJson);
            JSONObject result = new JSONObject();
            JSONArray nodes = new JSONArray();
            JSONArray old_nodes = mapJsonObject.getJSONArray("nodes");
            for (int i = 0; i < old_nodes.length(); i++) {
                JSONObject old_node = old_nodes.getJSONObject(i);
                JSONArray neighbours = new JSONArray().put("0").put("0").put("0").put("0");
                JSONArray edges = new JSONArray().put("x").put("x").put("x").put("x");
                JSONObject new_node = new JSONObject()
                        .put("id", old_node.getString("name"))
                        .put("type", old_node.getString("type"))
                        .put("neighbours", neighbours)
                        .put("edges", edges);
                nodes.put(new_node);
            }
            JSONArray edges = new JSONArray();
            JSONArray old_edges = mapJsonObject.getJSONArray("edges");
            for (int i = 0; i < old_edges.length(); i++) {
                JSONObject old_edge = old_edges.getJSONObject(i);
                JSONArray endpoints = new JSONArray();
                String a = old_edge.getString("node1");
                String b = old_edge.getString("node2");
                String edge_id = make_string(i);
                endpoints.put(a);
                endpoints.put(b);
                add_connection(a, b, edge_id, nodes, old_nodes);
                add_connection(b, a, edge_id, nodes, old_nodes);
                JSONObject new_edge = new JSONObject()
                    .put("id", edge_id)
                    .put("nodes", endpoints)
                    .put("length",1.0);
                edges.put(new_edge);

            }
            JSONObject metadata = new JSONObject();
            result.put("nodes", nodes)
                    .put("edges", edges)
                    .put("metadata", metadata);
            return result.toString();
        } catch (JSONException e) {
            return null;
        }
    }
}
