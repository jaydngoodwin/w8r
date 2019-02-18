package com.twoonetech.w8r;

import java.util.ArrayList;
import java.util.List;

public class Edge {
    private int id;
    private int[] nodes;
    private double length;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getNodes() {
        return nodes;
    }

    public void addNodes(int[] nodes) {
        this.nodes = nodes;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
