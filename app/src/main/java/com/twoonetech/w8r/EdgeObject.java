package com.twoonetech.w8r;

import java.util.ArrayList;
import java.util.List;

public class EdgeObject {
    private String id;
    private List<Integer> nodes = new ArrayList<>();
    private double length;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getNodes() {
        return nodes;
    }

//    public void addNodes(NodeObject[] nodes) {
//        this.nodes = new ArrayList<>();
//    }

    public void addNode(Integer node) {
        this.nodes.add(node);
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
