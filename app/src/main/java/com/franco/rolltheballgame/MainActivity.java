package com.franco.rolltheballgame;

import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.franco.rolltheballgame.view.MazeView;

public class MainActivity extends AppCompatActivity {

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = findViewById(R.id.linearLayout);
        linearLayout.addView(new MazeView(this, this.getWindow(), new Matrix(), 0,0,0,0));

    }
}
