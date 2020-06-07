package com.franco.rolltheballgame;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.franco.rolltheballgame.view.MazeView;

public class MainActivity extends AppCompatActivity {

    int maze[][] = new int[][] {
            {1,1,1,1,1,1,1,1},
            {1,0,0,0,0,1,0,0},
            {1,0,1,1,0,1,0,1},
            {1,0,1,1,0,1,0,1},
            {1,0,1,0,0,1,0,1},
            {1,0,1,0,1,1,0,1},
            {0,0,1,0,0,0,0,1},
            {1,1,1,1,1,1,1,1}
    };

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer == null) {
            Toast.makeText(this, "O dispositivo não possui acelerômetro!",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        linearLayout = findViewById(R.id.linearLayout);
        linearLayout.addView(new MazeView(this, sensorManager, accelerometer, maze, 0, 0, 0, 0));

    }
}
