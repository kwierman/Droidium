package com.tritium.droidium.sources;

import android.content.Context;

import com.tritium.droidium.datastream.DataEncoder;
import com.tritium.droidium.runcontrol.Run;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Created by kwierman on 1/14/15.
 * Interface for data sources
 */
public interface Source {
    public void OnPreRun(Context txt, Run run);
    public void OnPostRun(Context txt, Run run);
    List<DataEncoder> getEncoders();
    public Element describe(Element node, Document doc);
    public String getName();
    public String getDesc();
    public int getImage();
}
