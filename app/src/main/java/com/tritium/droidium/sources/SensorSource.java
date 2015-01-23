package com.tritium.droidium.sources;


        import android.content.Context;
        import android.hardware.Sensor;

        import com.tritium.droidium.R;
        import com.tritium.droidium.datastream.DataEncoder;
        import com.tritium.droidium.runcontrol.RunManager;

        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;
        import android.os.Bundle;
        import android.os.Message;
        import android.text.format.Time;
        import android.util.Log;

        import org.w3c.dom.Document;
        import org.w3c.dom.Element;
        import org.w3c.dom.Node;

        import java.nio.ByteBuffer;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.zip.DataFormatException;
        import android.os.Handler;

/**
 * Created by kwierman on 9/23/14.
 */
public class SensorSource implements Source, SensorEventListener{
    private final Sensor device;
    DataEncoder sensorChangedEncoder;
    DataEncoder accuracyChangedEncoder;
    Handler handle;

    //TODO: the run can't be passed to the sensor off the bat, the sensor lifetime has to exist longer than the run

    public SensorSource(Sensor dev, RunManager run){
        //TODO: Implement a way to add the strings in for vendor, etc...

        device = dev;
        sensorChangedEncoder = run.createNewEncoder(dev.getName()+"_onSensorChanged");
        sensorChangedEncoder.add((long)0, "time_up");
        sensorChangedEncoder.add((float)0.0,"xValue" );
        sensorChangedEncoder.add((float)0.0,"yValue" );
        sensorChangedEncoder.add((float)0.0,"zValue" );

        //setup the encoder
        accuracyChangedEncoder = run.createNewEncoder(dev.getName()+"_onAccuracyChanged");
        accuracyChangedEncoder.add( (int)0, "i" );
        accuracyChangedEncoder.add( (int)0, "version" );
        accuracyChangedEncoder.add( (int)0 ,"min_delay");
        accuracyChangedEncoder.add( (int)0 ,"type");
        accuracyChangedEncoder.add( (float)0 ,"max_range");
        accuracyChangedEncoder.add( (float)0 ,"power");
        accuracyChangedEncoder.add( (float)0 ,"resolution");

        //handle = run.getCurrentRun().getIncomingDataHandler();
    }

        //to allow for copying.
    private SensorSource(Sensor dev){
        this.device=dev;

    }

    @Override
    public boolean equals(Object object) {

        if (object != null && object instanceof SensorSource){
            if(this.device.getName().equals( ((SensorSource)object).device.getName() )  )
                return true;
        }

        return false;
    }


    @Override //from Source
    public void OnPreRun(Context txt) {
        //connect the listener
        //TODO: Get The incoming data handler
        SensorManager man = (SensorManager) txt.getSystemService(Context.SENSOR_SERVICE);
        man.registerListener(this, device, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override //from Source
    public void OnPostRun(Context txt) {
        handle=null;
        SensorManager man = (SensorManager) txt.getSystemService(Context.SENSOR_SERVICE);
        man.unregisterListener(this, device);
    }

    @Override
    public List<DataEncoder> getEncoders() {
        List<DataEncoder> fEncoders = new ArrayList<>();
        fEncoders.add(sensorChangedEncoder);
        fEncoders.add(accuracyChangedEncoder);
        return fEncoders;
    }

    @Override
    public Element describe(Element node, Document doc) {
        Element el = doc.createElement("SensorSource");
        el.setAttribute("maxRange", ""+device.getMaximumRange() );
        //el.setAttribute("fifoMaxEventCount", ""+device.getFifoMaxEventCount() );
        //el.setAttribute("fifoReservedEventCount", ""+device.getFifoReservedEventCount() );
        el.setAttribute("minDelay", ""+device.getMinDelay() );
        el.setAttribute("name", ""+device.getName() );
        el.setAttribute("power", ""+device.getPower() );
        el.setAttribute("resolution", ""+device.getResolution() );
        //el.setAttribute("stringType", ""+device.getStringType() );
        el.setAttribute("type", ""+device.getType() );
        el.setAttribute("vendor", ""+device.getVendor() );
        el.setAttribute("version", ""+device.getVersion() );
        el.setAttribute("deviceString", ""+device.toString() );
        Element encoders = doc.createElement("encoders");
        sensorChangedEncoder.describe( encoders, doc);
        accuracyChangedEncoder.describe(encoders, doc);
        el.appendChild(encoders);
        node.appendChild(el);
        return el;
    }

    @Override
    public String getName() {
        return device.getName();
    }

    @Override
    public String getDesc() {
        return device.getVendor();
    }

    @Override
    public int getImage() {
        return R.drawable.ic_sd_card;
    }

        /*
    @Override
    public Source getCopy() {
        SensorSource src = new SensorSource(this.device);
        src.accuracyChangedEncoder = this.accuracyChangedEncoder;
        src.sensorChangedEncoder = this.sensorChangedEncoder;
        return src;
    }
    */

    @Override //fromSensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        //modify the fields
        //sensorChangedEncoder.modify((long)0, "timeup");
        //sensorChangedEncoder.modify((float)0.0,"xValue" );
        //sensorChangedEncoder.modify((float)0.0,"yValue" );
        //sensorChangedEncoder.modify((float)0.0,"zValue" );
        //publish this to the run
        if(handle!=null){
            Message newDataMessage = Message.obtain();
            newDataMessage.setData(this.sensorChangedEncoder.toBundle());
            handle.sendMessage(newDataMessage);
        }

        /*

        if(handler)


        if(run!=null){
            this.sensorEncoder.setValues(sensorEvent.timestamp, sensorEvent.accuracy, sensorEvent.values);
            Message newDataMessage = Message.obtain();
            newDataMessage.setData(this.sensorEncoder.getBundle());
            run.getIncomingDataHandler().sendMessage(newDataMessage);
        }
        */
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        if(handle!=null){
            Message newDataMessage = Message.obtain();
            newDataMessage.setData(this.accuracyChangedEncoder.toBundle());
            handle.sendMessage(newDataMessage);
        }
        /*
        Log.d("Sensor Event Accuracy Changed", ""+i);
        this.accuracyEncoder.setValues(i,
                sensor.getVersion(),
                sensor.getVendor(),
                sensor.getMinDelay(),
                sensor.getType(),
                sensor.getMaximumRange(),
                sensor.getName(),
                sensor.getPower(),
                sensor.getResolution());
        Message accuracyMessage = Message.obtain();
        accuracyMessage.setData(this.accuracyEncoder.getBundle());
        run.getIncomingDataHandler().sendMessage(accuracyMessage);
        */
    }



}
