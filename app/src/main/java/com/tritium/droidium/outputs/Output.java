package com.tritium.droidium.outputs;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by kwierman on 1/14/15.
 */
public interface Output {
    public void OnPreRun(Context txt);
    public void OnPostRun(Context txt);
    public void Publish(byte[] data);
    public Element describe(Element node, Document doc);
    public String getName();
    public String getDesc();
    public int getImage();
}
