package com.twoonetech.w8r;

import org.graphstream.graph.Node;

public class W8rNode {
    private String id;
    private Node n;
    private int xPos;
    private int yPos;


    public W8rNode(String id, Node node, int x, int y) {
        this.id = id;
        this.n = node;
        this.xPos = x;
        this.yPos = y;
        node.setAttribute("xy", x, y);

        // Adding the w8r ui class to the node
        node.setAttribute("ui.class", "w8r");
    }

    public String getId() {
        return this.id;
    }

    public void setNode(Node node) {
        this.n = node;
    }

    public Node getNode() {
        return this.n;
    }

    public void setX(int x) {
        this.xPos = x;
        n.setAttribute("x", x);
    }

    public void setY(int y) {
        this.yPos = y;
        n.setAttribute("y", y);
    }

    public void setXY(int x, int y) {
        this.xPos = x;
        this.yPos = y;
        n.setAttribute("xy", x, y);
    }

    public int getX() { return this.xPos;}

    public int getY() { return this.yPos;}
}