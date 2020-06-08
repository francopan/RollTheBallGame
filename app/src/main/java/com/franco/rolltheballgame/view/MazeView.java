package com.franco.rolltheballgame.view;

import android.content.res.Configuration;
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

    private final int maze[][];
    private final float lines, cols;
    private Paint backgroundPaint, linePaint, ballPaint, wallPaint, beginPaint, endPaint;

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
        this.lines = this.maze.length;
        this.cols = this.maze[0].length;
        this.xEntry = xEntry;
        this.yEntry = yEntry;
        this.xExit = xExit;
        this.yExit = yExit;
        this.xBall = -1;
        this.yBall = -1;

        // paints
        backgroundPaint = PaintStyles.getBackgroundPaint();
        linePaint = PaintStyles.getLinePaint();
        wallPaint = PaintStyles.getWallPaint();
        ballPaint = PaintStyles.getBallPaint();
        beginPaint = PaintStyles.getBeginPaint();
        endPaint = PaintStyles.getEndPaint();

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
        long millisFromLast = (timestamp - this.previoutTimestamp) / 100_000;
        this.previoutTimestamp = timestamp;

        // threshold min inclination
        if (Math.abs(x) < 1.2) x = 0; else x -= Math.signum(x) * 1.2;
        if (Math.abs(y) < 1.2) y = 0; else y -= Math.signum(y) * 1.2;

        if (x == 0 && y == 0) return;

        // TODO implements ball limits positions

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.xBall += y * millisFromLast * 0.03;
            this.yBall += x * millisFromLast * 0.03;
        } else {
            this.xBall += x * millisFromLast * 0.03;
            this.yBall += y * millisFromLast * 0.03;
        }

        this.invalidate();
    }

//    -------------------------------------------------------------------
//    draw labirint

    private float pxFromDp(final float dp) {
        return dp * this.displayMetrics.density;
    }

    private float lineToPx(float line) {
        return (line / lines) * this.getBottom();
    }

    private float colToPx(float col) {
        return (col / cols) * this.getRight();
    }

    private float getBallRadius() {
        float unit = Math.min(lineToPx(1), colToPx(1));
        return unit * 0.20f;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int top = this.getTop();
        int bottom = this.getBottom();
        int left = this.getLeft();
        int right = this.getRight();

        // TODO FIX esse sistemas de coordenadas. mt confuso....

        // initialize ball
        if (xBall == -1) { // not initialized
            this.xBall = colToPx(yEntry + 0.5f);
            this.yBall = lineToPx(xEntry + 0.5f);
        }

        // clear board
        RectF mRectF = new RectF(left, top, right, bottom);
        canvas.drawRect(mRectF, backgroundPaint);    // clear all board
        canvas.drawRect(mRectF, linePaint);         // draw border

        // paint walls
        for (int line = 0; line < maze[0].length; line++) {
            int[] vLine = maze[line];
            for (int col = 0; col < vLine.length; col++) {
                if (vLine[col] == 1) {
                    this.drawInnerSquares(line, col, canvas, wallPaint);
                }
            }
        }

        // paint begin / end
        this.drawInnerSquares(xEntry, yEntry, canvas, beginPaint);
        this.drawInnerSquares(xExit, yExit, canvas, endPaint);

        // paint ball
        canvas.drawCircle(xBall, yBall, getBallRadius(), ballPaint);
    }

    private void drawInnerSquares(int line, int col, Canvas canvas, Paint paint) {
        float startX = colToPx(col);
        float stopX = colToPx(col + 1);
        float startY = lineToPx(line);
        float stopY = lineToPx(line + 1);
        RectF mRectF = new RectF(startX, startY, stopX, stopY);
        canvas.drawRect(mRectF, paint);
    }

}
