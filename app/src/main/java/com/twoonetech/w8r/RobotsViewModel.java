package com.twoonetech.w8r;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.Map;

public class RobotsViewModel extends ViewModel {

    //robots is a mapping between the ip addresses of each robot to an object which contains the status of the robot
    private MutableLiveData<Map<String,Object>> liveRobotData;

    public LiveData<Map<String,Object>> getLiveRobotData() {
        if (liveRobotData == null) {
            //If the live data is null, instantiate it with a new MutableLiveData and set it's value to null
            liveRobotData = new MutableLiveData<>();
            liveRobotData.setValue(null);
        }
        return liveRobotData;
    }

    public void updateUserData(Map<String,Object> robotData) {
        //Update the local robot data
        liveRobotData.setValue(robotData);
    }

    public Object getSpecificRobotData(String ip){
        //if given ip is not in the set of robots
        Object robotData;
        if (!liveRobotData.getValue().keySet().contains(ip)){
            robotData = null;
        } else {
            robotData = liveRobotData.getValue().get(ip);
        }
        return robotData;
    }



}
