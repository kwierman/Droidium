package com.tritium.droidium.sources;

import android.content.Context;

import com.tritium.droidium.datastream.DataEncoder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Created by kwierman on 1/14/15.
 */
public interface Source {
    public void OnPreRun(Context txt);
    public void OnPostRun(Context txt);
    List<DataEncoder> getEncoders();
    public Element describe(Element node, Document doc);
    public String getName();
    public String getDesc();
    public int getImage();
    //public Source getCopy();
}
