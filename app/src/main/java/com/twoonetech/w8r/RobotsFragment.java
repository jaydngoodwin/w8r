package com.twoonetech.w8r;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

public class RobotsFragment extends Fragment {

    private SharedViewModel model;
    private RobotsAdapter robotsAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_robots, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        robotsAdapter = new RobotsAdapter(model.getLiveRobots().getValue());
        recyclerView.setAdapter(robotsAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        model.getLiveRobots().observe(this, robots -> {
            robotsAdapter.setData(robots);
        });

        IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity())
                .setBeepEnabled(true)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                .setBarcodeImageEnabled(true)
                .setOrientationLocked(false)
                .setPrompt("SCAN QR CODE");
        Button scanButton = view.findViewById(R.id.scan_button);
        scanButton.setOnClickListener(view1 -> scanIntegrator.initiateScan());

        Button helpButton = view.findViewById(R.id.help_button);
        helpButton.setOnClickListener(view12 -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            HelpFragment helpFragment = new HelpFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container,helpFragment).addToBackStack("help").commit();
        });
    }

    public class RobotsAdapter extends RecyclerView.Adapter<RobotsAdapter.RobotViewHolder> {

        //RecyclerView dataset
        private List<Robot> data;

        public class RobotViewHolder extends RecyclerView.ViewHolder {

            private TextView robotName;
            private TextView robotState;

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
            View view = inflater.inflate(R.layout.row_main, parent, false);
            return new RobotsAdapter.RobotViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RobotsAdapter.RobotViewHolder holder, final int position) {
            //Get element from the dataset at this position
            //Replace the contents of the view with this element

            //Get robot and extract its details
            Robot robot = data.get(position);
            holder.robotName.setText(robot.getName());
            switch (robot.getState()) {
                case "idle":
                    holder.robotState.setText(robot.getState());
                    break;
                case "following":
                    holder.robotState.setText(robot.getState());
                    break;
                case "obstacle":
                    holder.robotState.setText(robot.getState());
                    break;
            }

            holder.itemView.setOnClickListener(view -> {
                FragmentManager fm = getFragmentManager();

                Bundle args = new Bundle();
                args.putString("ip",robot.getIp());
                RobotFragment robotFragment = new RobotFragment();
                robotFragment.setArguments(args);

                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container,robotFragment).addToBackStack("robot").commit();
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
}