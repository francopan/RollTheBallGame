package com.franco.rolltheballgame;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.franco.rolltheballgame.view.MazeView;

import java.io.IOException;

import static androidx.core.content.ContextCompat.getSystemService;

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

    int maze1[][] = new int[][] {
            {1,0,0,0,1,1,1,1,1,0,0,0,1,1,1,1},
            {1,0,1,0,0,0,1,0,1,0,0,0,1,1,1,1},
            {1,0,0,1,1,0,1,0,1,0,0,0,1,1,1,1},
            {1,1,0,1,1,0,1,0,1,0,0,0,1,1,1,1},
            {1,1,0,1,1,0,1,0,1,0,0,0,1,1,1,1},
            {1,0,0,0,1,0,0,0,1,0,0,0,1,1,1,1},
            {0,0,1,0,1,1,1,1,1,0,0,0,1,1,1,1},
            {1,0,0,0,1,1,1,1,1,0,0,0,1,1,1,1},
            {1,0,0,0,1,1,1,1,1,0,0,0,1,1,1,1},
            {1,0,1,0,0,0,1,0,1,0,0,0,1,1,1,1},
            {1,0,0,1,1,0,1,0,1,0,0,0,1,1,1,1},
            {1,1,0,1,1,0,1,0,1,0,0,0,1,1,1,1},
            {1,1,0,1,1,0,1,0,1,0,0,0,1,1,1,1},
            {1,0,0,0,1,0,0,0,1,0,0,0,1,1,1,1},
            {0,0,1,0,1,1,1,1,1,0,0,0,1,1,1,1},
            {1,0,0,0,1,1,1,1,1,0,0,0,1,1,1,1}
    };

    int maze3[][] = new int[][] {
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
    Vibrator v;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        this.mp = new MediaPlayer();

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer == null) {
            Toast.makeText(this, "O dispositivo não possui acelerômetro!",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        // Play Start Song
        try {
            //this.mp.setDataSource("https://freesound.org/data/previews/60/60443_35187-lq.mp3");
            this.mp.setDataSource("https://freesound.org/data/previews/243/243020_4284968-lq.mp3");
            this.mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mp.start();

        linearLayout = findViewById(R.id.linearLayout);
        linearLayout.addView(new MazeView(this, sensorManager, accelerometer, maze,
                6, 0, 1, 7, v));
    }
}
