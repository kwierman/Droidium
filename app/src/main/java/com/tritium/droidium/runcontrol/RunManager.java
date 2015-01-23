package com.tritium.droidium.runcontrol;

import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tritium.droidium.datastream.DataEncoder;
import com.tritium.droidium.outputs.Output;
import com.tritium.droidium.sources.SensorSource;
import com.tritium.droidium.sources.Source;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwierman on 1/14/15.
 * Run Manager is the communication point between the run thread and the UI thread.
 * In order to add your own type of source or output, do the following:
 * RunManager.addAvailableSource(sen);
 * RunManager.setSourceActive(sen);
 * Or, for outputs:
 * RunManager.addAvailableOutput(sen);
 * RunManager.setOutputActive(sen);
 * In order to remove them from the available sensors:
 * runManager.removeAvailableSensor(sen);
 * runManager.removeAvailableOutput(out);
 *
 */
public class RunManager extends Application{

    private static final String TAG="RUNManager";

    private Run currentRun;
    private ArrayList<Source> availableSources;
    private ArrayList<Output> availableOutputs;


    @Override
    public void onCreate()
    {
        currentRun = new Run(this);//setup the new run to work with the run manager.
        super.onCreate();
        availableSources = new ArrayList<>();
        availableOutputs = new ArrayList<>();
        //now probe the system for available sources and outputs, and get the encoders.
        //repeatRun=false;
        Log.v(TAG, "Starting up...");
        SensorManager manager =(SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
        Log.v(TAG, "Found Manager");
        List<Sensor> devices = manager.getSensorList(Sensor.TYPE_ALL);
        Log.d(TAG, "Found: "+devices.size()+" devices");

        for(Sensor s: devices){
            SensorSource tmp = new SensorSource(s, this);
            availableSources.add(tmp);
        }
    }

    public Run getCurrentRun(){
        if (currentRun.isAlive() )
                return null;
        return currentRun;
    }


    public DataEncoder createNewEncoder(String name){
        //int n_enc = this.get
        List<DataEncoder> encList = this.getEncoders();

        DataEncoder enc = new DataEncoder(encList.size(), name);
        return enc;
    }

    List<DataEncoder> getEncoders(){
        //for each of the sources query for encoders, and get them back
        List<DataEncoder> encList = new ArrayList<>();
        for(Source s: availableSources){
            for(DataEncoder d : s.getEncoders())
                encList.add(d);
        }
        return encList;
    }


    public List<Source> getAvailableSources(){
        return availableSources;
    }

    public List<Output> getAvailableOutputs(){
        return availableOutputs;
    }

    public void startRun(){
        currentRun.setRunStopCallback(new Handler(){
                @Override
                public void handleMessage(Message msg){
                    currentRun = new Run(currentRun);
                }
            }
        );
        if( !currentRun.isAlive() ){
            currentRun.start();
            //currentRun.getRunStartHandler().sendEmptyMessage(0);
        }
    }

    public void stopRun(){
        if(currentRun.isAlive() ){
            currentRun.getRunStopHandler().sendEmptyMessage(0);
        }
    }

}
