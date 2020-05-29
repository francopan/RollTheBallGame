package com.franco.rolltheballgame;

import android.os.Bundle;
import android.widget.LinearLayout;

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

        linearLayout = findViewById(R.id.linearLayout);
        linearLayout.addView(new MazeView(this, maze, 0,0,0,0));
    }
}
