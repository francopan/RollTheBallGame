package com.franco.rolltheballgame;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.franco.rolltheballgame.view.MazeView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int currentLevel;
    ArrayList<int[][]> maps;
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

    int maze2[][] = new int[][] {
            {1,1,1,1,1,1,1,1},
            {1,0,0,0,1,0,0,0},
            {1,0,1,0,1,0,1,1},
            {1,0,1,0,1,0,1,1},
            {1,0,1,0,1,0,1,1},
            {1,0,1,0,1,0,1,1},
            {0,0,1,0,0,0,1,1},
            {1,1,1,1,1,1,1,1}
    };

    int maze3[][] = new int[][] {
            {1,1,1,1,1,1,1,1},
            {1,1,0,0,0,0,0,0},
            {1,1,0,1,1,1,1,1},
            {1,1,0,0,0,0,0,1},
            {1,1,1,1,1,1,0,1},
            {1,1,0,0,0,1,0,1},
            {0,0,0,1,0,0,0,1},
            {1,1,1,1,1,1,1,1}
    };


    LinearLayout linearLayout;
    SensorManager sensorManager;
    Sensor accelerometer;
    Vibrator v;
    MediaPlayer mp, mp2;

    AssetFileDescriptor sourceStartGame;
    AssetFileDescriptor sourceFinishGame;

    public MainActivity() throws IOException {
        this.maps = new ArrayList<>();
        this.maps.add(this.maze);
        this.maps.add(this.maze2);
        this.maps.add(this.maze3);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mp = new MediaPlayer();
        this.mp2 = new MediaPlayer();
        sourceStartGame = getResources().openRawResourceFd(R.raw.gamestart);
        sourceFinishGame = getResources().openRawResourceFd(R.raw.tada);

        try {
            this.mp.setDataSource(sourceStartGame.getFileDescriptor(), sourceStartGame.getStartOffset(), sourceStartGame.getLength());
            this.mp.prepare();
            this.mp2.setDataSource(sourceFinishGame.getFileDescriptor(), sourceFinishGame.getStartOffset(), sourceFinishGame.getLength());
            this.mp2.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);
        this.v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        this.currentLevel = 1;
        this.sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer == null) {
            Toast.makeText(this, "O dispositivo não possui acelerômetro!",
                    Toast.LENGTH_LONG).show();
            finish();
        }
        this.makeGameView(currentLevel);
    }

    private void makeGameView(int currentLevel) {
        linearLayout = findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();
        linearLayout.addView(new MazeView(this, sensorManager, accelerometer, v, this::wonGame, this.maps.get(currentLevel - 1),
                6, 0, 1, 7, currentLevel));
        mp.start();
    }

    private void wonGame() {
        Toast.makeText(this, "Parabéns!", Toast.LENGTH_LONG).show();
        mp2.start();
        if (currentLevel == 3) {
            this.finish();
        } else {
            this.currentLevel++;
            this.makeGameView(currentLevel);
        }
    }

    private void resetGame(View view) {
        this.currentLevel = 1;
        this.makeGameView(this.currentLevel);
    }
}
