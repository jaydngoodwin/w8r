package com.twoonetech.w8r;

import android.app.AlertDialog;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private SharedViewModel model;
    private SharedPreferences robotsPrefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = ViewModelProviders.of(this).get(SharedViewModel.class);
        robotsPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        List<Robot> robots = new ArrayList<>();
        Map<String,String> ipNameMap = (Map<String,String>) robotsPrefs.getAll();
        Iterator it = ipNameMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            robots.add(new Robot((String) pair.getKey(),(String) pair.getValue()));
            it.remove(); // avoids a ConcurrentModificationException
        }
        model.getLiveRobots().setValue(robots); //set since this will NOT be in a background thread

        FragmentManager fm = getSupportFragmentManager();

        RobotsFragment robotsFragment = new RobotsFragment();

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container,robotsFragment).commit();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<Robot> robots = model.getLiveRobots().getValue();
                if (!robots.isEmpty()) {
                    for (Robot robot : robots) {
                        robot.updateState();
                        if (robot.getTables().isEmpty()) {
                            robot.updateTables();
                        }
                    }
                    model.getLiveRobots().postValue(robots);
                }
            }
        },0,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String scannedString = scanResult.getContents();
            if (scannedString != null && isValidIp(scannedString)) {
                if (model.getRobotWithIp(scannedString) == null) {
                    LayoutInflater li = LayoutInflater.from(this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    View dialogView = li.inflate(R.layout.dialog_robot_name, null);
                    builder.setView(dialogView);
                    EditText robotName = dialogView.findViewById(R.id.robot_name);
                    builder.setPositiveButton("SET", (dialogInterface, i) -> {
                        AsyncTask.execute(() -> {
                            Robot robot = new Robot(scannedString,robotName.getText().toString());
                            String registerResponse = robot.register(Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
                            //if (registerResponse != null && registerResponse.equals("ok")) {
                                PreferenceManager.getDefaultSharedPreferences(this).edit().putString(scannedString, robotName.getText().toString()).apply();
                                List<Robot> robots = model.getLiveRobots().getValue();
                                robots.add(robot);
                                model.getLiveRobots().postValue(robots); //post since this will be in a background thread
                            //} else {
                                Handler handler = new Handler(this.getMainLooper());
                                handler.post(() -> Toast.makeText(this, "Robot isn't turned on",Toast.LENGTH_LONG).show());
                            //}
                        });
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(this, "Robot already registered", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this,"Not a valid QR code",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean isValidIp (String ip) {
        try {
            if (ip == null || ip.isEmpty()) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if (parts.length != 4) {
                return false;
            }

            for (String s : parts) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if (ip.endsWith(".")) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}