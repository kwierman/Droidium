package com.tritium.droidium;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.tritium.droidium.R;
import com.tritium.droidium.plotting.Axis;
import com.tritium.droidium.plotting.Grid;
import com.tritium.droidium.plotting.Plot;
import com.tritium.droidium.plotting.Frame;

import java.util.ArrayList;
import java.util.List;

/*
 * PlotView holds plotting utilities to be used
 */
public class PlotView extends View {

    private List<Plot> plots;//holds all of the plots on this surface
    private Axis xAxis;//holds the xAxis
    private Axis yAxis;//holds the yAxis
    private Frame frame; //holds the frame
    private Grid grid; //holds the grid

    private float xMin;
    private float xMax;
    private float yMin;
    private float yMax;
    private int nPoints;
    private final float padding = (float).1;

    private final static String TAG="PlotView";

    public PlotView(Context context) {
        super(context);
        init(null, 0);
    }

    public PlotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PlotView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        this.plots = new ArrayList<Plot>();
        this.xAxis = new Axis(Axis.Type.x);
        this.yAxis = new Axis(Axis.Type.y);
        this.frame = new Frame();
        this.grid = new Grid();

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PlotView, defStyle, 0);
        try{
            this.nPoints = a.getInteger(R.styleable.PlotView_nPoints, 1000);
            this.xAxis.nDivisions = a.getInteger(R.styleable.PlotView_nXDivisions,3);
            this.yAxis.nDivisions = a.getInteger(R.styleable.PlotView_nYDivisions,3);
            this.grid.setDivisions(this.xAxis.nDivisions, this.yAxis.nDivisions);
            this.xAxis.setLabelSize(a.getDimensionPixelSize(R.styleable.PlotView_xAxisLabelSize, 10) );
            this.yAxis.setLabelSize(a.getDimensionPixelSize(R.styleable.PlotView_yAxisLabelSize, 10) );
        }
        finally {
            a.recycle();
        }
        this.xMin=0;
        this.xMax=0;
        this.yMin=0;
        this.yMax=0;


    }

    public void setPlot(Plot p){
        p.setOwner(this);
        this.plots = new ArrayList<Plot>();
        this.plots.add(p);
        this.setXMin(p.getXMin() );
        this.xMax = p.getXMax();
        this.xAxis.max = p.getXMax();
        this.yMin = p.getYMin();
        this.yMax = p.getYMax();
        this.yAxis.min = p.getYMin();
        this.yAxis.max = p.getYMax();

    }

    public Plot newPlot(){
        Plot temp = new Plot(this);
        this.plots.add(temp);
        return temp;
    }
    public void checkX(float x)
    {
        if(x<xMin || xMin==0)
        {
            xMin = x;
            xAxis.min = xMin;
        }
        else if(x>xMax)
        {
            xMax = x;
            xAxis.max = xMax;
        }
    }
    public void setXMin(float x)
    {
        this.xMin = x;
        xAxis.min=this.xMin;
    }

    public void checkY(float y)
    {
        if(y<yMin|| yMin==0)
        {
            yMin = y;
            yAxis.min = yMin;
        }
        else if(y>yMax)
        {
            yMax = y;
            yAxis.max = yMax;
        }
    }
    /**
     * Translates an input x on the plot view and puts out the draw point on the canvas
     * @param x
     * @return
     */
    public float translateX(float x){

        float percent = (x-this.xMin)/(this.xMax - this.xMin);
        //float position = getWidth()*this.padding+percent*(getWidth()*(1-this.padding) - getWidth()*this.padding );
        float position = getWidth()*(this.padding+percent*( (1-this.padding)-this.padding  ) );
        return position;
    }

    public float translateY(float y){
        float percent = (y-this.yMin)/(this.yMax - this.yMin);
        float position = getHeight()*( (1-this.padding)-percent*( 1-2*this.padding )  );
        return position;
    }


    public int getNPoints()
    {
        return this.nPoints;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        this.grid.setArea(getWidth(), getHeight());
        this.grid.draw(canvas);

        this.frame.setArea( getWidth(), getHeight() );
        this.frame.draw(canvas);

        this.xAxis.setArea(getWidth(), getHeight());
        this.yAxis.setArea(getWidth(), getHeight());

        this.xAxis.draw(canvas);
        this.yAxis.draw(canvas);

        for(Plot p : plots)
            p.draw(canvas);
    }
}
