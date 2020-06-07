package com.franco.rolltheballgame.misc;

import android.graphics.Color;
import android.graphics.Paint;

public class PaintStyles {

    private PaintStyles() {
    }

    public static Paint getBackgroundPaint() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        return paint;
    }

    public static Paint getLinePaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        return paint;
    }

    public static Paint getWallPaint() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        return paint;
    }

    public static Paint getBallPaint() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);
        return paint;
    }

}
