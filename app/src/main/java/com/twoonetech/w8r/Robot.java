package com.twoonetech.w8r;

import java.util.List;

public class Robot {


    private String ip;
    private String state;
    private String currentPosition;
    private String destination;
    private List<Integer> tables;

    public String getIp() {
        return ip;
    }

    public Robot(String ip) {
        this.ip = ip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<Integer> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }
}
