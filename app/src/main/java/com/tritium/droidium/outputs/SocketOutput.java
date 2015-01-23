package com.tritium.droidium.outputs;

import android.content.Context;

import com.tritium.droidium.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by kwierman on 1/14/15.
 */
public class SocketOutput implements Output {
    Socket socketDevice;
    SocketAddress endpoint;

    SocketOutput(String address, int port){
        socketDevice = new Socket();
        endpoint = new InetSocketAddress(address, port);
    }


    @Override
    public void OnPreRun(Context txt) {

        try {
            socketDevice.connect(endpoint);
        }
        catch(IOException e){
            //TODO: Fill in
        }

    }

    @Override
    public void OnPostRun(Context txt) {
        try {
            socketDevice.close();
        }
        catch (IOException e){
            //TODO: Fill in
        }
    }

    @Override
    public void Publish(byte[] data) {
        try {
            socketDevice.getOutputStream().write(data);
        }
        catch (IOException e){
            //TODO do something here.
        }
    }

    @Override
    public Element describe(Element node, Document doc) {
        return null;
    }

    @Override
    public String getName() {
        return "SocketOutput";
    }

    @Override
    public String getDesc() {
        return endpoint.toString();
    }

    @Override
    public int getImage() {
        return R.drawable.ic_wifi;
    }
}
