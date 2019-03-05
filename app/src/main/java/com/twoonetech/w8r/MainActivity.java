package com.twoonetech.w8r;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SharedViewModel model;
    private RobotRecyclerViewAdapter robotRecyclerViewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = ViewModelProviders.of(this).get(SharedViewModel.class);
        model.init();
        Map<String,String> ipNameMap = (Map<String,String>) PreferenceManager.getDefaultSharedPreferences(this).getAll();
        Iterator it = ipNameMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            model.addRobot(new Robot((String) pair.getKey(),(String) pair.getValue()));
            it.remove(); // avoids a ConcurrentModificationException
        }
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        robotRecyclerViewAdapter = new RobotRecyclerViewAdapter(model.getRobots().getValue());
        recyclerView.setAdapter(robotRecyclerViewAdapter);

        IntentIntegrator scanIntegrator = new IntentIntegrator(this)
                .setBeepEnabled(true)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                .setBarcodeImageEnabled(true)
                .setOrientationLocked(false)
                .setPrompt("SCAN QR CODE");
        FloatingActionButton scanButton = findViewById(R.id.scan_button);
        scanButton.setOnClickListener(view -> {
            scanIntegrator.initiateScan();
        });

//        Button help = findViewById(R.id.help);
//        help.setOnClickListener(view -> {
//
//        });

    }

    //when the camera has scanned something this will be executed
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String scannedString = scanResult.getContents();
            if (scannedString != null && isValidIp(scannedString)) {
                Toast.makeText(this, "Scanned: " + scannedString, Toast.LENGTH_SHORT).show();
                String appId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                Robot scannedRobot = new Robot(scannedString);
                String registerResponse = scannedRobot.register(appId);

                //if registering successful(server side), save the robots ID, IP address to shared prefs
                if (registerResponse.equals("registered_successfully")) {
                    Toast.makeText(this, "Robot registered", Toast.LENGTH_SHORT).show();
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putString("bob", scannedString).apply();
                    model.addRobot(scannedRobot);
                    //Might need to directly manipulate robots
                    robotRecyclerViewAdapter.setData(model.getRobots().getValue());

//                    LayoutInflater li = LayoutInflater.from(getApplicationContext());
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//                    View dialogView = li.inflate(R.layout.dialog_robot_name, null);
//                    builder.setView(dialogView);
//                    EditText robotName = dialogView.findViewById(R.id.robot_name);
//                    builder.setPositiveButton("SET", (dialogInterface, i) -> {
//                        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(robotName.toString(), scannedString).apply();
//                        model.addRobot(scannedRobot);
//                        //Might need to directly manipulate robots
//                        robotRecyclerViewAdapter.notifyDataSetChanged();
//                        //robots.add(new Robot(scannedIp,robotName.toString()));
//                    });
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
                } else {
                    Toast.makeText(this, "Failed to register robot", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this,"Not a valid QR code",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView robotName;
        private View robotState;

        private ViewHolder(View view) {
            super(view);
            //Get views
            robotName = view.findViewById(R.id.robot_name);
            robotState = view.findViewById(R.id.robot_state);
        }
    }

    public class RobotRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

        //RecyclerView dataset
        private List<Robot> robots;

        private RobotRecyclerViewAdapter(List<Robot> robots) {
            this.robots = robots;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.row_main, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            //Get element from the dataset at this position
            //Replace the contents of the view with this element

            //Get robot and extract its details
            Robot robot = robots.get(position);
            holder.robotName.setText(robot.getName());
            GradientDrawable bgShape = (GradientDrawable) holder.robotState.getBackground();
            bgShape.setColor(Color.BLACK);

            holder.itemView.setOnClickListener(view -> {
                FragmentManager fm = getSupportFragmentManager();

                Bundle args = new Bundle();
                args.putString("ip",robot.getIp());
                RobotFragment robotFragment = new RobotFragment();
                robotFragment.setArguments(args);

                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container,robotFragment).commit();
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return robots.size();
        }

        public void setData(List<Robot> robots) {
            this.robots = robots;
            notifyDataSetChanged();
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