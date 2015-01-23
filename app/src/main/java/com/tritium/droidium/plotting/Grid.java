package com.tritium.droidium.plotting;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by kwierman on 9/23/14.
 */
public class Grid {
    private Paint paint;
    private float width;
    private float height;
    private int xDiv;
    private int yDiv;
    private final float padding = (float)0.1;

    private float hash;

    public Grid()
    {
        this.paint = new Paint();
        this.paint.setColor(Color.WHITE);
        this.width=0;
        this.height=0;
        this.xDiv=0;
        this.yDiv=0;
        this.hash=10;
    }

    public void setDivisions(int x, int y){
        this.xDiv=x;
        this.yDiv=y;
    }

    public void setArea(int width, int height) {
        this.width=width;
        this.height=height;
    }

    public void draw(Canvas canvas){
        //draw the x hashes
        float startX = this.width*this.padding;
        float stopX = this.width*(1-this.padding);
        float startY = this.height*(this.padding);
        float stopY = this.height*(1-this.padding);

        float fullWidth= stopX-startX;
        float fullHeight=stopY-startY;

        for(int i=0; i<xDiv;i++){
            float xValue = startX+((float) i)*(stopX-startX)/((float)xDiv);

            for(int j=0;j<(int)(fullHeight/this.hash);j++ )
            {
                float yValue = startY+((float)j)*hash;

                if(j%2==0)
                    canvas.drawLine(xValue, yValue, xValue, yValue+hash, paint);
            }
        }

        for(int i=0; i<yDiv;i++){
            float yValue = stopY - ((float) i)*(stopY-startY)/((float)yDiv);

            for(int j=0;j<(int)(fullWidth/this.hash);j++ )
            {
                float xValue = startX+((float)j)*hash;

                if(j%2==0)
                    canvas.drawLine(xValue, yValue, xValue+hash, yValue, paint);
            }
        }


    }



}
