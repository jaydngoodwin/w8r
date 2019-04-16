package com.twoonetech.w8r;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RobotsFragment extends Fragment {

    private FragmentActivity mContext;
    private SharedViewModel model;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (FragmentActivity) context;
        model = ViewModelProviders.of(mContext).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_robots, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        FragmentManager fm = getFragmentManager();

        Button newMapButton = view.findViewById(R.id.map_button);
        Button helpButton = view.findViewById(R.id.help_button);
        FloatingActionButton scanButton = view.findViewById(R.id.scan_button);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        RobotsAdapter robotsAdapter = new RobotsAdapter(model.getLiveRobots().getValue());
        recyclerView.setAdapter(robotsAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext,layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        model.getLiveRobots().observe(this, robots -> {
            robotsAdapter.setData(robots);
        });

        IntentIntegrator scanIntegrator = IntentIntegrator.forSupportFragment(this);
        scanIntegrator.setBeepEnabled(false);
        scanIntegrator.setOrientationLocked(false);
        scanIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        scanIntegrator.setBarcodeImageEnabled(true);
        scanIntegrator.setPrompt("Scan Robot QR Code");
        scanButton.setOnClickListener(view1 -> scanIntegrator.initiateScan());

        newMapButton.setOnClickListener(view2 -> {
            fm.beginTransaction().replace(R.id.fragment_container,new CreateMapFragment()).addToBackStack(null).commit();
        });

        helpButton.setOnClickListener(view3 -> {
            fm.beginTransaction().replace(R.id.fragment_container,new HelpFragment()).addToBackStack(null).commit();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String scannedString = scanResult.getContents();
            if (isValidIp(scannedString)) {
                if (model.getRobotWithIp(scannedString) == null) {
                    LayoutInflater li = LayoutInflater.from(mContext);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    View dialogView = li.inflate(R.layout.dialog_robot_name, null);
                    builder.setView(dialogView);
                    EditText robotName = dialogView.findViewById(R.id.robot_name);
                    builder.setPositiveButton("SET", (dialogInterface, i) -> {
                        AsyncTask.execute(() -> {
                            Robot robot = new Robot(scannedString, robotName.getText().toString());
                            String registerResponse = robot.register(Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID));
                            Handler handler = new Handler(mContext.getMainLooper());
                            if (registerResponse != null && registerResponse.equals("ok")) {
                                robot.setAppId(Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID)); //COULD PUT THIS IN ROBOT?
                                mContext.getSharedPreferences("RobotNamesPrefs", MODE_PRIVATE).edit().putString(scannedString, robotName.getText().toString()).apply();
                                List<Robot> robots = model.getLiveRobots().getValue();
                                robots.add(robot);
                                model.getLiveRobots().postValue(robots); //post since this will be in a background thread
                                handler.post(() -> Toast.makeText(mContext, "Robot successfully registered", Toast.LENGTH_SHORT).show());
                            } else {
                                handler.post(() -> Toast.makeText(mContext, "Robot isn't turned on", Toast.LENGTH_SHORT).show());
                            }
                        });
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(mContext, "Robot already registered", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "Not a valid QR code", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, "Failed to scan QR code", Toast.LENGTH_SHORT).show();
        }
    }

    public class RobotsAdapter extends RecyclerView.Adapter<RobotsAdapter.RobotViewHolder> {

        //RecyclerView dataset
        private List<Robot> data;

        public class RobotViewHolder extends RecyclerView.ViewHolder {

            private TextView robotName;
            private ImageView robotState;

            public RobotViewHolder(View view) {
                super(view);
                //Get views
                robotName = view.findViewById(R.id.robot_name);
                robotState = view.findViewById(R.id.robot_state);
            }
        }

        private RobotsAdapter(List<Robot> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public RobotsAdapter.RobotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.row_robots, parent, false);
            return new RobotsAdapter.RobotViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RobotsAdapter.RobotViewHolder holder, final int position) {
            //Get element from the dataset at this position
            //Replace the contents of the view with this element

            //Get robot and extract its details
            Robot robot = data.get(position);
            holder.robotName.setText(robot.getName());
            if (robot.getState() == null) {
                holder.robotState.setImageResource(R.drawable.idle_icon);
            } else {
                switch (robot.getState()) {
                    case "idle":
                        holder.robotState.setImageResource(R.drawable.idle_icon);
                        break;
                    case "following": //CHANGE THIS TO "SERVING" (or "MOVING")?
                        holder.robotState.setImageResource(R.drawable.serving_icon);
                        break;
                    case "obstacle":
                        holder.robotState.setImageResource(R.drawable.obstacle_icon);
                        break;
                }
            }

            holder.itemView.setOnClickListener(view -> {
                Bundle args = new Bundle();
                args.putString("ip",robot.getIp());
                RobotFragment robotFragment = new RobotFragment();
                robotFragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,robotFragment).addToBackStack(null).commit();
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return data.size();
        }

        public void setData(List<Robot> newData) {
            this.data = newData;
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
            return !ip.endsWith(".");
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}