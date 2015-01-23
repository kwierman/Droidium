package com.tritium.droidium.runcontrol;

import com.tritium.droidium.outputs.Output;
import com.tritium.droidium.sources.Source;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.LogPrinter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.text.format.Time;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


/**
 * Created by kwierman on 1/14/15.
 */
public class Run extends Thread {

    static final String TAG = "RUNTHREAD";

    private ArrayList<Source> fSources;
    private ArrayList<Output> fOutputs;

    Time startTime;
    Time stopTime;

    RunManager context;

    Handler incomingDataHandler;//owned here, used by data sources to notify thread that new data has appeared
    Handler runStartHandler;//owned here, used by UI thread to notify this thread to start
    Handler runStopHandler;//owned here, used by UI thread to notify this thread to stop
    Handler runStopCallback;//Owned by this thread to tell the main thread that the run is finished

    public Run(RunManager txt){
        super();
        context=txt;
        this.fSources = new ArrayList<>();
        this.fOutputs = new ArrayList<>();
        startTime = new Time();
        stopTime = new Time();

;
    }

    public Run(Run other){
        super();
        this.context = other.context;
        this.fSources = other.fSources;
        this.fOutputs=other.fOutputs;
        this.startTime = new Time();
        this.stopTime= new Time();
        runStartHandler = other.runStartHandler;
        runStopHandler = other.runStopHandler;
        incomingDataHandler = other.incomingDataHandler;
    }


    public Document describeRun(){
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("run");

            Element startTimeElement = doc.createElement("start_time");
            startTimeElement.setAttribute("time", startTime.toString() );
            rootElement.appendChild(startTimeElement);

            Element stopTimeElement = doc.createElement("stop_time");
            stopTimeElement.setAttribute("time", stopTime.toString() );
            rootElement.appendChild(stopTimeElement);

            doc.appendChild(rootElement);
            Element src_doc = doc.createElement("sources");
            for(Source s : fSources){
                s.describe(src_doc, doc);
                //src_doc.appendChild(s.describe(doc) );
            }
            rootElement.appendChild(src_doc);
            Element out_doc = doc.createElement("outputs");
            for(Output o: fOutputs){
                //out_doc.appendChild(o.describe(doc));
            }
            rootElement.appendChild(out_doc);
            /*Element enc_doc = doc.createElement("encoders");
            for(DataEncoder e : fEncoders){
                e.describe(enc_doc, doc);
            }
            rootElement.appendChild(enc_doc);
            */
            return doc;
        }
        catch (ParserConfigurationException e){
            return null;
        }
    }


    public String toDocString() {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(this.describeRun()), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }


    /**
     * Method to run during the thread
     */
    @Override
    public void run() {
        Log.d(TAG, this.toDocString() );
        Looper.prepare();

        runStartHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                start();
            }
        };
        runStopHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                Looper.myLooper().quit();
            }
        };

        incomingDataHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                byte[] rec = msg.getData().getByteArray("data");
                if(rec!=null)
                    publish(rec);
            }
        };


        LogPrinter logger = new LogPrinter(Log.VERBOSE, "RUNLOG");
        Looper.myLooper().setMessageLogging(logger);

        //this loops over the

        for(Source s : fSources){
            s.OnPreRun(context,this);
        }
        for(Output o: fOutputs){
            o.OnPreRun(context);
        }

        startTime.setToNow();
        Log.d(TAG,"Run Starting at: "+startTime.toMillis(true));
        //setup the run start notifier
        Looper.loop();
        stopTime.setToNow();

        for(Source s : fSources){
            s.OnPostRun(context,this);
        }
        for(Output o : fOutputs){
            o.OnPostRun(context);
        }
        Log.d(TAG,"Run Ending at: "+stopTime.toMillis(true));
        //setup the run stop notifier
        runStopCallback.sendEmptyMessage(0);
    }


    public void publish(byte[] rec){
        for(Output o : fOutputs) o.Publish(rec);
    }

    public Handler getIncomingDataHandler(){
        return incomingDataHandler;
    }
    public Handler getRunStartHandler(){return runStartHandler;}
    public Handler getRunStopHandler(){return runStopHandler;}


    public boolean hasSource(Source src){
        return fSources.contains(src);
    }

    //Source manipulation
    public void addSource(Source src){
        if(!hasSource(src))
            this.fSources.add(src);
    }

    public void removeSource(Source src){
        this.fSources.remove(src);
    }

    public boolean hasOutput(Output out){
        return fOutputs.contains(out);
    }


    public void addOutput(Output out){
        if(!hasOutput(out))
            this.fOutputs.add(out);
    }

    public void removeOutput(Output out){
        this.fOutputs.remove(out);
    }

    public void setRunStopCallback(Handler callback){
        runStopCallback = callback;
    }
}
