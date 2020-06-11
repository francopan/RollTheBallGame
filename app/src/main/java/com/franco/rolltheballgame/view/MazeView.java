package com.franco.rolltheballgame.view;

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
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
    private final Runnable callback;
    private Paint backgroundPaint, linePaint, ballPaint, wallPaint, beginPaint, endPaint;

    private int xEntry, yEntry, xExit, yExit, currentLevel;
    private float xBall, yBall;

    // sensors
    private final SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener listener;
    private long previousTimestamp = 0l;

    // others
    private DisplayMetrics displayMetrics;
    private Vibrator vibrator;

    private boolean ready = false;
    private boolean finished = false;

    public MazeView(ComponentActivity context, SensorManager sensorManager, Sensor accelerometer,
                    Vibrator vibrator, Runnable winCallback, int[][] maze, int xEntry, int yEntry, int xExit, int yExit,
                    int currentLevel) {
        super(context);

        // maze
        this.maze = maze;
        this.lines = this.maze.length;
        this.cols = this.maze[0].length;
        this.xEntry = xEntry;
        this.yEntry = yEntry;
        this.xExit = xExit;
        this.yExit = yExit;
        this.xBall = -1;
        this.yBall = -1;
        this.currentLevel = currentLevel;

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
        this.vibrator = vibrator;
        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                updateBall(event);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        // callback
        this.callback = winCallback;

        // others
        displayMetrics = this.getContext().getResources().getDisplayMetrics();
        context.getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume() {
        if (!finished) {
            sensorManager.registerListener(listener, accelerometer,
                    SensorManager.SENSOR_DELAY_UI);
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause() {
        sensorManager.unregisterListener(listener);
    }

//    -------------------------------------------------------------------
//    sensor controls

    private void updateBall(SensorEvent event) {
        if (!this.ready) return;

        float x = event.values[0];  // positive=down left phone, negative=down right phone
        float y = event.values[1];  // positive=down bottom phone, negative=down top phone
        float z = event.values[2];  // dont used
        long timestamp = event.timestamp;   // calc how much the ball has to move between frames
        long millisFromLast = (timestamp - this.previousTimestamp) / 100_000;
        this.previousTimestamp = timestamp;
        float ballRadius = getBallRadius();
        float maxDelta = ballRadius * 2;

        // threshold min inclination
        if (Math.abs(x) < 1.2) x = 0; else x -= Math.signum(x) * 1.2;
        if (Math.abs(y) < 1.2) y = 0; else y -= Math.signum(y) * 1.2;
        if (x == 0 && y == 0) return;

        // calc delta
        float xBallDelta, yBallDelta;
        float xDelta = Math.max(-maxDelta, Math.min(maxDelta, x * millisFromLast * 0.03f));
        float yDelta = Math.max(-maxDelta, Math.min(maxDelta, y * millisFromLast * 0.03f));
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            xBallDelta = yDelta;
            yBallDelta = xDelta;
        } else {
            xBallDelta = xDelta;
            yBallDelta = yDelta;
        }

        // maze limit
        int lineMazePos = (int) pxToLine(this.yBall);
        int colMazePos = (int) pxToCol(this.xBall);
        if (x > 0) {
            int lineMazePosRadius = (int) pxToLine(this.yBall + ballRadius + yBallDelta);
            if (lineMazePos != lineMazePosRadius && maze[lineMazePosRadius][colMazePos] > 0) {
                yBallDelta = (lineToPx(lineMazePosRadius) - ballRadius) - this.yBall;
                this.vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        } else {
            int lineMazePosRadius = (int) pxToLine(this.yBall - ballRadius + yBallDelta);
            if (lineMazePos != lineMazePosRadius && maze[lineMazePosRadius][colMazePos] > 0) {
                yBallDelta = (lineToPx(lineMazePos) + ballRadius) - this.yBall;
                this.vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        }
        if (y > 0) {
            int colMazePosRadius = (int) pxToCol(this.xBall + ballRadius + xBallDelta);
            if (colMazePos != colMazePosRadius && maze[lineMazePos][colMazePosRadius] > 0) {
                xBallDelta = (colToPx(colMazePosRadius) - ballRadius) - this.xBall;
                this.vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        } else {
            int colMazePosRadius = (int) pxToCol(this.xBall - ballRadius + xBallDelta);
            if (colMazePos != colMazePosRadius && maze[lineMazePos][colMazePosRadius] > 0) {
                xBallDelta = (colToPx(colMazePos) + ballRadius) - this.xBall;
                this.vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        }

        // move
        this.yBall += yBallDelta;
        this.xBall += xBallDelta;

        // screen limit
        this.xBall = Math.max(ballRadius, Math.min(this.getRight() - ballRadius, this.xBall));
        this.yBall = Math.max(ballRadius, Math.min(this.getBottom() - ballRadius, this.yBall));

        // check win
        if (this.xExit == (int) pxToLine(this.yBall) && this.yExit == (int) pxToCol(this.xBall)) {
            this.finished = true;
            onPause();
            callback.run();
        } else {
            // redraw()
            this.invalidate();
        }

    }

//    -------------------------------------------------------------------
//    draw labirint

    private float lineToPx(float line) {
        return Math.max(0, Math.min(this.getBottom()-1, (line / lines) * this.getBottom()));
    }

    private float colToPx(float col) {
        return Math.max(0, Math.min(this.getRight()-1, (col / cols) * this.getRight()));
    }

    private float pxToLine(float px) {
        return Math.max(0, Math.min(lines-1, (px / this.getBottom()) * lines));
    }

    private float pxToCol(float px) {
        return Math.max(0, Math.min(cols-1, (px / this.getRight()) * cols));
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

        // Draw Level Number
        drawLevelNumber(canvas);

        // ready flag
        this.ready = true;
    }

    private void drawInnerSquares(int line, int col, Canvas canvas, Paint paint) {
        float startX = colToPx(col);
        float stopX = colToPx(col + 1);
        float startY = lineToPx(line);
        float stopY = lineToPx(line + 1);
        RectF mRectF = new RectF(startX, startY, stopX, stopY);
        canvas.drawRect(mRectF, paint);
    }

    private void drawLevelNumber(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setTextSize(80);
        canvas.drawText("Lv. " + this.currentLevel, 10, 60, paint);
    }

}
