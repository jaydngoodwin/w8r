package com.twoonetech.w8r;

import java.util.List;

public class Robot {

    private String ip;
    private String state;
    private String currentPosition;
    private String destination;
    private List<Integer> tables;

    public Robot(String ip) {
        this.ip = ip;
    }
}
