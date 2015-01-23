package com.tritium.droidium.sources;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.tritium.droidium.R;
import com.tritium.droidium.datastream.DataEncoder;
import com.tritium.droidium.runcontrol.Run;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Created by kwierman on 1/14/15.
 * GPS Source is to be used for location finding service integration into dat acquisitions.
 */
public class GPSSource implements Source, LocationListener {


    @Override//from location listener
    public void onLocationChanged(Location location) {

    }

    @Override //from location listener
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override //from location listener
    public void onProviderEnabled(String provider) {

    }

    @Override //from location listener
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void OnPreRun(Context txt, Run run) {

    }

    @Override
    public void OnPostRun(Context txt, Run run) {

    }

    @Override
    public List<DataEncoder> getEncoders() {
        return null;
    }

    @Override
    public Element describe(Element node, Document doc) {
        return null;
    }

    @Override
    public String getName() {
        return "Location Source";
    }

    @Override
    public String getDesc() {
        return "returns GPS Position";
    }

    @Override
    public int getImage() {
        return R.drawable.ic_launcher;
    }
}
