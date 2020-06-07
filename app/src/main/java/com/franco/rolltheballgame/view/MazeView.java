package com.franco.rolltheballgame.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.activity.ComponentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.franco.rolltheballgame.misc.PaintStyles;

public class MazeView extends View implements LifecycleObserver {

    private int maze[][];
    private Paint backgroundPaint, linePaint, ballPaint, wallPaint;

    private int xEntry, yEntry, xExit, yExit;
    private float xBall, yBall;

    // sensors
    private final SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener listener;
    private long previoutTimestamp = 0l;

    // others
    private DisplayMetrics displayMetrics;

    public MazeView(ComponentActivity context, SensorManager sensorManager, Sensor accelerometer,
                    int[][] maze, int xEntry, int yEntry, int xExit, int yExit) {
        super(context);
        this.maze = maze;
        this.xEntry = xEntry;
        this.yEntry = yEntry;
        this.xExit = xExit;
        this.yExit = yExit;

        // ball
        this.xBall = xEntry;
        this.yBall = yEntry;

        // paints
        backgroundPaint = PaintStyles.getBackgroundPaint();
        linePaint = PaintStyles.getLinePaint();
        wallPaint = PaintStyles.getWallPaint();
        ballPaint = PaintStyles.getBallPaint();

        // sensors
        this.sensorManager = sensorManager;
        this.accelerometer = accelerometer;
        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                updateBall(event);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        // others
        displayMetrics = this.getContext().getResources().getDisplayMetrics();
        context.getLifecycle().addObserver(this);
    }


    private float pxFromDp(final float dp) {
        return dp * this.displayMetrics.density;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume() {
        sensorManager.registerListener(listener, accelerometer,
                SensorManager.SENSOR_DELAY_UI);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause() {
        sensorManager.unregisterListener(listener);
    }

//    -------------------------------------------------------------------
//    sensor controls

    private void updateBall(SensorEvent event) {
        float x = event.values[0];  // positive=down left phone, negative=down right phone
        float y = event.values[1];  // positive=down bottom phone, negative=down top phone
        float z = event.values[2];  // dont used
        long timestamp = event.timestamp;   // calc how much the ball has to move between frames

        // TODO impl

        this.previoutTimestamp = timestamp;
    }

//    -------------------------------------------------------------------
//    draw labirint

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int top = this.getTop();
        int bottom = this.getBottom();
        int left = this.getLeft();
        int right = this.getRight();

        // clear board
        RectF mRectF = new RectF(left, top, right, bottom);
        canvas.drawRect(mRectF, backgroundPaint);    // clear all board
        canvas.drawRect(mRectF, linePaint);         // draw border

        for (int x = 0; x < maze[0].length; x++) {
            for (int y = 0; y < maze[0].length; y++) {
                if (maze[x][y] == 1) {
                    this.drawInnerSquares(x, y, Float.valueOf(maze[0].length), canvas, wallPaint);
                }
            }
        }
    }

    private void drawInnerSquares(Integer x, Integer y, Float matrixSize, Canvas canvas, Paint paint) {
        float startX = (y / matrixSize) * this.getRight();
        float stopX = ((y + 1) / matrixSize) * this.getRight();
        float startY = (x / matrixSize) * this.getBottom();
        float stopY = ((x + 1) / matrixSize) * this.getBottom();
        RectF mRectF = new RectF(startX, startY, stopX, stopY);
        canvas.drawRect(mRectF, paint);
    }

}
