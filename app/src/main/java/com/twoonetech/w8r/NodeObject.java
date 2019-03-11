package com.twoonetech.w8r;

import java.util.ArrayList;
import java.util.List;

public class NodeObject {

    private int id;
    private int[] neighbours;
    private String type;
    private int[] edges;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getNeighbours() {
        return neighbours;
    }

    public void addNeighbours(int [] neighbours) {
        this.neighbours = neighbours;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int[] getEdges() {
        return edges;
    }

    public int getNextNeighbour() {
        int nextNeighbour = 0;
        for (int i = 0; i < neighbours.length; i++) {
            if (neighbours[i] == id) {
                nextNeighbour = neighbours[i + 1];
                return nextNeighbour;
            }
        }

        return -1;
    }

    public void addEdges(int[] edges) {
        this.edges = edges;
    }
}
