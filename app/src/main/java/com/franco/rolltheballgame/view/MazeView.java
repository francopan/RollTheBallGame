package com.franco.rolltheballgame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.View;

public class MazeView extends View {

    private int maze[][];
    private Paint linePaint, ballPaint, wallPaint;

    private int xEntry, yEntry, xExit, yExit;
    private float xBall, yBall;

    private DisplayMetrics displayMetrics;

    public MazeView(Context context, int maze[][], int xEntry, int yEntry, int xExit, int yExit) {
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
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.BLUE);
        linePaint.setStrokeWidth(5);

        wallPaint = new Paint();
        wallPaint.setStyle(Paint.Style.FILL);
        wallPaint.setColor(Color.GREEN);

        ballPaint = new Paint();
        ballPaint.setStyle(Paint.Style.FILL);
        ballPaint.setColor(Color.YELLOW);

        // others
        displayMetrics = this.getContext().getResources().getDisplayMetrics();

    }

    private float pxFromDp(final float dp) {
        return dp * this.displayMetrics.density;
    }

//    -------------------------------------------------------------------
//

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int top = this.getTop();
        int bottom = this.getBottom();
        int left = this.getLeft();
        int right = this.getRight();

        RectF mRectF = new RectF(left, top, right, bottom);
        canvas.drawRect(mRectF, linePaint);

        for (int x = 0; x < maze[0].length; x++) {
            for (int y = 0; y < maze[0].length; y++) {
                if (maze[x][y] == 1) {
                    this.drawInnerSquares(x,y, Float.valueOf(maze[0].length), canvas, wallPaint);
                }
            }
        }
    }

    private void drawInnerSquares(Integer x, Integer y, Float matrixSize, Canvas canvas, Paint paint) {
        float startX = (y/matrixSize) *  this.getRight();
        float stopX = ((y+1)/matrixSize) *  this.getRight();
        float startY = (x/matrixSize) * this.getBottom();
        float stopY = ((x+1)/matrixSize) * this.getBottom();
        RectF mRectF = new RectF(startX, startY, stopX, stopY);
        canvas.drawRect(mRectF, paint);
    }

}
