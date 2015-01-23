package com.tritium.droidium.plotting;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.tritium.droidium.PlotView;

/**
 * Created by kwierman on 9/23/14.
 */
public class Plot {
    public class PlotDatum{
        public float x;
        public float y;
    }

    protected List<PlotDatum> data;
    protected PlotView owner;
    protected Paint paint;

    private final static String TAG="Plot";

    public Plot(){
        paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);

        this.data  = new ArrayList<PlotDatum>();
        this.owner = null;

    }

    public Plot(PlotView context)
    {
        paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);

        this.data  = new ArrayList<PlotDatum>();
        this.owner = context;

    }
    public Plot(Plot other){
        paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);
        this.data = new ArrayList<PlotDatum>();
        for(PlotDatum d : other.data){
            PlotDatum temp = new PlotDatum();
            temp.x = d.x;
            temp.y = d.y;
            this.data.add(temp);
        }
        this.owner=  other.owner;
    }


    public void setOwner(PlotView ctx){
        this.owner = ctx;
    }

    public void setPoints(ArrayList<PlotDatum> p){
        this.data =p;
    }
    public PlotDatum getNewDatum(){
        return new PlotDatum();
    }


    public float getXMin(){
        float x= Float.MAX_VALUE;
        for(PlotDatum p : data){
            if(p.x<x) x=p.x;
        }
        return x;
    }
    public float getXMax(){
        float x= -Float.MAX_VALUE;
        for(PlotDatum p : data){
            if(p.x>x) x=p.x;
        }
        return x;
    }
    public float getYMin(){
        float x= Float.MAX_VALUE;
        for(PlotDatum p : data){
            if(p.y<x) x=p.y;
        }
        return x;
    }
    public float getYMax(){
        float x= -Float.MAX_VALUE;
        for(PlotDatum p : data){
            if(p.y>x) x=p.y;
        }
        return x;
    }


    public void addPoint(PlotDatum point)
    {
        if(this.data.size()>=owner.getNPoints() )
        {
            data.remove(0);
            //resize the left hand side of the axis.
            owner.setXMin(data.get(0).x );
        }
        data.add(point);
        owner.checkX( point.x );
        owner.checkY( point.y );
    }
    public void draw(Canvas canvas)
    {
        float prevX = this.owner.translateX(0);
        float prevY = this.owner.translateY(0);
        Log.d(TAG,"Plotting With: "+this.data.size()+" Points");

        boolean isNotFirst=false;
        for(PlotDatum point : this.data)
        {

            float x = this.owner.translateX( point.x );
            float y = this.owner.translateY( point.y );

            if(!isNotFirst){
                //this.owner.setXMin(x);
                //this.owner.checkX(x);
                //this.owner.checkY(y);
            }

            else
                canvas.drawLine(prevX, prevY, x, y, this.paint);
            //this.owner.checkX(x);
            isNotFirst =true;
            canvas.drawCircle(x,y,5,this.paint);
            prevX = x;
            prevY = y;
        }
        Log.d(TAG,"x,Y:"+prevX+","+prevY);

    }

}
