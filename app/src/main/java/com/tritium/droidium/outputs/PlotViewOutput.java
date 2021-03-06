package com.tritium.droidium.outputs;

import android.content.Context;

import com.tritium.droidium.R;
import com.tritium.droidium.datastream.DataEncoder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by kwierman on 1/23/15.
 */
public class PlotViewOutput implements Output {
    DataEncoder mEncoder;

    @Override
    public void OnPreRun(Context txt) {

    }

    @Override
    public void OnPostRun(Context txt) {

    }

    @Override
    public void Publish(byte[] data) {

    }

    @Override
    public Element describe(Element node, Document doc) {
        return null;
    }

    @Override
    public String getName() {
        return "PlotViewOutput";
    }

    @Override
    public String getDesc() {
        if(mEncoder!=null){
            return "Attached to: "+Integer.toString(mEncoder.getID());
        }
        return "Unattached";
    }

    @Override
    public int getImage() {
        return R.drawable.ic_plot;
    }
}
