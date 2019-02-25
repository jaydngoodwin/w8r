package com.twoonetech.w8r;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class TestActivity extends AppCompatActivity implements View.OnTouchListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ImageButton left = findViewById(R.id.left);
        left.setOnTouchListener(this);
        ImageButton right = findViewById(R.id.right);
        right.setOnTouchListener(this);
        ImageButton forward = findViewById(R.id.forward);
        forward.setOnTouchListener(this);
        ImageButton backward = findViewById(R.id.backward);
        backward.setOnTouchListener(this);
        ImageButton stop = findViewById(R.id.stop);
        stop.setOnClickListener(view -> RobotViewModel.sendCommand("192.168.105.149","stop",new String[]{}));
    }

    public boolean onTouch(View v, MotionEvent event){

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN :
                onClick(v);
                break;
            case MotionEvent.ACTION_UP :
                RobotViewModel.sendCommand("192.168.105.149","stop",new String[]{});
                break;
        }

        return true;
    }

    //Uses a switch based on what button was pressed
    public boolean onClick(View v) {
        switch (v.getId()){
            case R.id.left:
                RobotViewModel.sendCommand("192.168.105.149","left",new String[]{});
                break;
            case R.id.right:
                RobotViewModel.sendCommand("192.168.105.149","right",new String[]{});
                break;
            case R.id.forward:
                RobotViewModel.sendCommand("192.168.105.149","forward",new String[]{});
                break;
            case R.id.backward:
                RobotViewModel.sendCommand("192.168.105.149","backward",new String[]{});
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
