package com.twoonetech.w8r;

import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.List;

public class NodeObject {

    private int id;
    private int[] neighbours;
    private String type;
    private List<String> edges;
    private Node n;
    private int xPos;
    private int yPos;

    public NodeObject(int id, String type, int x, int y) {
        this.id = id;
        this.type = type;
        this.xPos = x;
        this.yPos = y;

    }

    public int getId() {
        return id;
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


    public List<String> getEdges() {
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

    public void setNode(Node node) {
        // Adding the relevant type to the node so the proper styles can be applied
        node.setAttribute("ui.class", type);
        this.n = node;
    }

    public Node getNode() {
        return this.n;
    }

    public void setX(int x) {
        this.xPos = x;
    }

    public void setY(int y) {
        this.yPos = y;
    }

    public int getX() { return this.xPos;}

    public int getY() { return this.yPos;}

    public void addEdges(List<String> edges) {
        this.edges = edges;
    }
}
