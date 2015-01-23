package com.tritium.droidium.plotting;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by kwierman on 9/23/14.
 */
public class Axis {
    public float min;
    public float max;
    public int nDivisions;
    private float width;
    private float height;
    private Paint paint;
    private final float padding =(float)0.1;
    private String precision;

    public enum Type{
        x,y;
    }
    private Type type;

    private final static String TAG="Axis";


    public Axis(float min, float max, int nDivisions){
        this.min=min;
        this.max=max;
        this.nDivisions=nDivisions;
        this.paint = new Paint();
        this.paint.setColor(Color.WHITE);
        this.precision="%0.2f";
    }
    public Axis(Type y) {
        this.type = y;
        this.min=0;
        this.max=1;
        this.nDivisions=3;
        this.width = 0;
        this.height = 0;
        this.paint = new Paint();
        this.paint.setColor(Color.WHITE);
        this.precision = "%.2f";
    }
    public void setArea(float xAxisWidth, float xAxisHeight) {
        this.width = xAxisWidth;
        this.height = xAxisHeight;
    }

    public void setLabelSize(float size)
    {
        this.paint.setTextSize(size);
    }
    public void draw(Canvas canvas) {

        float startX = 0;

        float stopX = 1;
        float startY = 0;
        float stopY = 1;
        switch (this.type) {
            case x :
                startX = this.width*this.padding;
                stopX = this.width*(1-this.padding);
                startY= this.height*(1-this.padding);
                stopY= this.height*(1-this.padding);
                for(int i=0; i<=nDivisions;i++)
                {
                    //this marks where things should go
                    float progress = i;
                    progress/=(nDivisions);
                    float x = startX + progress*(stopX-startX);
                    float y = startY;
                    float width=10;//10 pixels wide
                    float marker = min + ((float)i) /((float)nDivisions) *(max-min);
                    //hash
                    canvas.drawLine(x, startY, x, stopY+width, paint);
                    //marker
                    canvas.drawText(String.format(this.precision, marker), x+width, y+this.paint.getTextSize(), paint);
                }
                break;
            case y :
                startX = this.width*this.padding;
                stopX = this.width*this.padding;
                startY= this.height*this.padding;
                stopY= this.height*(1-this.padding);
                for(int i=0; i<=nDivisions;i++)
                {
                    //this marks where things should go
                    float progress = nDivisions-i;
                    progress/=(nDivisions);
                    float x = 0;
                    float y = startY + progress*(stopY-startY);
                    float width=10;//10 pixels wide
                    float marker = ((float)i) /((float)nDivisions) *(max-min);
                    //hash
                    canvas.drawLine(startX, y, startX-width, y, paint);
                    //marker
                    canvas.drawText(String.format(this.precision, marker) , x, y, paint);
                }
                break;
        }
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

}
