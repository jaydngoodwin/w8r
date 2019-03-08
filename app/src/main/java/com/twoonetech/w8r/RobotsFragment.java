package com.twoonetech.w8r;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;
//
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class RobotsFragment {
//
//    private SharedViewModel model;
//    private MainActivity.RobotsAdapter robotsAdapter;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        setContentView(R.layout.activity_main);
//
////        model = ViewModelProviders.of(this).get(SharedViewModel.class);
////        model.addRobot(new Robot("192.169.105.149"));
////
////        FragmentManager fm = getSupportFragmentManager();
////
////        Bundle args = new Bundle();
////        args.putString("ip","192.169.105.149");
////        RobotFragment robotFragment = new RobotFragment();
////        robotFragment.setArguments(args);
////
////        FragmentTransaction ft = fm.beginTransaction();
////        ft.replace(R.id.fragment_container,robotFragment).commit();
//
//        model = ViewModelProviders.of(this).get(SharedViewModel.class);
//        model.init();
//
//        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//
//        // use a linear layout manager
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        // specify an adapter
//        robotsAdapter = new MainActivity.RobotsAdapter(model.getLiveRobots().getValue());
//        recyclerView.setAdapter(robotsAdapter);
//
//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                model.updateRobots();
//            }
//        },0,1000);
//
//        model.getLiveRobots().observe(this, robots -> {
//            robotsAdapter.setData(robots);
//        });
//
//        IntentIntegrator scanIntegrator = new IntentIntegrator(this)
//                .setBeepEnabled(true)
//                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
//                .setBarcodeImageEnabled(true)
//                .setOrientationLocked(false)
//                .setPrompt("SCAN QR CODE");
//        FloatingActionButton scanButton = findViewById(R.id.scan_button);
//        scanButton.setOnClickListener(view -> {
//            scanIntegrator.initiateScan();
//        });
//
//    }
//
//    //when the camera has scanned something this will be executed
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
//        if (scanResult != null) {
//            String scannedString = scanResult.getContents();
//            Log.d("IP",scannedString);
//            if (scannedString != null && isValidIp(scannedString)) {
//                Toast.makeText(this, "Scanned: " + scannedString, Toast.LENGTH_SHORT).show();
//                AsyncTask.execute(() -> {
//                    model.addRobot(scannedString,"bob"); //"bob" is temporary, ask user for name
//                    //STATE IF ADDING FAILS HERE
//
////                    LayoutInflater li = LayoutInflater.from(getApplicationContext());
////                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
////                    View dialogView = li.inflate(R.layout.dialog_robot_name, null);
////                    builder.setView(dialogView);
////                    EditText robotName = dialogView.findViewById(R.id.robot_name);
////                    builder.setPositiveButton("SET", (dialogInterface, i) -> {
////                        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(robotName.toString(), scannedString).apply();
////                        model.addRobot(scannedRobot);
////                        //Might need to directly manipulate robots
////                        robotRecyclerViewAdapter.notifyDataSetChanged();
////                        //robots.add(new Robot(scannedIp,robotName.toString()));
////                    });
////                    AlertDialog alertDialog = builder.create();
////                    alertDialog.show();
//                });
//                Log.d("IP",scannedString);
//            } else {
//                Toast.makeText(this,"Not a valid QR code",Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    public class RobotsAdapter extends RecyclerView.Adapter<MainActivity.RobotsAdapter.RobotViewHolder> {
//
//        //RecyclerView dataset
//        private List<Robot> data;
//
//        public class RobotViewHolder extends RecyclerView.ViewHolder {
//
//            private TextView robotName;
//            private View robotState;
//
//            public RobotViewHolder(View view) {
//                super(view);
//                //Get views
//                robotName = view.findViewById(R.id.robot_name);
//                robotState = view.findViewById(R.id.robot_state);
//            }
//        }
//
//        private RobotsAdapter(List<Robot> data) {
//            this.data = data;
//        }
//
//        @NonNull
//        @Override
//        public MainActivity.RobotsAdapter.RobotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//            View view = inflater.inflate(R.layout.row_main, parent, false);
//            return new MainActivity.RobotsAdapter.RobotViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull MainActivity.RobotsAdapter.RobotViewHolder holder, final int position) {
//            //Get element from the dataset at this position
//            //Replace the contents of the view with this element
//
//            //Get robot and extract its details
//            Robot robot = data.get(position);
//            holder.robotName.setText(robot.getName());
//            GradientDrawable bgShape = (GradientDrawable) holder.robotState.getBackground();
//            switch (robot.getState()) {
//                case "idle":
//                    //bgShape.setColor(Color.parseColor("#"+String.valueOf(R.color.statusIdle)));
//                    bgShape.setColor(Color.parseColor("green"));
//                    break;
//                case "following":
//                    //bgShape.setColor(Color.parseColor("#"+String.valueOf(R.color.statusMoving)));
//                    bgShape.setColor(Color.parseColor("yellow"));
//                    break;
//                case "obstacle":
//                    //bgShape.setColor(Color.parseColor("#"+String.valueOf(R.color.statusObstacle)));
//                    bgShape.setColor(Color.parseColor("red"));
//                    break;
//            }
//
//            holder.itemView.setOnClickListener(view -> {
//                FragmentManager fm = getSupportFragmentManager();
//
//                Bundle args = new Bundle();
//                args.putString("ip",robot.getIp());
//                RobotFragment robotFragment = new RobotFragment();
//                robotFragment.setArguments(args);
//
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.replace(R.id.fragment_container,robotFragment).commit();
//            });
//        }
//
//        // Return the size of your dataset (invoked by the layout manager)
//        @Override
//        public int getItemCount() {
//            return data.size();
//        }
//
//        public void setData(List<Robot> newData) {
//            this.data = newData;
//            notifyDataSetChanged();
//        }
//
//    }
//
//    public static boolean isValidIp (String ip) {
//        try {
//            if (ip == null || ip.isEmpty()) {
//                return false;
//            }
//
//            String[] parts = ip.split( "\\." );
//            if (parts.length != 4) {
//                return false;
//            }
//
//            for (String s : parts) {
//                int i = Integer.parseInt( s );
//                if ( (i < 0) || (i > 255) ) {
//                    return false;
//                }
//            }
//            if (ip.endsWith(".")) {
//                return false;
//            }
//
//            return true;
//        } catch (NumberFormatException nfe) {
//            return false;
//        }
//    }
//}