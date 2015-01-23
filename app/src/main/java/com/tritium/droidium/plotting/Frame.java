package com.tritium.droidium.plotting;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by kwierman on 9/23/14.
 */
public class Frame {
    private float height;
    private float width;
    private Paint paint;
    private final float padding = (float) 0.1;

    public Frame()
    {
        this.height = 10;
        this.width = 10;
        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.STROKE);

    }


    public void setArea(float width, float height) {
        this.height = height;
        this.width = width;

    }
    public void draw(Canvas canvas) {
        this.paint.setColor(Color.WHITE);
        float xMin = this.width;
        xMin*=padding;
        float xMax = this.width;
        xMax*=(1.0-padding);
        float yMin = this.height;
        yMin*=padding;
        float yMax = this.height;
        yMax*=1.0-padding;

        canvas.drawRect(xMin, yMin, xMax, yMax, paint);

    }


}
