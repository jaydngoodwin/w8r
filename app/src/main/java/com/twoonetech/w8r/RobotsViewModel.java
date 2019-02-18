package com.twoonetech.w8r;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

public class RobotsViewModel extends ViewModel {

    //liveRobotIps is a live list of the robot ips that are on the same network
    private MutableLiveData<List<String>> liveRobotIps;

    public LiveData<Map<String,Object>> getLiveRobotData(){
        if (liveRobotData == null) {
            //If the live data is null, instantiate it with a new MutableLiveData and set it's value to null
            liveRobotData = new MutableLiveData<>();
            liveRobotData.setValue(null);
        }
        return liveRobotData;
    }

    public void updateUserData(Map<String,Object> robotData){
        //Update the local robot data
        liveRobotData.setValue(robotData);
    }

    public Object getSpecificRobotData(String ip){
        //If given ip is not in the set of robots
        Methods.sendCommand("","","getStatus",new String[]{});
        Object robotData;
        if (!liveRobotData.getValue().keySet().contains(ip)){
            robotData = null;
        } else {
            robotData = liveRobotData.getValue().get(ip);
        }
        return robotData;
    }

    //Use DHCP to get ip address of all devices on network that ARE robots and put it into the live list
    public List<String> searchForRobots(){
        List<String> ips; //DO DHCP STUFF HERE
        liveRobotIps.postValue(ips); //Put updated list of ips into the live list
        return ips;
    }

}
